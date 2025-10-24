module com.example.CAB302_EuclidSolver {
    requires javafx.controls;
    requires javafx.fxml;

    // ✅ Add this line so javax.imageio, java.awt, etc. are accessible
    requires java.desktop;
    requires java.logging;
    requires java.sql;

    // If you use FXML with controllers in this package:
    opens com.CAB302_EuclidSolver to javafx.fxml;

    // Export your package so other modules (and JavaFX runtime) can see it
    exports com.CAB302_EuclidSolver;
    exports com.CAB302_EuclidSolver.model.database;
    opens com.CAB302_EuclidSolver.model.database to javafx.fxml;
    exports com.CAB302_EuclidSolver.model.user;
    opens com.CAB302_EuclidSolver.model.user to javafx.fxml;
    exports com.CAB302_EuclidSolver.util;
    opens com.CAB302_EuclidSolver.util to javafx.fxml;
    exports com.CAB302_EuclidSolver.controller;
    opens com.CAB302_EuclidSolver.controller to javafx.fxml;
    exports com.CAB302_EuclidSolver.model.question;
    opens com.CAB302_EuclidSolver.model.question to javafx.fxml;
}
