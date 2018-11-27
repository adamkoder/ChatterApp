package com.example.publicchat;

public class User {
    private String ID;
    private String Username;

    public User(String ID, String username) {
        this.ID = ID;
        Username = username;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
