package com.example.getimages.models;

public class UrlModel {
    private String raw;
    private String regular;


    public UrlModel(String raw, String regular) {

        this.raw = raw;
        this.regular = regular;
    }

    //for fetching the raw images
    public String getRaw() {
        return raw;
    }
    public void setRaw(String regular) {
        this.raw = raw;
    }


    //for fetching the regular images
    public String getRegular() {
        return regular;
    }
    public void setRegular(String regular) {
        this.regular = regular;
    }

}
