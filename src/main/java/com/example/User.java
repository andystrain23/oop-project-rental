package com.example;

public class User {
    private String username;
    private boolean admin = false;

    public void login(String username, String password) {
        // check database

        this.username = username;
        // result of query
        this.admin = true;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAdmin() {
        return this.admin;
    }
}
