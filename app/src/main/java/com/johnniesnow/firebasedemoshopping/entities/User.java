package com.johnniesnow.firebasedemoshopping.entities;

import java.util.HashMap;

/**
 * Created by Jonathan on 13/06/17.
 */

public class User {
    private String email;
    private String name;
    private HashMap<String, Object> dateJoined;
    private boolean hasLoggedInWithPassword;

    public User() {
    }

    public User(String email, String name, HashMap<String, Object> dateJoined, boolean hasLoggedInWithPassword) {
        this.email = email;
        this.name = name;
        this.dateJoined = dateJoined;
        this.hasLoggedInWithPassword = hasLoggedInWithPassword;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public HashMap<String, Object> getDateJoined() {
        return dateJoined;
    }

    public boolean isHasLoggedInWithPassword() {
        return hasLoggedInWithPassword;
    }
}




