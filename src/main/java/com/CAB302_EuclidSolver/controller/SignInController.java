package com.CAB302_EuclidSolver.controller;


import com.CAB302_EuclidSolver.model.database.UserDAO;
import com.CAB302_EuclidSolver.model.user.User;
import com.CAB302_EuclidSolver.util.LoadScene;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.Optional;


public class SignInController {

    @FXML private TextField loginEmail;
    @FXML private PasswordField loginPassword;
    @FXML private Label loginError;

    UserDAO userDAO = new UserDAO();


    @FXML
    private void handleRegisterScreen() throws IOException {
        LoadScene.getInstance().render("scenes/register/register-scene.fxml", "scenes/register/register-styles.css");
    }

    @FXML
    private void handleForgotPassword() throws IOException {
        LoadScene.getInstance().render("scenes/password-reset/password-reset-scene.fxml", "scenes/password-reset/password-reset-styles.css");
    }



    @FXML
    private void handleSignIn() throws IOException {
        System.out.println("Signing in with: " + loginEmail.getText());

        // Empty Fields
        if (
            loginEmail.getText().isEmpty() ||
            loginPassword.getText().isEmpty()
        ) {
            loginError.setText("Please fill all fields.");
            loginError.getStyleClass().add("active");
            return;
        }

        Optional<User> userSigningIn = userDAO.getUserByEmail(loginEmail.getText());

        if (userSigningIn.isEmpty()) {
            loginError.setText("Invalid Email or Password.");
            loginError.getStyleClass().add("active");
            return;
        }

        User user = userSigningIn.get();

        if (
            !user.getEmail().equals(loginEmail.getText()) ||
            !user.getPassword().equals(loginPassword.getText())
        ) {
            loginError.setText("Invalid Email or Password.");
            loginError.getStyleClass().add("active");
            return;
        }


        // Valid Sign In
        LoadScene.getInstance().render("scenes/main/main-scene.fxml", "scenes/main/main-styles.css");

    }

}
