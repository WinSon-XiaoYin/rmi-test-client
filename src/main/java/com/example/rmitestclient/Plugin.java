package com.example.rmitestclient;

import java.util.List;

public class Plugin {
    private String name;
    private String parent;  // represents fork type
    private List<State> sources;
    private List<State> targets;
    private String event;

    private List<String> guards;
    private List<String> actions;

    private List<Plugin> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public List<State> getSources() {
        return sources;
    }

    public void setSources(List<State> sources) {
        this.sources = sources;
    }

    public List<State> getTargets() {
        return targets;
    }

    public void setTargets(List<State> targets) {
        this.targets = targets;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public List<String> getGuards() {
        return guards;
    }

    public void setGuards(List<String> guards) {
        this.guards = guards;
    }

    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public List<Plugin> getChildren() {
        return children;
    }

    public void setChildren(List<Plugin> children) {
        this.children = children;
    }
}
