import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class EuclidSolverHome extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Euclid Solver");

        // ==== Title Bar ====
        Label titleLabel = new Label("Euclid Solver");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Right-side icons (using placeholders for now)
        Button profileBtn = new Button("👤");
        Button settingsBtn = new Button("⚙️");
        HBox rightIcons = new HBox(10, profileBtn, settingsBtn);
        rightIcons.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topBar = new BorderPane();
        topBar.setLeft(titleLabel);
        topBar.setRight(rightIcons);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // ==== Homepage label ====
        Label homepageLabel = new Label("Homepage");
        homepageLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // ==== Feature Buttons ====
        Button practiceBtn = createFeatureButton("🎯", "Practice");
        Button friendsBtn = createFeatureButton("👥", "Friends");
        Button clockBtn = createFeatureButton("⏰", "Clock Test");
        Button notebookBtn = createFeatureButton("📒", "Notebook");

        // Practice button switches to Practice.java
        practiceBtn.setOnAction(e -> {
            Practice practice = new Practice();
            practice.start(primaryStage);
        });


        settingsBtn.setOnAction(e -> {
            Settings settings = new Settings();
            settings.start(primaryStage);
        });

        profileBtn.setOnAction(e -> {
            Profile profile = new Profile();
            profile.start(primaryStage);
        });



        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);
        grid.add(practiceBtn, 0, 0);
        grid.add(friendsBtn, 1, 0);
        grid.add(clockBtn, 0, 1);
        grid.add(notebookBtn, 1, 1);

        // ==== Main Layout ====
        VBox mainLayout = new VBox(20, topBar, homepageLabel, grid);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #b3e5fc;"); // light blue

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Helper: Create feature buttons with icon + text
    private Button createFeatureButton(String icon, String text) {
        Label iconLabel = new Label(icon);
        iconLabel.setStyle("-fx-font-size: 24px;");

        Label textLabel = new Label(text);
        textLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        VBox content = new VBox(5, iconLabel, textLabel);
        content.setAlignment(Pos.CENTER);

        Button btn = new Button();
        btn.setGraphic(content);
        btn.setPrefSize(200, 100);
        btn.setStyle(
                "-fx-background-color: white; " +
                        "-fx-background-radius: 10; " +
                        "-fx-border-color: #ddd; -fx-border-radius: 10;"
        );

        return btn;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
