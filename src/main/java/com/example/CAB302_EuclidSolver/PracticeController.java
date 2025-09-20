package com.example.CAB302_EuclidSolver;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class PracticeController {


    private quizGenerator engine;
    private Image[] imageCache;


    @FXML private Label scoreLabel;
    @FXML private TextField answerField;
    @FXML private Button checkButton;
    @FXML private ImageView questionView;

    @FXML private Label answerLabel;



    @FXML
    private void handleExitQuiz() throws IOException {
        LoadScene loadScene = new LoadScene(Main.getPrimaryStage());
        loadScene.render("main-scene.fxml", "main-styles.css");
    }


    @FXML
    private void handleCheckAnswer() {
        System.out.println("Checking Answer");

        System.out.println(engine);

        var q = engine.getCurrentQuestion();
        System.out.println(q);

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
        /*plainLabel.setText("Plain: " + q.rearrangedRaw);
        latexLabel.setText("LaTeX: " + q.equationLatex);*/
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

    private static String buildLatexUrl(String latex) {
        String fullLatex = "\\dpi{200}\\bg{white} \\displaystyle " + latex;
        String encoded = URLEncoder.encode(fullLatex, StandardCharsets.UTF_8).replace("+", "%20");
        return "https://latex.codecogs.com/png.latex?" + encoded;
    }




    /* NOTEBOOK DRAWING FUNCTION */


    @FXML private Canvas notepadCanvas;

    @FXML private Button undoButton;
    @FXML private Button redoButton;
    @FXML private Button clearButton;

    private GraphicsContext gc;


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


    @FXML
    private void initialize() {

        engine = new quizGenerator(30);
        imageCache = new Image[30];

        loadNextQuestion();

        gc = notepadCanvas.getGraphicsContext2D();

        // Set default pen style
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);

        //  Move pen when Mouse pressed
        notepadCanvas.setOnMousePressed(e -> {
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });

        // Draw when Mouse dragged
        notepadCanvas.setOnMouseDragged(e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        // Finish stroke when Mouse released
        notepadCanvas.setOnMouseReleased(e -> {
            gc.closePath();
        });


        // Mouse events
        notepadCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {
            currentStroke = new Stroke(3); // fixed line width
            currentStroke.addPoint(e.getX(), e.getY());
            lastX = e.getX();
            lastY = e.getY();
        });

        notepadCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, e -> {
            double x = e.getX(), y = e.getY();
            gc.setStroke(Color.BLACK); // fixed color
            gc.setLineWidth(currentStroke.lineWidth);
            gc.strokeLine(lastX, lastY, x, y);
            currentStroke.addPoint(x, y);
            lastX = x; lastY = y;
        });

        notepadCanvas.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {
            undoStack.push(currentStroke);
            redoStack.clear();
            currentStroke = null;
        });

        undoButton.setOnAction(e -> {
            if (!undoStack.isEmpty()) {
                redoStack.push(undoStack.pop());
                redrawAll(gc, notepadCanvas);
            }
        });

        redoButton.setOnAction(e -> {
            if (!redoStack.isEmpty()) {
                undoStack.push(redoStack.pop());
                redrawAll(gc, notepadCanvas);
            }
        });

        clearButton.setOnAction(e -> {
            undoStack.clear();
            redoStack.clear();
            redrawAll(gc, notepadCanvas);
        });
    }

    private void redrawAll(GraphicsContext gc, Canvas canvas) {
        // Clear canvas
        gc.setFill(Color.web("#fffbea"));

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


}
