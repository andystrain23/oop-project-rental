package com.example;

import java.sql.Date;

public class User {
    private String username;
    private boolean admin = false;
    private Date creationDate;

    public void login(String username, int permissions, Date creationDate) {
        this.username = username;
        // result of query
        if (permissions == 1) {
            this.admin = true;
        }
        this.creationDate = creationDate;
    }

    public String getUsername() { return this.username; }

    public boolean isAdmin() { return this.admin; }

    public Date getCreationDate() { return this.creationDate; }
}
