package com.example.publicchat.Models;

public class UserModel {

    protected String username;
    protected String UID;

    public UserModel(){}

    public UserModel(String username, String UID) {
        this.username = username;
        this.UID = UID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
