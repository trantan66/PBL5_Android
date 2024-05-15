package com.midterm.firebasetest;

public class SearchModel {
    String alternativename;
    String name;
    String image;

    public SearchModel(String alternativename, String name, String image) {
        this.alternativename = alternativename;
        this.name = name;
        this.image = image;
    }

    public SearchModel() {
    }

    public String getAlternativename() {
        return alternativename;
    }

    public void setAlternativename(String alternativename) {
        this.alternativename = alternativename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
