module com.example.javafx_backend_test {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javafx_backend_test to javafx.fxml;
    exports com.example.javafx_backend_test;
}