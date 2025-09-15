package com.example.javafx_backend_test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO {
        connection = DatabaseConnection.getInstance();
    }

    public void createUserTable() {
        try {
            Statement createUserTable = connection.createStatement();
            createUserTable.execute(
                    "CREATE TABLE IF NOT EXISTS users ("
                            + "userID INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "username TEXT UNIQUE NOT NULL,"
                            + "email TEXT UNIQUE NOT NULL,"
                            + "password TEXT NOT NULL"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }

        public void insert(User users) {
            try {
                PreparedStatement insertUser = connection.prepareStatement(
                        "INSERT INTO users (username, email, password) VALUES (?, ?, ?)"
                );
                insertUser.setString(1, users.getUsername());
                insertUser.setString(2, users.getEmail());
                insertUser.setString(3, users.getPassword());
                insertUser.execute();
            } catch (SQLException ex) {
                System.err.println(ex);
            }

        }






    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
