package com.CAB302_EuclidSolver.controller;

import com.CAB302_EuclidSolver.model.database.UserDAO;
import com.CAB302_EuclidSolver.model.user.User;
import com.CAB302_EuclidSolver.model.user.UserSession;
import com.CAB302_EuclidSolver.util.LoadScene;
import javafx.fxml.FXML;

import java.io.IOException;
import java.util.Optional;

public class MainController {



    @FXML
    private void handleProfilePage() throws IOException {

        LoadScene.getInstance().render("scenes/profile/profile-scene.fxml", "scenes/profile/profile-styles.css");

    }


    @FXML
    private void handleSignOut() throws IOException {
        System.out.println("Signing out");

        LoadScene.getInstance().render("scenes/signin/signin-scene.fxml", "scenes/signin/signin-styles.css");
        UserSession.getInstance().logout();

    }



    @FXML
    private void handlePracticeEasyMode() throws IOException {

        LoadScene.getInstance().render("scenes/practice/practice-scene.fxml", "scenes/practice/practice-styles.css");

    }


    @FXML
    private void handlePracticeHardMode() throws IOException {

        LoadScene.getInstance().render("scenes/practice/hard-mode-scene.fxml", "scenes/practice/practice-styles.css");

    }



    @FXML
    private void handlePracticeClockMode() throws IOException {

        LoadScene.getInstance().render("scenes/practice/clock-mode-scene.fxml", "scenes/practice/practice-styles.css");

    }


    @FXML
    private void handleLeaderboardPage() throws IOException {

        LoadScene.getInstance().render("scenes/leaderboard/leaderboard-scene.fxml", "scenes/leaderboard/leaderboard-styles.css");

    }


}
