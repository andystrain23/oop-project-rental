package com.example;

public class User {
    private String username;
    private boolean admin = false;

    public void login(String username, int permissions) {
        this.username = username;
        // result of query
        if (permissions == 1) {
            this.admin = true;
        }
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAdmin() {
        return this.admin;
    }
}
