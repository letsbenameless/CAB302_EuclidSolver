package com.example.javafx_backend_test;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {

        String stylesheet = Main.class.getResource("stylesheet.css").toExternalForm();

        //quizGenerator.main(args);

        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("sign-in-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1250, 750);
        scene.getStylesheets().add(stylesheet);

        primaryStage.setTitle("EuclidSolver");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("images/favicon.png")));

        primaryStage.setScene(scene);
        primaryStage.show();

    }
}
