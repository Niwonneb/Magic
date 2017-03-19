package com.ngw.seed.model;

public class Thought {
    private String text;
    private String id;
    private boolean isQuestion;

    public Thought(String id, String text, boolean isQuestion) {
        this.text = text;
        this.id = id;
        this.isQuestion = isQuestion;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public boolean isQuestion() { return isQuestion; }
}
