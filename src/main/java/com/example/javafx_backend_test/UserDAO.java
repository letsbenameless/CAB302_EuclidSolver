package com.example.javafx_backend_test;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    private Connection connection;

    public UserDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createUserTable() {
        try (Statement createUserTable = connection.createStatement()) {
            createUserTable.execute(
                    "CREATE TABLE IF NOT EXISTS users ("
                            + "userID INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "username TEXT UNIQUE NOT NULL, "
                            + "email TEXT UNIQUE NOT NULL, "
                            + "password TEXT NOT NULL)"
            );
        } catch (SQLException ex) {
            System.err.println("Error creating user table: " + ex.getMessage());
        }
    }

    public void insert(User user) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try (PreparedStatement insertUser = connection.prepareStatement(sql)) {
            insertUser.setString(1, user.getUsername());
            insertUser.setString(2, user.getEmail());
            insertUser.setString(3, user.getPassword());
            insertUser.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Error inserting user: " + ex.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException ex) {
            System.err.println("Error closing connection: " + ex.getMessage());
        }
    }


    public List<User> getAll() {
        List<User> accounts = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                accounts.add(
                        new User(
                                rs.getInt("userID"),
                                rs.getString("username"),
                                rs.getString("email"),
                                rs.getString("password")
                        )
                );
            }
        } catch (SQLException ex) {
            System.err.println(ex);
        }
        return accounts;
    }



    public User getUserByUsername(String username) {
        User user = null;

        String sql = "SELECT userID, username, email, password FROM users WHERE username = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching user: " + ex.getMessage());
        }

        return user;
    }


    public User getUserByEmail(String email) {
        User user = null;  // initialize as null, so we can return null if not found

        String sql = "SELECT userID, username, email, password FROM users WHERE email = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (SQLException ex) {
            System.err.println("Error fetching user: " + ex.getMessage());
        }

        return user;
    }


}
