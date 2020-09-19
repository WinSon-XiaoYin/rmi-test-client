package com.example.rmitestclient;

public class State {

    String name;
    String entryAction;
    String exitAction;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntryAction() {
        return entryAction;
    }

    public void setEntryAction(String entryAction) {
        this.entryAction = entryAction;
    }

    public String getExitAction() {
        return exitAction;
    }

    public void setExitAction(String exitAction) {
        this.exitAction = exitAction;
    }
}
