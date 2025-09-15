package com.example.javafx_backend_test;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML private Button practiceLink;

    @FXML private Button signOutButton;




    @FXML
    private void handleSignOut() throws IOException {
        System.out.println("Signing out");

        // TODO: Add sign out logic

        LoadScene loadScene = new LoadScene(Main.getPrimaryStage());
        loadScene.render("sign-in-scene.fxml", "sign-in-styles.css");


    }


    @FXML
    private void handlePracticeClick() throws IOException {


        LoadScene loadScene = new LoadScene(Main.getPrimaryStage());
        loadScene.render("practice-scene.fxml", "practice-styles.css");

    }

}
