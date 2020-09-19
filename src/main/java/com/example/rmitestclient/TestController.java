package com.example.rmitestclient;

//import com.alibaba.fastjson.JSON;
//import com.google.gson.Gson;
import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.ExternalizableSerializer;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.config.StateMachineBuilder;
import org.springframework.statemachine.guard.Guard;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.util.*;

@RestController
public class TestController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private RemoteBeanService remoteBeanService;

    private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {

        @SuppressWarnings("rawtypes")
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
//            kryo.setReferences(true);
//            kryo.setRegistrationRequired(false);
////            kryo.register(StateMachineBuilder.Builder.class, new ExternalizableSerializer());

//            UnmodifiableCollectionsSerializer.registerSerializers(kryo);
//            ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
//                    .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
            return kryo;
        }
    };

    public Kryo getInstance() {
        return kryoThreadLocal.get();
    }

    public byte[] serialize(Object obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Output output = new Output(out);
        Kryo kryo = getInstance();
        kryo.writeClassAndObject(output, obj);
        output.flush();
        return out.toByteArray();

    }

    @SuppressWarnings("unchecked")
    private Plugin deserialize(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        Kryo kryo = getInstance();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Input input = new Input(in);
        return kryo.readObject(input, Plugin.class);
    }

    @Bean
    public Action<String, String> testAction() {
        return ctx -> System.out.println("Test: " + ctx.getTarget().getId());
    }

    @Bean
    public Action<String, String> test1Action() {
        return ctx -> System.out.println("Test: 1 " + ctx.getTarget().getId());
    }

    @Bean
    public Action<String, String> test2Action() {
        return ctx -> System.out.println("Test: 2 " + ctx.getTarget().getId());
    }

    @Bean
    public Action<String, String> test3Action() {
        return ctx -> System.out.println("Test: 3 " + ctx.getTarget().getId());
    }

    @Bean
    public Guard<String, String> test1Guard() {
        return ctx -> true;
    }

    @Bean
    public Guard<String, String> test2Guard() {
        return ctx -> false;
    }

    @Bean
    public Guard<String, String> test3Guard() {
        return ctx -> false;
    }

    public StateMachineBuilder.Builder<String, String> parsePluginTree(List<Plugin> plugins, StateMachineBuilder.Builder<String, String> builder) throws Exception {
        for (Plugin plugin: plugins) {
            if (plugin.getSources().size() == 1 && plugin.getTargets().size() == 1) {
                Set<String> states = new HashSet<>();
                for (State s: plugin.getSources()) {
                    states.add(s.getName());
                }
                for (State s: plugin.getTargets()) {
                    states.add(s.getName());
                }
                String source = plugin.getSources().get(0).getName();
                String target = plugin.getTargets().get(0).getName();
                builder.configureTransitions().withExternal()
                        .source(source).target(target)
                        .event(plugin.getEvent()).action(getAction(plugin.getActions().get(0)));

            } else if (plugin.getSources().size() == 1 && plugin.getTargets().size() > 1) {
                String source = plugin.getSources().get(0).getName();

                builder.configureTransitions().withChoice()
                        .source(source)
                        .first(plugin.getTargets().get(0).getName(), (Guard<String, String>) context.getBean(plugin.getGuards().get(0)))
                        .then(plugin.getTargets().get(1).getName(), (Guard<String, String>) context.getBean(plugin.getGuards().get(1)),
                                (Action<String, String>) context.getBean(plugin.getActions().get(1)))
                        .last(plugin.getTargets().get(2).getName(), (Action<String, String>) context.getBean(plugin.getActions().get(2)));

            }
            if (plugin.getChildren() !=null && plugin.getChildren().size() > 0) {
                return parsePluginTree(plugin.getChildren(), builder);
            }
        }
        return builder;
    }

    public Guard<String, String> getGuard(String guard) {
        // remote:guard  or local:guard
        String[] temp = guard.split(":");
        String loadType = temp[0];
        String guardName = temp[1];
        if (loadType.equals("local")) {
            return (Guard<String, String>) context.getBean(guardName);
        } else {
            return remoteBeanService.loadGuard(guardName);
        }
    }

    public Action<String, String> getAction(String action) {
        // remote:guard  or local:guard
        String[] temp = action.split(":");
        String loadType = temp[0];
        String actionName = temp[1];
        if (loadType.equals("local")) {
            return (Action<String, String>) context.getBean(actionName);
        } else {
            return remoteBeanService.loadAction(actionName);
//            RemoteAction1 remoteAction1 = new RemoteAction1();
//            byte[] data = JSON.toJSONBytes(remoteAction1);
//            Action<String, String> action1 = JSON.parseObject(data, Action.class);
//            return action1;
        }
    }

    @GetMapping("/plugin")
    public void plugin() throws Exception {
        State state1 = new State();
        state1.setName("S1");

        State state2 = new State();
        state2.setName("S2");

        State state3 = new State();
        state3.setName("S3");

        State state4 = new State();
        state4.setName("S4");

        State state5 = new State();
        state5.setName("S5");

        Plugin plugin2 = new Plugin();
        plugin2.setSources(Arrays.asList(state2));
        plugin2.setTargets(Arrays.asList(state3, state4, state5));
        plugin2.setActions(Arrays.asList("", "test2Action", "test3Action"));
        plugin2.setGuards(Arrays.asList("test1Guard", "test2Guard", "test3Guard"));

        Plugin plugin4 = new Plugin();
        plugin4.setSources(Arrays.asList(state2));
        plugin4.setTargets(Arrays.asList(state3));
        plugin4.setEvent("E2");
        plugin4.setActions(Arrays.asList("local:test2Action"));
//        plugin4.setGuards(Arrays.asList("test1Guard", "test2Guard", "test3Guard"));

        Plugin plugin1 = new Plugin();
        plugin1.setSources(Arrays.asList(state3));
        plugin1.setTargets(Arrays.asList(state1));
        plugin1.setEvent("E1");
        plugin1.setActions(Arrays.asList("remote:remoteAction1"));
        plugin1.setChildren(Arrays.asList(plugin4));

        Plugin plugin = new Plugin();
        plugin.setSources(Arrays.asList(state1));
        plugin.setTargets(Arrays.asList(state2));
        plugin.setEvent("E");
        plugin.setActions(Arrays.asList("local:testAction"));
        plugin.setChildren(Arrays.asList(plugin1));

        byte[] data = JSON.toJSONBytes(plugin);
        Plugin plugin3 = JSON.parseObject(data, Plugin.class);

        List<State> stateList = Arrays.asList(state1, state2, state3, state4, state5);
        Set<String> states = new HashSet<>();
        for (State s: stateList) {
            states.add(s.getName());
        }
        String initialState = stateList.get(0).getName();

        StateMachineBuilder.Builder<String, String> builder = new StateMachineBuilder.Builder<String, String>();

        builder.configureStates().withStates().initial(initialState).states(states);

        builder = parsePluginTree(Arrays.asList(plugin3), builder);

        StateMachine<String, String> stateMachine = builder.build();
        stateMachine.start();
        System.out.println(stateMachine.getState().getId());
        stateMachine.sendEvent("E");
        System.out.println(stateMachine.getState().getId());
        stateMachine.sendEvent("E2");
        System.out.println(stateMachine.getState().getId());
        stateMachine.sendEvent("E1");
        System.out.println(stateMachine.getState().getId());
        stateMachine.stop();
    }

    @GetMapping("/build")
    public void build() throws Exception {
        StateMachineBuilder.Builder<String, String> builder = new StateMachineBuilder.Builder<String, String>();
        StateMachinePlugin stateMachinePlugin = remoteBeanService.connect("draftPlugin");

        // can not pass a builder to remote
        byte[] data = stateMachinePlugin.configure(serialize(builder));
//        builder = deserialize(data);
//        StateMachineBuilder.Builder<String, String> builder2 = deserialize(data);

        Set<String> stateSets = new HashSet<String>();
        stateSets.add("S3");
        stateSets.add("S4");
        builder.configureTransitions().withExternal()
                .source("S1").target("S2").action(new CustomAction("http://12306.com/test"));
//        builder2.configureStates().withStates().states(stateSets).initial("S1");
//        builder2.configureTransitions().withExternal()
//                .source("S2")
//                .target("S3")
//                .event("E2").and().withChoice().source("S6").first("S4").then("S5").last("S7").and()
//        .withJoin().sources("s2", "s3").target("s5").and().withJunction().source("s2").first("s")
//        .then("s3").then("s").and().withEntry().source("S").target("2").and().withExit().source().target();
//        StateMachine<String, String> stateMachine = builder2.build();
//        stateMachine.start();
//        System.out.println(stateMachine.getState());
//        stateMachine.sendEvent("E1");
//        System.out.println(stateMachine.getState());
//        stateMachine.stop();
    }
}
