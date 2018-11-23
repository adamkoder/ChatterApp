package com.example.publicchat;

public class ChatBubbleInfo {
    private String username = "132";
    private String message = "123";

    public ChatBubbleInfo(String username, String message) {
        this.username = username;
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
