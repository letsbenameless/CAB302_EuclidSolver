package com.example.javafx_backend_test;

import javafx.application.Application;

import java.sql.Connection;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;


public class Main extends Application {

    // Static reference to the primary stage to be used on internal navigation
    private static Stage primaryStage;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start (Stage primaryStage) throws Exception {

        // Set Static Primary Stage
        Main.primaryStage = primaryStage;


        // Establish Database connection
        Connection connection = DatabaseConnection.getInstance();
        UserDAO userDAO = new UserDAO();

        // Create initial database tables
        userDAO.createUserTable();


        // Define initial stylesheet for the first Scene
        String stylesheet = Main.class.getResource("sign-in-styles.css").toExternalForm();



        // Define Primary Stage Branding
        primaryStage.setTitle("EuclidSolver");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("images/favicon.png")));


        // Set up initial Scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("sign-in-scene.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1250, 750);
        scene.getStylesheets().add(stylesheet);


        // Display Sign In initial Scene
        primaryStage.setScene(scene);
        primaryStage.show();


        //quizGenerator.main(args);

    }

    public static Stage getPrimaryStage() {
        return Main.primaryStage;
    }
}
