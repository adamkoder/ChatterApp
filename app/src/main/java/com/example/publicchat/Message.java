package com.example.publicchat;

public class Message {
    private String text;
    private User user;
    private int time;

    public Message(String text, User user, int time) {
        this.text = text;
        this.user = user;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
