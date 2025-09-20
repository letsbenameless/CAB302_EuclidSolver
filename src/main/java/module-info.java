module com.example.CAB302_EuclidSolver {
    requires javafx.controls;
    requires javafx.fxml;

    // ✅ Add this line so javax.imageio, java.awt, etc. are accessible
    requires java.desktop;
    requires java.logging;
    requires java.sql;

    // If you use FXML with controllers in this package:
    opens com.example.CAB302_EuclidSolver to javafx.fxml;

    // Export your package so other modules (and JavaFX runtime) can see it
    exports com.example.CAB302_EuclidSolver;
}
