package com.example.javafx_backend_test;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;


public class SignInController {

    @FXML private Button signOutButton;
    @FXML private TextField regName;
    @FXML private TextField regEmail;
    @FXML private PasswordField regPassword;

    @FXML private TextField loginEmail;
    @FXML private PasswordField loginPassword;



    @FXML
    private void handleRegister() throws IOException {
        System.out.println("Registering: " + regName.getText() + ", " + regEmail.getText());

        // TODO: Add registration logic


        boolean isValid = !regName.getText().isEmpty();


        if (isValid) {

            loadMainScene();

        }
    }





    @FXML
    private void handleSignIn() throws IOException {
        System.out.println("Signing in with: " + loginEmail.getText());

        // TODO: Add login logic

        boolean isValid = !loginEmail.getText().isEmpty();


        if (isValid) {

            loadMainScene();

        }

    }

    @FXML
    private void handleSignOut() throws IOException {
        System.out.println("Signing out");

        // TODO: Add sign out logic


        try {
            Stage stage = (Stage) signOutButton.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("sign-in-scene.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1250, 750);
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @FXML
    private void handlePracticeClick() {
        System.out.println("Going into practice mode");

        // TODO: Add practice logic

    }




    private void loadMainScene() throws IOException {
        Stage stage = (Stage) loginEmail.getScene().getWindow();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1250, 750);

        stage.setScene(scene);
    }
}
