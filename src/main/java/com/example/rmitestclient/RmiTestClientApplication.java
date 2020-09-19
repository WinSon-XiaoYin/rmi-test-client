package com.example.rmitestclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;

@SpringBootApplication
public class RmiTestClientApplication {

//    @Bean
//    public RmiProxyFactoryBean rmiProxyFactoryBean(){
//        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
//        rmiProxyFactoryBean.setServiceUrl("rmi://127.0.0.1:1099/testPlugin");
//        rmiProxyFactoryBean.setServiceInterface(TestPlugin.class);
//        return rmiProxyFactoryBean;
//
//    }

    public static void main(String[] args) {
        SpringApplication.run(RmiTestClientApplication.class, args);
    }

}
