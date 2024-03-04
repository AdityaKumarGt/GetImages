package com.example.getimages.models;

import java.util.ArrayList;

public class SearchModel {
    public ArrayList<ImageModel> results;

    public ArrayList<ImageModel> getResults() {
        return results;
    }

    public void setResults(ArrayList<ImageModel> results) {
        this.results = results;
    }

    public SearchModel(ArrayList<ImageModel> results) {
        this.results = results;
    }
}