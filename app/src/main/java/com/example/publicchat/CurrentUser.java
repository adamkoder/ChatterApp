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

    public static void setInstance(String username){
        singleInstance = new User(username);
    }

        public static void clearUser(){
        singleInstance = null;
    }
}