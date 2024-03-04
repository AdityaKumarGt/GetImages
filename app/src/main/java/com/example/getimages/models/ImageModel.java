package com.example.getimages.models;

public class ImageModel {
    private UrlModel urls;
    private String alt_description;

    public ImageModel(UrlModel urls) {
        this.urls = urls;
    }

    public UrlModel getUrls() {
        return urls;
    }

    public void setUrls(UrlModel urls) {
        this.urls = urls;
    }





    public ImageModel(String alt_description) {
        this.alt_description = alt_description;
    }

    public String getAlt_description() {
        return alt_description;
    }

    public void setAlt_description(String alt_description) {
        this.alt_description = alt_description;
    }

}
