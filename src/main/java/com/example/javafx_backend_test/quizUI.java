package com.example.javafx_backend_test;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Simple JavaFX front end that drives the quizGenerator.
 * Displays each question's equation as an image rendered from its LaTeX URL,
 * accepts user answers, checks them, and tracks the score.
 */
public class quizUI extends Application {

    private quizGenerator engine;
    private ImageView questionView;
    private TextField answerField;
    private Label scoreLabel;
    private Button checkButton;
    private Button debugButton;
    private VBox debugBox;
    private Label plainLabel;
    private Label latexLabel;
    private Label answerLabel;
    private Button copyPlainBtn;
    private Button copyLatexBtn;
    private Button copyAnswerBtn;
    private Button copyAllBtn;
    private Image[] imageCache;

    // Notebook drawing
    static class Stroke {
        double lineWidth;
        List<Point2D> points = new ArrayList<>();

        Stroke(double lineWidth) {
            this.lineWidth = lineWidth;
        }

        void addPoint(double x, double y) {
            points.add(new Point2D(x, y));
        }
    }

    private Stack<Stroke> undoStack = new Stack<>();
    private Stack<Stroke> redoStack = new Stack<>();
    private Stroke currentStroke;
    private double lastX, lastY;

    @Override
    public void start(Stage stage) {
        engine = new quizGenerator(30);

        imageCache = new Image[engine.getTotalQuestions()];

        questionView = new ImageView();
        answerField = new TextField();
        checkButton = new Button("Check Answer");
        scoreLabel = new Label("Score: 0/0");
        debugButton = new Button("Toggle Debug");

        plainLabel = new Label();
        latexLabel = new Label();
        answerLabel = new Label();

        copyPlainBtn = new Button("Copy");
        copyLatexBtn = new Button("Copy");
        copyAnswerBtn = new Button("Copy");
        copyAllBtn = new Button("Copy All");

        HBox plainRow = new HBox(5, plainLabel, copyPlainBtn);
        HBox latexRow = new HBox(5, latexLabel, copyLatexBtn);
        HBox answerRow = new HBox(5, answerLabel, copyAnswerBtn);

        debugBox = new VBox(5, plainRow, latexRow, answerRow, copyAllBtn);
        debugBox.setVisible(false);
        debugBox.managedProperty().bind(debugBox.visibleProperty());

        checkButton.setOnAction(e -> handleCheck());
        debugButton.setOnAction(e -> debugBox.setVisible(!debugBox.isVisible()));
        copyPlainBtn.setOnAction(e -> copyToClipboard(plainLabel.getText()));
        copyLatexBtn.setOnAction(e -> copyToClipboard(latexLabel.getText()));
        copyAnswerBtn.setOnAction(e -> copyToClipboard(answerLabel.getText()));
        copyAllBtn.setOnAction(e -> copyToClipboard(plainLabel.getText() + "\n" + latexLabel.getText() + "\n" + answerLabel.getText()));

        // --- Notebook UI ---
        VBox notebookBox = new VBox(5);
        notebookBox.setPadding(new Insets(10));

        Canvas canvas = new Canvas(480, 300);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        Button undoButton = new Button("Undo");
        Button redoButton = new Button("Redo");
        Button clearButton = new Button("Clear");

        HBox controls = new HBox(10, undoButton, redoButton, clearButton);
        controls.setAlignment(Pos.CENTER);
        notebookBox.getChildren().addAll(controls, canvas);

        // Mouse events
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            currentStroke = new Stroke(3); // fixed line width
            currentStroke.addPoint(e.getX(), e.getY());
            lastX = e.getX();
            lastY = e.getY();
        });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            double x = e.getX(), y = e.getY();
            gc.setStroke(Color.BLACK); // fixed color
            gc.setLineWidth(currentStroke.lineWidth);
            gc.strokeLine(lastX, lastY, x, y);
            currentStroke.addPoint(x, y);
            lastX = x; lastY = y;
        });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            undoStack.push(currentStroke);
            redoStack.clear();
            currentStroke = null;
        });

        undoButton.setOnAction(e -> {
            if (!undoStack.isEmpty()) {
                redoStack.push(undoStack.pop());
                redrawAll(gc, canvas);
            }
        });

        redoButton.setOnAction(e -> {
            if (!redoStack.isEmpty()) {
                undoStack.push(redoStack.pop());
                redrawAll(gc, canvas);
            }
        });

        clearButton.setOnAction(e -> {
            undoStack.clear();
            redoStack.clear();
            redrawAll(gc, canvas);
        });

        // Wrap notebook in a ScrollPane
        VBox root = getVBox(notebookBox);

        stage.setTitle("Algebra Quiz");
        stage.setScene(new Scene(root, 500, 400));
        stage.show();

        // Preload the first few questions so the UI can show them instantly.
        preloadImagesAhead();
        loadNextQuestion();
    }

    private VBox getVBox(VBox notebookBox) {
        VBox root = new VBox(15, questionView, answerField, checkButton, scoreLabel, debugButton, debugBox, notebookBox);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        return root;
    }

    private void redrawAll(GraphicsContext gc, Canvas canvas) {
        // Clear canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // Draw all strokes in undoStack
        for (Stroke s : undoStack) {
            gc.setStroke(Color.BLACK); // fixed black color
            gc.setLineWidth(s.lineWidth);
            List<Point2D> pts = s.points;
            for (int i = 1; i < pts.size(); i++) {
                gc.strokeLine(pts.get(i-1).getX(), pts.get(i-1).getY(), pts.get(i).getX(), pts.get(i).getY());
            }
        }
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
        int idx = engine.getCurrentIndex();
        if (imageCache[idx] != null) {
            questionView.setImage(imageCache[idx]);
        } else {
            questionView.setImage(new Image(buildLatexUrl(q.equationLatex)));
        }
        plainLabel.setText("Plain: " + q.rearrangedRaw);
        latexLabel.setText("LaTeX: " + q.equationLatex);
        answerLabel.setText("Answer: " + q.correctAnswer);

        // Start preloading the subsequent questions' images.
        preloadImagesAhead();
    }

    /** Preload images for the next few questions. */
    private void preloadImagesAhead() {
        int start = engine.getCurrentIndex() + 1;
        for (int i = start; i < start + 3 && i < engine.getTotalQuestions(); i++) {
            if (imageCache[i] == null) {
                var q = engine.peekQuestion(i);
                if (q != null) {
                    imageCache[i] = new Image(buildLatexUrl(q.equationLatex), true);
                }
            }
        }
    }

    private static void copyToClipboard(String text) {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);
        Clipboard.getSystemClipboard().setContent(content);
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