package com.example.CAB302_EuclidSolver;

//USER SESSION SINGLETON
public class UserSession {
    private static UserSession instance;
    private String username;

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void login(String username) {
        this.username = username;
    }

    public void logout() {
        this.username = null;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLoggedIn() {
        return username != null;
    }
}
