package com.midterm.firebasetest;

public class HistoryModel {
    String  alternativename, name, image, sciencename, family, partused, timestamp, username, uses;

    public HistoryModel(String alternativename, String name, String image, String sciencename, String family, String partused, String timestamp, String username, String uses) {
        this.alternativename = alternativename;
        this.name = name;
        this.image = image;
        this.sciencename = sciencename;
        this.family = family;
        this.partused = partused;
        this.timestamp = timestamp;
        this.username = username;
        this.uses = uses;
    }

    public HistoryModel() {
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

    public String getSciencename() {
        return sciencename;
    }

    public void setSciencename(String sciencename) {
        this.sciencename = sciencename;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getPartused() {
        return partused;
    }

    public void setPartused(String partused) {
        this.partused = partused;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUses() {
        return uses;
    }

    public void setUses(String uses) {
        this.uses = uses;
    }
}
