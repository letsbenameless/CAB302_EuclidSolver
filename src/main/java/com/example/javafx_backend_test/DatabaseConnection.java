package com.example.javafx_backend_test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException


public class DatabaseConnection {
    // create users.db database if it doesnt exist in the src file
    // or connects to it if it does exist
    private static Connection instance = null;

    private DatabaseConnection() {
        String url = "jdbc:sqlite:database.db";
        try {
            instance = DriverManager.getConnection(url);
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new DatabaseConnection();
        }
        return instance;
    }
    }


}
