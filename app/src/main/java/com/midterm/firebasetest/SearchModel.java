package com.midterm.firebasetest;

public class SearchModel {
    String username;
    String name;
    String image;

    public SearchModel(String username, String name, String image) {
        this.username = username;
        this.name = name;
        this.image = image;
    }

    public SearchModel() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
