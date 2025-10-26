package com.CAB302_EuclidSolver.controller;

import com.CAB302_EuclidSolver.Main;
import com.CAB302_EuclidSolver.model.database.UserDAO;
import com.CAB302_EuclidSolver.model.user.User;
import com.CAB302_EuclidSolver.model.user.UserSession;
import com.CAB302_EuclidSolver.util.LoadScene;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField registrationUsername;
    @FXML private TextField registrationEmail;
    @FXML private PasswordField registrationPassword;
    @FXML private Label registrationError;

    UserDAO userDAO = new UserDAO();

    @FXML
    private void handleSignInScreen() throws IOException {
        LoadScene.getInstance().render("scenes/signin/signin-scene.fxml", "scenes/signin/signin-styles.css");
    }


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
        if (userDAO.getUserByUsername(registrationUsername.getText()).isPresent()) {
            registrationError.setText("This username has already been taken.");
            registrationError.getStyleClass().add("active");
            return;
        }


        // Email already exists
        if (userDAO.getUserByEmail(registrationEmail.getText()).isPresent()) {
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


        LoadScene.getInstance().render("scenes/main/main-scene.fxml", "scenes/main/main-styles.css");
        UserSession.getInstance().login(newUser.getUsername());

    }


}
