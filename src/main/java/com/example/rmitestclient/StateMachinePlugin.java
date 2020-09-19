package com.example.rmitestclient;

import org.springframework.statemachine.config.StateMachineBuilder;

public interface StateMachinePlugin {

    public byte[] configure(byte[] data) throws Exception;
}
