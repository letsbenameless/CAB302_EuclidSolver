package com.CAB302_EuclidSolver.model.database;

import com.CAB302_EuclidSolver.model.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO implements IUserDAO {
    private final Connection connection;


    // Constructor for UserDAO
    public UserDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }


    //
    @Override
    public void createUserTable() {
        try {
            Statement createUserTable = connection.createStatement();

            createUserTable.execute(
            "CREATE TABLE IF NOT EXISTS users ("
                + "userID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "email TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL)"
            );
        } catch (SQLException ex) {
            throw new DataAccessException("Error creating user database table:", ex);
        }
    }


    // Insert/Create new user into the database
    @Override
    public void insert(User user) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";

        try {
            PreparedStatement insertUser = connection.prepareStatement(sql);

            insertUser.setString(1, user.getUsername());
            insertUser.setString(2, user.getEmail());
            insertUser.setString(3, user.getPassword());
            insertUser.executeUpdate();

        } catch (SQLException ex) {
            throw new DataAccessException("Error inserting user:", ex);
        }
    }


    // Retrieve a list with all registered users
    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        try {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT * FROM users");

            while (rs.next()) {
                users.add(
                    new User(
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                    )
                );
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error retrieving all users.", ex);
        }

        return users;
    }



    // Private method with access to the database
    private User getUserByField(String field, String value) {
        String sql = "SELECT userID, username, email, password FROM users WHERE " + field + " = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password")
                );
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Error fetching user by " + field + ": " + value, ex);
        }
        return null;
    }


    // Retrieve User object by their username
    @Override
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(getUserByField("username", username));
    }


    // Retrieve User object by their email address
    @Override
    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(getUserByField("email", email));
    }

}
