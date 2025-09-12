import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Settings {

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Euclid Solver - Settings");

        // ==== Top Bar ====
        Button homeBtn = new Button("🏠");
        homeBtn.setOnAction(e -> {
            EuclidSolverHome home = new EuclidSolverHome();
            home.start(primaryStage);
        });

        Button profileBtn = new Button("👤");
        Button settingsBtn = new Button("⚙️"); // current page
        HBox rightIcons = new HBox(10, homeBtn, profileBtn, settingsBtn);
        rightIcons.setAlignment(Pos.CENTER_RIGHT);

        BorderPane topBar = new BorderPane();
        topBar.setLeft(new Label("Euclid Solver"));
        topBar.setRight(rightIcons);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        // ==== Section Labels ====
        Label settingsTitle = new Label("Settings");
        settingsTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Label accountLabel = new Label("Account");
        accountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label preferencesLabel = new Label("Preferences");
        preferencesLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // ==== Account Buttons ====
        Button emailBtn = new Button("Email");
        Button passwordBtn = new Button("Password");
        Button usernameBtn = new Button("Username");

        VBox accountBox = new VBox(10, accountLabel, emailBtn, passwordBtn, usernameBtn);
        accountBox.setAlignment(Pos.TOP_LEFT);

        // ==== Preferences Buttons ====
        Button themeBtn = new Button("Light Theme");
        Button profileImgBtn = new Button("Change Profile Image");

        VBox preferencesBox = new VBox(10, preferencesLabel, themeBtn, profileImgBtn);
        preferencesBox.setAlignment(Pos.TOP_LEFT);

        // ==== Sound + Language ====
        Label soundLabel = new Label("Sound");
        ToggleButton soundToggle = new ToggleButton("On");
        soundToggle.setSelected(true);
        soundToggle.setOnAction(e -> {
            if (soundToggle.isSelected()) {
                soundToggle.setText("On");
            } else {
                soundToggle.setText("Off");
            }
        });

        Label languageLabel = new Label("Language");
        Button languageBtn = new Button("EN-AU");

        HBox soundBox = new HBox(10, soundLabel, soundToggle);
        HBox languageBox = new HBox(10, languageLabel, languageBtn);

        VBox leftSettings = new VBox(20, accountBox, soundBox, languageBox);
        VBox rightSettings = new VBox(20, preferencesBox);

        HBox settingsContent = new HBox(50, leftSettings, rightSettings);
        settingsContent.setAlignment(Pos.TOP_CENTER);

        // ==== Main Layout ====
        VBox mainLayout = new VBox(20, topBar, settingsTitle, settingsContent);
        mainLayout.setAlignment(Pos.TOP_CENTER);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #b3e5fc;");

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
