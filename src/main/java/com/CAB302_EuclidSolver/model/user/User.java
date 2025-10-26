package com.CAB302_EuclidSolver.model.user;

import com.CAB302_EuclidSolver.model.database.UserDAO;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class User {
    private int userID;
    private String username;
    private String email;
    private String password;


    private int userXP;
    private int totalQuestionsAnswered;
    private int totalHardQuestionsAnswered;
    private int totalClockQuestionsAnswered;



    public User(
        int userID,
        String username,
        String email,
        String password,
        int userXP,
        int totalQuestions,
        int totalHardQuestions,
        int totalClockQuestions
    ) {

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
        this.userXP = userXP;
        this.totalQuestionsAnswered = totalQuestions;
        this.totalHardQuestionsAnswered = totalHardQuestions;
        this.totalClockQuestionsAnswered = totalClockQuestions;
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


    public int getUserXP() {
        return this.userXP;
    }
    public int getTotalQuestionsAnswered() {
        return this.totalQuestionsAnswered;
    }
    public int getTotalHardQuestionsAnswered() {
        return this.totalHardQuestionsAnswered;
    }
    public int getTotalClockQuestionsAnswered() {
        return this.totalClockQuestionsAnswered;
    }

    public void setUserXP(int XP) {
        this.userXP = XP;

    }
    public void setTotalQuestionsAnswered(int newTotal) {
        this.totalQuestionsAnswered = newTotal;
    }
    public void setTotalHardQuestionsAnswered(int newTotal) {
        this.totalHardQuestionsAnswered = newTotal;
    }
    public void setTotalClockQuestionsAnswered(int newTotal) {
        this.totalClockQuestionsAnswered = newTotal;
    }

}