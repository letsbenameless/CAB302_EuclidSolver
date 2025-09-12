import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Profile {

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Euclid Solver - Profile");

        // ==== Top Bar ====
        Button homeBtn = new Button("🏠");
        homeBtn.setOnAction(e -> {
            EuclidSolverHome home = new EuclidSolverHome();
            home.start(primaryStage);
        });

        Button profileBtn = new Button("👤"); // current page
        Button settingsBtn = new Button("⚙️");
        settingsBtn.setOnAction(e -> {
            Settings settings = new Settings();
            settings.start(primaryStage);
        });

        HBox rightIcons = new HBox(10, homeBtn, profileBtn, settingsBtn);
        rightIcons.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topBar = new BorderPane();
        topBar.setLeft(new Label("Euclid Solver"));
        topBar.setRight(rightIcons);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // ==== Title ====
        Label profileTitle = new Label("Profile");
        profileTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // ==== Achievements ====
        Label achievementsLabel = new Label("Achievements");
        achievementsLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox achievement1 = new VBox();
        achievement1.setPrefSize(200, 100);
        achievement1.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        VBox achievement2 = new VBox();
        achievement2.setPrefSize(200, 100);
        achievement2.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        VBox achievementsBox = new VBox(20, achievementsLabel, achievement1, achievement2);
        achievementsBox.setAlignment(Pos.TOP_LEFT);

        // ==== Profile Image (Yellow Circle) ====
        Circle profileCircle = new Circle(100, Color.YELLOW); // radius 100px

        // ==== Skill Level Box ====
        Label skillLabel = new Label("Skill level");
        skillLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        VBox skillBox = new VBox(skillLabel);
        skillBox.setAlignment(Pos.CENTER);
        skillBox.setPrefSize(200, 50);
        skillBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        VBox profileRight = new VBox(20, profileCircle, skillBox);
        profileRight.setAlignment(Pos.CENTER);

        // ==== Content Layout ====
        HBox content = new HBox(50, achievementsBox, profileRight);
        content.setAlignment(Pos.CENTER);

        // ==== Main Layout ====
        VBox mainLayout = new VBox(20, topBar, profileTitle, content);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #b3e5fc;");

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
