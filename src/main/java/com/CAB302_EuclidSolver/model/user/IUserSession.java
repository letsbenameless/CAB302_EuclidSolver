package com.CAB302_EuclidSolver.model.user;

public interface IUserSession {
    void logout();
    String getUsername();
    boolean isLoggedIn();
}
