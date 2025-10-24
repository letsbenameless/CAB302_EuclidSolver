package com.CAB302_EuclidSolver.util;

import com.CAB302_EuclidSolver.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * LoadScene is a SINGLETON responsible for rendering new scenes into the Main stage and their respective styles
 */

public class LoadScene {

    // LoadScene SINGLETON with 'volatile' for thread-safe
    private static volatile LoadScene instance;

    // The current app active stage - should be mainly the primary stage
    private Stage currentStage;

    // Private constructor to comply with SINGLETON behaviour
    protected LoadScene() {
        currentStage = Main.getPrimaryStage();
    }




    // SINGLETON instance getter
    public static LoadScene getInstance() {
        if (instance == null) {
            synchronized (LoadScene.class) {
                if (instance == null) {
                    instance = new LoadScene();
                }
            }
        }
        return instance;
    }


    // SINGLETON stage setter
    public void setStage(Stage stage) {
        this.currentStage = stage;
    }


    // Main render method
    public void render(String sceneFXML, String sceneStyles) throws IOException {
        if (currentStage == null) {
            throw new IllegalStateException("Stage has not been set in LoadScene!");
        }

        String globalStylesheet = Main.class.getResource("styles/globals.css").toExternalForm();
        String stylesheet = Main.class.getResource(sceneStyles).toExternalForm();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(sceneFXML));
        Scene scene = new Scene(fxmlLoader.load(), 1250, 750);

        scene.getStylesheets().add(globalStylesheet);
        scene.getStylesheets().add(stylesheet);


        currentStage.setScene(scene);
        currentStage.show();
    }
}
