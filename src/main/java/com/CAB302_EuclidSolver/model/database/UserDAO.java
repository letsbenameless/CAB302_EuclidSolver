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


    // Creates User Table in the database
    @Override
    public void createUserTable() {
        try {
            Statement createUserTable = connection.createStatement();

            createUserTable.execute(
            "CREATE TABLE IF NOT EXISTS users ("
                + "userID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "username TEXT UNIQUE NOT NULL, "
                + "email TEXT UNIQUE NOT NULL, "
                + "password TEXT NOT NULL, "
                + "userXP INTEGER NOT NULL, "
                + "total_questions INTEGER NOT NULL, "
                + "total_hard_questions INTEGER NOT NULL, "
                + "total_clock_questions INTEGER NOT NULL)"
            );
        } catch (SQLException ex) {
            throw new DataAccessException("Error creating user database table:", ex);
        }
    }


    // Insert/Create new user into the database
    @Override
    public void insert(User user) {
        String sql = "INSERT INTO users (username, email, password, userXP, total_questions, total_hard_questions, total_clock_questions) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement insertUser = connection.prepareStatement(sql);

            insertUser.setString(1, user.getUsername());
            insertUser.setString(2, user.getEmail());
            insertUser.setString(3, user.getPassword());
            insertUser.setInt(4, 0);
            insertUser.setInt(5, 0);
            insertUser.setInt(6, 0);
            insertUser.setInt(7, 0);
            insertUser.executeUpdate();

        } catch (SQLException ex) {
            throw new DataAccessException("Error inserting user:", ex);
        }
    }



    public void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, email = ?, password = ?, userXP = ?, " +
                "total_questions = ?, total_hard_questions = ?, total_clock_questions = ? " +
                "WHERE userID = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPassword());
            stmt.setInt(4, user.getUserXP());
            stmt.setInt(5, user.getTotalQuestionsAnswered());
            stmt.setInt(6, user.getTotalHardQuestionsAnswered());
            stmt.setInt(7, user.getTotalClockQuestionsAnswered());
            stmt.setInt(8, user.getUserID()); // userID is the primary key

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated == 0) {
                System.out.println("No user found with ID " + user.getUserID());
            } else {
                System.out.println("User updated successfully: " + user.getUsername());
            }

        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
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
                        rs.getString("password"),
                        rs.getInt("userXP"),
                        rs.getInt("total_questions"),
                        rs.getInt("total_hard_questions"),
                        rs.getInt("total_clock_questions")
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
        String sql = "SELECT userID, username, email, password, userXP, total_questions, total_hard_questions, total_clock_questions FROM users WHERE " + field + " = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("userID"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("userXP"),
                        rs.getInt("total_questions"),
                        rs.getInt("total_hard_questions"),
                        rs.getInt("total_clock_questions")
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
