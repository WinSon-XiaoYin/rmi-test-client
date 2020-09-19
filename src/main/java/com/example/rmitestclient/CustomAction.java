package com.example.rmitestclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.web.client.RestTemplate;

public class CustomAction implements Action {

    private String url;

    @Autowired
    private RestTemplate restTemplate;

    public CustomAction(String url) {
        this.url = url;
    }

    @Override
    public void execute(StateContext stateContext) {
        // use restTemplate to call remote action, since the action should be owned by domain service
//        Object object = restTemplate.postForObject(this.url, stateContext);
    }
}
