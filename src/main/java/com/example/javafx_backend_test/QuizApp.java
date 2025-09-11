package com.example.javafx_backend_test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Simple JavaFX front end that drives the quizGenerator.
 * Displays each question's equation as an image rendered from its LaTeX URL,
 * accepts user answers, checks them, and tracks the score.
 */
public class QuizApp extends Application {

    private quizGenerator engine;
    private ImageView questionView;
    private TextField answerField;
    private Label scoreLabel;
    private Button checkButton;

    @Override
    public void start(Stage stage) {
        engine = new quizGenerator(10);
        questionView = new ImageView();
        answerField = new TextField();
        checkButton = new Button("Check Answer");
        scoreLabel = new Label("Score: 0/0");

        checkButton.setOnAction(e -> handleCheck());

        VBox root = new VBox(15, questionView, answerField, checkButton, scoreLabel);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));

        stage.setTitle("Algebra Quiz");
        stage.setScene(new Scene(root, 500, 400));
        stage.show();

        loadNextQuestion();
    }

    private void handleCheck() {
        var q = engine.getCurrentQuestion();
        if (q == null) {
            return;
        }
        engine.submitAnswer(answerField.getText());
        scoreLabel.setText("Score: " + engine.getScoreKeeper().getCorrect() + "/" + engine.getScoreKeeper().getAttempted());
        if (engine.hasNextQuestion()) {
            loadNextQuestion();
            answerField.clear();
        } else {
            checkButton.setDisable(true);
            answerField.setDisable(true);
        }
    }

    private void loadNextQuestion() {
        var q = engine.nextQuestion();
        String url = buildLatexUrl(q.equationLatex);
        questionView.setImage(new Image(url, true));
    }

    private static String buildLatexUrl(String latex) {
        String fullLatex = "\\dpi{200}\\bg{white} \\displaystyle " + latex;
        String encoded = URLEncoder.encode(fullLatex, StandardCharsets.UTF_8).replace("+", "%20");
        return "https://latex.codecogs.com/png.latex?" + encoded;
    }

    public static void main(String[] args) {
        launch();
    }
}

