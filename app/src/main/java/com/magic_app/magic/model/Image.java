package com.magic_app.magic.model;

import com.google.gson.annotations.SerializedName;
import com.magic_app.magic.apiservice.Api;

public class Image {

    public Image(String description, String uri) {
        this.description = description;
        small = uri;
        big = uri;
    }

    @SerializedName("small")
    public String small = "";
    public String getSmall() {
        return get(small);
    }

    @SerializedName("big")
    public String big = "";
    public String getBig() {
        return get(big);
    }

    @SerializedName("descrition")
    public String description = "";

    private String get(String size) {
        //return Api.serverUrl + size;
        return size;
    }

    public String getDescription() {
        return description;
    }
}
