package com.example.rmitestclient;

import org.springframework.remoting.httpinvoker.HttpInvokerProxyFactoryBean;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
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

    public StateMachinePlugin httpConnect(String beanName) {
        HttpInvokerProxyFactoryBean invoker = new HttpInvokerProxyFactoryBean();
        invoker.setServiceInterface(StateMachinePlugin.class);
        invoker.setServiceUrl("rmi://127.0.0.1:8080/" + beanName);
        invoker.afterPropertiesSet();
        return (StateMachinePlugin) invoker.getObject();
    }
}
