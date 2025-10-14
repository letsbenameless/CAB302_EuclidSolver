package com.CAB302_EuclidSolver.controller;

import com.CAB302_EuclidSolver.Main;
import com.CAB302_EuclidSolver.util.LoadScene;
import javafx.fxml.FXML;

import java.io.IOException;

public class MainController {



    @FXML
    private void handleSignOut() throws IOException {
        System.out.println("Signing out");

        // TODO: Add sign out logic

        LoadScene loadScene = new LoadScene(Main.getPrimaryStage());
        loadScene.render("signin-scene.fxml", "signin-styles.css");


    }


    @FXML
    private void handlePracticeClick() throws IOException {


        LoadScene loadScene = new LoadScene(Main.getPrimaryStage());
        loadScene.render("practice-scene.fxml", "practice-styles.css");

    }

}
