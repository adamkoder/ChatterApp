package com.example.publicchat.Models;

import java.util.Date;

public class MessageModel {
    private String text;
    private String username;
    private Date time;

    public MessageModel() {
    }

    public MessageModel(String text, String username, Date time) {
        this.text = text;
        this.username = username;
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return username;
    }

    public void setUser(String username) {
        this.username = username;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}