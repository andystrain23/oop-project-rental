package com.example;

import java.sql.*;
import java.util.List;

public class User {
    private String username;
    private String address;
    private String phone;
    private String email;
    private boolean admin = false;
    private Date creationDate;

    public boolean login(String[] dbDetails, String username, String password) {
        Connection connection = null;
        Statement statement;
        try {
            connection = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            System.out.println("Connected!");
            String query = String.format("SELECT * FROM accounts WHERE username = '%s'", username);
            statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            // VERY BAD
            // if resultSet is not empty
            if (resultSet.next()) {
                String dbPass = resultSet.getString("PASSWORD");
                // if supplied password not the same as stored password, reject login
                if (!dbPass.equals(password)) return false;
                // if account not yet approved
                if (resultSet.getInt("APPROVED") == 2) return false;

                this.username = resultSet.getString("USERNAME");
                this.address = resultSet.getString("ADDRESS");
                this.phone = resultSet.getString("PHONE");
                this.email = resultSet.getString("EMAIL");
                this.creationDate = resultSet.getDate("CREATED_DATE");

                if (resultSet.getInt("PERMISSIONS") == 1) {
                    this.admin = true;
                }
            } else {
                System.out.println("User not found, returning");
                return false;
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqlException) {
                    System.out.println(sqlException);
                }
            }
        }
        return true;
    }

    public boolean registerUser(List<String> details, String[] dbDetails) {
        // connect to database, add new user
        Connection connection = null;
        Statement statement;

        try {
            connection = DriverManager.getConnection(dbDetails[0], dbDetails[1], dbDetails[2]);
            String query = String.format("INSERT INTO accounts VALUES (null, \"%s\", \"%s\", null, null, null, 2, curdate(), 2);", details.get(0), details.get(1));
            statement = connection.createStatement();
            statement.executeUpdate(query);
            System.out.println("User added");
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println(e);
                }
            }
        }
        return true;
    }

    public String getUsername() { return this.username; }

    public String getAddress() { return address; }

    public String getEmail() { return email; }

    public String getPhone() { return phone; }

    public boolean isAdmin() { return this.admin; }

    public Date getCreationDate() { return this.creationDate; }
}
