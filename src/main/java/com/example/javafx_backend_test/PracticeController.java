package com.example.javafx_backend_test;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PracticeController {



    @FXML
    private void handleExitQuiz() {

    }


    @FXML
    private void handleCheckAnswer() {

    }


    @FXML
    private Canvas notepadCanvas;

    private GraphicsContext gc;

    @FXML
    private void initialize() {
        gc = notepadCanvas.getGraphicsContext2D();

        // Set default pen style
        gc.setLineWidth(2);
        gc.setStroke(Color.BLACK);

        // Mouse pressed → move pen
        notepadCanvas.setOnMousePressed(e -> {
            gc.beginPath();
            gc.moveTo(e.getX(), e.getY());
            gc.stroke();
        });

        // Mouse dragged → draw
        notepadCanvas.setOnMouseDragged(e -> {
            gc.lineTo(e.getX(), e.getY());
            gc.stroke();
        });

        // Mouse released → finish stroke
        notepadCanvas.setOnMouseReleased(e -> {
            gc.closePath();
        });
    }


}
