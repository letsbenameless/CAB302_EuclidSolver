package com.CAB302_EuclidSolver.model.user;

public interface IUserSession {
    void login(String username);
    void logout();
    String getUsername();
    boolean isLoggedIn();
}
