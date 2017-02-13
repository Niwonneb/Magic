package com.ngw.seed.model;

public class Thought {
    private String text;
    private String id;

    public Thought(String id, String text) {
        this.text = text;
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }
}
