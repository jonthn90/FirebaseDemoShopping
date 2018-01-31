package com.johnniesnow.firebasedemoshopping.entities;


import java.util.HashMap;

public class Users {
    private HashMap<String,User> usersFriends;

    public Users() {
    }

    public HashMap<String, User> getUsersFriends() {
        return usersFriends;
    }
}
