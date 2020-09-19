package com.example.rmitestclient;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.statemachine.action.Action;
import org.springframework.statemachine.guard.Guard;
import org.springframework.stereotype.Service;

@Service
public class RemoteBeanService {

    public StateMachinePlugin connect(String beanName) {
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceInterface(StateMachinePlugin.class);
        factory.setServiceUrl("rmi://127.0.0.1:1099/" + beanName);
        factory.afterPropertiesSet();
        return (StateMachinePlugin) factory.getObject();
    }

    public Guard<String, String> loadGuard(String beanName) {
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceInterface(Guard.class);
        factory.setServiceUrl("rmi://127.0.0.1:1099/" + beanName);
        factory.afterPropertiesSet();
        return (Guard<String, String>) factory.getObject();
    }

    public Action<String, String> loadAction(String beanName) {
        RmiProxyFactoryBean factory = new RmiProxyFactoryBean();
        factory.setServiceInterface(Action.class);
        factory.setServiceUrl("rmi://127.0.0.1:1099/" + beanName);
        factory.afterPropertiesSet();
        return (Action<String, String>) factory.getObject();
    }

    public StateMachinePlugin httpConnect(String beanName) {
        HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
        invoker.setServiceInterface(StateMachinePlugin.class);
        invoker.setServiceUrl("rmi://127.0.0.1:8080/" + beanName);
        invoker.afterPropertiesSet();
        return (StateMachinePlugin) invoker.getObject();
    }
}
