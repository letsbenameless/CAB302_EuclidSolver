package com.CAB302_EuclidSolver;

import com.CAB302_EuclidSolver.model.database.DatabaseConnection;
import com.CAB302_EuclidSolver.model.database.UserDAO;
import com.CAB302_EuclidSolver.util.LoadScene;
import javafx.application.Application;

import javafx.scene.image.Image;
import javafx.stage.Stage;


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
        DatabaseConnection.getInstance();
        UserDAO userDAO = new UserDAO();


        // Create initial database tables
        userDAO.createUserTable();



        // Define Primary Stage Branding
        primaryStage.setTitle("EuclidSolver");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("images/favicon.png")));


        // Set up initial scene and global styles
        LoadScene.getInstance().render("scenes/signin/signin-scene.fxml", "scenes/signin/signin-styles.css");

    }


    /**
     * This is a method consumed within the LoadScene SINGLETON
     * Should always refer back to the primary stage for scene rendering
     */
    public static Stage getPrimaryStage() {
        return Main.primaryStage;
    }
}
