package com.example.javafx_backend_test;

public class User {
    private int userID;
    private String username;
    private String email;
    private String password;
    // ^ creates the fields that the users need to add to their account

    public User(int userID, String username, String email, String password) {

        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        this.userID = userID;
        this.username = username;
        this.email = email;
        this.password = password;
    }
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public int getUserID(){
        return userID;
    }
    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    public String getPassword(){
        return password;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + "'" +
                ", email='" + email + "'" +
                ", password=" + password + "'" +
                '}';
    }

}