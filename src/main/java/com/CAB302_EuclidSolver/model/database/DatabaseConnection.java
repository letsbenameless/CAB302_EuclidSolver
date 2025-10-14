package com.CAB302_EuclidSolver.model.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Create users.db database if it doesn't exist in the src file or connects to it if it does exist
 * Establish single database connection during the entire application runtime
 */
public class DatabaseConnection {

    // DatabaseConnection SINGLETON with 'volatile' for thread-safe
    private static volatile DatabaseConnection instance;
    private Connection connection;


    // Private constructor to comply with SINGLETON behaviour
    private DatabaseConnection() {
        try {
            String url = "jdbc:sqlite:database.db";
            connection = DriverManager.getConnection(url);
            System.out.println("Database connection established.");
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
        }
    }

    // SINGLETON instance getter
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    // Getter for the Connection object
    public Connection getConnection() {
        return connection;
    }

}

