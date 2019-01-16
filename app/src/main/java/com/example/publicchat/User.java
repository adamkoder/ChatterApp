package com.example.publicchat;

public class User {

    protected String username;
    protected String UID;

    public User(){}

    public User(String username, String UID) {
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
