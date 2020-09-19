package com.example.rmitestclient;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;

@Configuration
public class RemoteAction1 implements Action {
    @Override
    public void execute(StateContext stateContext) {
        System.out.println("Remote action: " + stateContext.getTarget().getId());
    }
}
