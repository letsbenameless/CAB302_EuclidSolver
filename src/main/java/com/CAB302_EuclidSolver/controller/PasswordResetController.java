package com.CAB302_EuclidSolver.controller;

import com.CAB302_EuclidSolver.model.database.UserDAO;
import com.CAB302_EuclidSolver.model.user.User;
import com.CAB302_EuclidSolver.model.user.UserSession;
import com.CAB302_EuclidSolver.util.LoadScene;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.Optional;

public class PasswordResetController {



    @FXML private TextField forgotPasswordUsername;
    @FXML private TextField forgotPasswordEmail;
    @FXML private Label forgotPasswordError;
    @FXML private TextField forgotPasswordNewPass;
    @FXML private Button forgotPasswordReset;

    UserDAO userDAO = new UserDAO();


    /**
     * Handles the return to the sign in scene in case the user gives up on resetting the password
     * @throws IOException
     */
    @FXML
    public void handleReturnToSignIn() throws IOException {
        LoadScene.getInstance().render("scenes/signin/signin-scene.fxml", "scenes/signin/signin-styles.css");
    }


    /**
     * Handle the reset password initial validation where the username is checked against the user email
     * If both values coincide, the user is presented with a field to reset their password
     */
    @FXML
    public void handleResetPassword() {

        Optional<User> userByEmail = userDAO.getUserByEmail(forgotPasswordEmail.getText());
        Optional<User> userByUsername = userDAO.getUserByUsername(forgotPasswordUsername.getText());

        if (
                userByEmail.isPresent() &&
                userByUsername.isPresent() &&
                userByEmail.toString().equals(userByUsername.toString())
        ) {
            forgotPasswordNewPass.getStyleClass().add("active");
            forgotPasswordReset.getStyleClass().add("active");

            forgotPasswordError.getStyleClass().remove("active");
        }
        else {

            forgotPasswordNewPass.getStyleClass().remove("active");
            forgotPasswordReset.getStyleClass().remove("active");

            forgotPasswordError.setText("Invalid Username or Email address. Please try again.");
            forgotPasswordError.getStyleClass().add("active");

        }


    }


    /**
     * Checks if the user email and new password fields are valid before resetting the user password
     * @throws IOException
     */
    @FXML
    public void handleSetNewPassword() throws IOException {

        if (
            !forgotPasswordEmail.getText().isEmpty() &&
            !forgotPasswordNewPass.getText().isEmpty()
        ) {

            Optional<User> optionalUser = userDAO.getUserByEmail(forgotPasswordEmail.getText());

            if (optionalUser.isPresent()) {

                User user = optionalUser.get();

                // Resets user password and loads the main scene
                user.setPassword(forgotPasswordNewPass.getText());
                userDAO.updateUser(user);

                LoadScene.getInstance().render("scenes/main/main-scene.fxml", "scenes/main/main-styles.css");
                UserSession.getInstance().login(user.getUsername());

            }

        }

    }

}
