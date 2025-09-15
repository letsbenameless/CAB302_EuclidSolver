package com.example.javafx_backend_test;


import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;


public class SignInController {

    @FXML private TextField registrationUsername;
    @FXML private TextField registrationEmail;
    @FXML private PasswordField registrationPassword;
    @FXML private Label registrationError;

    @FXML private TextField loginEmail;
    @FXML private PasswordField loginPassword;
    @FXML private Label loginError;

    UserDAO userDAO = new UserDAO();


    @FXML
    private void handleRegister() throws IOException {

        // Empty Fields
        if (
            registrationUsername.getText().isEmpty() ||
            registrationEmail.getText().isEmpty() ||
            registrationPassword.getText().isEmpty()
        ) {
            registrationError.setText("Please fill all fields.");
            registrationError.getStyleClass().add("active");
            return;
        }

        // Username already exists
        System.out.println("USER: " + userDAO.getUserByUsername(registrationUsername.getText()));
        if (userDAO.getUserByUsername(registrationUsername.getText()) != null) {
            registrationError.setText("This username has already been taken.");
            registrationError.getStyleClass().add("active");
            return;
        }


        // Email already exists
        if (userDAO.getUserByEmail(registrationEmail.getText()) != null) {
            registrationError.setText("A user with this email address already exist. Try Signing In instead.");
            registrationError.getStyleClass().add("active");
            return;
        }


        // Valid Registration
        User newUser = new User(
            registrationUsername.getText(),
            registrationEmail.getText(),
            registrationPassword.getText()
        );
        userDAO.insert(newUser);

        LoadScene loadScene = new LoadScene(Main.getPrimaryStage());
        loadScene.render("main-scene.fxml", "main-styles.css");

    }




    @FXML
    private void handleSignIn() throws IOException {
        System.out.println("Signing in with: " + loginEmail.getText());
        System.out.println(userDAO.getUserByUsername("andre"));

        // Empty Fields
        if (
            loginEmail.getText().isEmpty() ||
            loginPassword.getText().isEmpty()
        ) {
            loginError.setText("Please fill all fields.");
            loginError.getStyleClass().add("active");
            return;
        }

        User userSigningIn = userDAO.getUserByEmail(loginEmail.getText());

        if (userSigningIn == null) {
            loginError.setText("Invalid Email or Password.");
            loginError.getStyleClass().add("active");
            return;
        }

        if (
            !userSigningIn.getEmail().equals(loginEmail.getText()) ||
            !userSigningIn.getPassword().equals(loginPassword.getText())
        ) {
            loginError.setText("Invalid Email or Password.");
            loginError.getStyleClass().add("active");
            return;
        }


        // Valid Sign In
        LoadScene loadScene = new LoadScene(Main.getPrimaryStage());
        loadScene.render("main-scene.fxml", "main-styles.css");

    }

}
