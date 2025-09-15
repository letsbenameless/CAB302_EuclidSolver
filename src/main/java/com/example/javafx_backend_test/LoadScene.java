package com.example.javafx_backend_test;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LoadScene {

    private Stage currentStage;

    public LoadScene() {

    }

    public LoadScene(Stage stage) {
        this.currentStage = stage;
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }


    public void render(String sceneFXML, String sceneStyles) throws IOException {

        if (currentStage == null) {
            throw new IllegalStateException("Stage has not been set in LoadScene!");
        }

        String stylesheet = Main.class.getResource(sceneStyles).toExternalForm();

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(sceneFXML));
        Scene scene = new Scene(fxmlLoader.load(), 1250, 750);
        scene.getStylesheets().add(stylesheet);

        currentStage.setScene(scene);
        currentStage.show();

    }

}
