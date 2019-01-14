package com.example.publicchat;

public class User {

    private static User singleInstance = null;

    private static String username;


    private User(String user) {
        username = user;
    }

    public static User getInstance(String username){
        if(singleInstance == null)
            singleInstance = new User(username);

        return singleInstance;
    }

    public static void clearUser(){
        singleInstance = null;
    }

    public static String getUsername() {
        return username;
    }


}
