package com.example.publicchat;

public class User {
    private String ID;
    private String username;
    private String password;
    private String email;

    public User() {
    }

    public User(String ID, String username, String password) {
        this.ID = ID;
        this.username = username;
        this.password = password;
//        this.email = email;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
