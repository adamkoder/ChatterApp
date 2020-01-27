package com.example.publicchat.Models;

public class CurrentUserModel extends UserModel {

    private static UserModel singleInstance = null;

    private CurrentUserModel(){}

    private CurrentUserModel(String username){
        this.username = username;
    }

    public static UserModel getInstance(){
        if(singleInstance == null){
            singleInstance = new CurrentUserModel();
        }
        return singleInstance;
    }

    public static void setInstance(String username, String UID){
        singleInstance = new UserModel(username, UID);
    }

        public static void clearUser(){
        singleInstance = null;
    }
}