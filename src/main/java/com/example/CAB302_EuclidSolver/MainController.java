package com.example.CAB302_EuclidSolver;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class MainController {



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
