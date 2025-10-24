import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Practice {

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Euclid Solver - Practice");

        // ==== Top Bar with Buttons ====
        Label titleLabel = new Label("Euclid Solver");
        titleLabel.setStyle("-fx-font-size: 21px; -fx-font-weight: bold;");

        // Home button
        Button homeBtn = new Button("🏠");
        homeBtn.setOnAction(e -> {
            EuclidSolverHome home = new EuclidSolverHome();
            home.start(primaryStage);
        });

        // Profile button
        Button profileBtn = new Button("👤");
        profileBtn.setOnAction(e -> {
            Profile profile = new Profile();
            profile.start(primaryStage);
        });

        // Settings button
        Button settingsBtn = new Button("⚙️");
        settingsBtn.setOnAction(e -> {
            Settings settings = new Settings();
            settings.start(primaryStage);
        });

        // Right-side icons bar
        HBox rightIcons = new HBox(10, homeBtn, profileBtn, settingsBtn);
        rightIcons.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topBar = new BorderPane();
        topBar.setLeft(titleLabel);
        topBar.setRight(rightIcons);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // ==== Timer Label ====
        Label timerLabel = new Label("5m 30s remaining");
        timerLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        timerLabel.setPadding(new Insets(5));
        timerLabel.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // ==== Question Label ====
        Label questionLabel = new Label("23×50");
        questionLabel.setStyle("-fx-font-size: 32px; -fx-font-weight: bold;");
        VBox questionBox = new VBox(questionLabel);
        questionBox.setAlignment(Pos.CENTER);
        questionBox.setPrefSize(300, 150);
        questionBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // ==== Workbook Section ====
        Label workbookLabel = new Label("Workbook");
        workbookLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        VBox workbookBox = new VBox(workbookLabel);
        workbookBox.setAlignment(Pos.TOP_LEFT);
        workbookBox.setPrefSize(600, 200);
        workbookBox.setPadding(new Insets(10));
        workbookBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // ==== Main Layout ====
        VBox mainLayout = new VBox(20, topBar, timerLabel, questionBox, workbookBox);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #b3e5fc;");

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
