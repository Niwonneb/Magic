package com.ngw.toowyn.model;

import com.google.gson.annotations.SerializedName;

public class Thought {

    public Thought(String text) {
        this.text = text;
    }

    @SerializedName("text")
    public String text = "";
    public String getText() {
        return text;
    }
}
