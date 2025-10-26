package com.CAB302_EuclidSolver.model.user;

import com.CAB302_EuclidSolver.model.user.SessionException;
import com.CAB302_EuclidSolver.util.LoadScene;

import java.io.IOException;

public class UserSession implements IUserSession {

    private static volatile UserSession instance; // Thread-safe Singleton
    private String username;

    // Private constructor prevents instantiation
    private UserSession() {}

    // Double-checked locking Singleton pattern (thread-safe)
    public static UserSession getInstance() {
        if (instance == null) {
            synchronized (UserSession.class) {
                if (instance == null) {
                    instance = new UserSession();
                }
            }
        }
        return instance;
    }


    @Override
    public void login(String username) {
        if (isLoggedIn()) {
            throw new SessionException("A user is already logged in. Please logout first.");
        }
        if (username == null || username.trim().isEmpty()) {
            throw new SessionException("Username cannot be null or empty.");
        }
        this.username = username;
    }


    @Override
    public void logout() {
        if (!isLoggedIn()) {
            throw new SessionException("No user is currently logged in.");
        }
        this.username = null;

        try {
            LoadScene.getInstance().render("scenes/signin/signin-scene.fxml", "scenes/signin/signin-styles.css");
        } catch (IOException e) {
            throw new SessionException("Failed to load sign-in scene during logout.", e);
        }
    }

    @Override
    public String getUsername() {
        if (!isLoggedIn()) {
            throw new SessionException("No active user session found.");
        }
        return username;
    }

    @Override
    public boolean isLoggedIn() {
        return username != null;
    }


    /** TESTING ONLY. Reset instance for testing */
    public void TESTreset() {
        this.username = null;
    }
    /** TESTING ONLY. Logs out without rendering scene */
    public void TESTlogout() {
        this.username = null;
    }

    /** TESTING ONLY. Logs in wihthout rendering scene */
    public void TESTlogin(String username) {
        this.username = username;
    }
}
