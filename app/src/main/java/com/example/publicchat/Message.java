package com.example.publicchat;

import java.util.Date;

public class Message {
    private String text;
    private User user;
    private Date time;

    public Message(String text, User user, Date time) {
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}