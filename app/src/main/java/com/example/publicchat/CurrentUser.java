package com.example.publicchat;

public class CurrentUser extends User {

    private static User singleInstance = null;

    private CurrentUser(){}

    private CurrentUser(String username){
        this.username = username;
    }

    public static User getInstance(){
        if(singleInstance == null){
            singleInstance = new CurrentUser();
        }
        return singleInstance;
    }

    public static void setInstance(String username, String UID){
        singleInstance = new User(username, UID);
    }

        public static void clearUser(){
        singleInstance = null;
    }
}