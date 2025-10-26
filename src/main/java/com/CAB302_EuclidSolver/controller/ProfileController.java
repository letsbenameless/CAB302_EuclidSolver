package com.CAB302_EuclidSolver.controller;

import com.CAB302_EuclidSolver.model.database.UserDAO;
import com.CAB302_EuclidSolver.model.user.UserSession;
import com.CAB302_EuclidSolver.util.LoadScene;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import com.CAB302_EuclidSolver.model.user.User;

import java.io.IOException;
import java.util.Optional;

public class ProfileController {

    @FXML private Label usernameLabel;
    @FXML private Label emailLabel;
    @FXML private Label xpLabel;

    @FXML private Label totalQuestionsLabel;
    @FXML private Label hardModeLabel;
    @FXML private Label clockModeLabel;

    @FXML private Circle totalQuestionsCircle;
    @FXML private Circle hardModeCircle;
    @FXML private Circle clockModeCircle;

    private User activeUser;

    /**
     * Initialise method called automatically when the FXML is loaded.
     */
    @FXML
    public void initialize() {

        UserDAO userDAO = new UserDAO();

        Optional<User> activeUser = userDAO.getUserByUsername(UserSession.getInstance().getUsername());

        activeUser.ifPresent(user -> {
            this.activeUser = user;
        });

        loadUserData();
    }


    /**
     * Populates the labels on the profile page with user information.
     */
    private void loadUserData() {
        if (activeUser != null) {
            usernameLabel.setText(activeUser.getUsername());
            emailLabel.setText(activeUser.getEmail());
            xpLabel.setText("Total XP: " + activeUser.getUserXP());

            totalQuestionsLabel.setText(String.valueOf(activeUser.getTotalQuestionsAnswered()));
            hardModeLabel.setText(String.valueOf(activeUser.getTotalHardQuestionsAnswered()));
            clockModeLabel.setText(String.valueOf(activeUser.getTotalClockQuestionsAnswered()));
        }
    }

    @FXML
    private void handleMainPage() throws IOException {
        LoadScene.getInstance().render("scenes/main/main-scene.fxml", "scenes/main/main-styles.css");
    }

    @FXML
    private void handleSignOut() throws IOException {
        System.out.println("Signing out");

        LoadScene.getInstance().render("scenes/signin/signin-scene.fxml", "scenes/signin/signin-styles.css");
        UserSession.getInstance().logout();

    }
}
