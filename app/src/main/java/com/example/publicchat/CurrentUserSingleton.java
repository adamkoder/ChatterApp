package com.example.publicchat;

public class CurrentUserSingleton {
    public static CurrentUserSingleton instance;

    public static void init(User user){
        if(instance == null)
            instance = new CurrentUserSingleton(user);
    }

    private static User user;

    private CurrentUserSingleton(User user){
        this.user = user;
    }

    public static User getUser() {
        return user;
    }
}
