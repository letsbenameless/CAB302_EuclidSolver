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


    @FXML
    private void handleRegister() throws IOException {

        // TODO: Add registration logic


        boolean isValid = !registrationUsername.getText().isEmpty();


        if (isValid) {

            LoadScene loadScene = new LoadScene(Main.getPrimaryStage());
            loadScene.render("main-scene.fxml", "main-styles.css");

        } else {

            registrationError.setText("Invalid Email or Password.");
            registrationError.getStyleClass().add("active");
        }
    }




    @FXML
    private void handleSignIn() throws IOException {
        System.out.println("Signing in with: " + loginEmail.getText());

        // TODO: Add login logic

        boolean isValid = !loginEmail.getText().isEmpty();


        if (isValid) {

            LoadScene loadScene = new LoadScene(Main.getPrimaryStage());
            loadScene.render("main-scene.fxml", "main-styles.css");


        } else {
            loginError.setText("Invalid Email or Password.");
            loginError.getStyleClass().add("active");
        }

    }

}
