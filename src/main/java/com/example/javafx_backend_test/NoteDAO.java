package com.example.javafx_backend_test;

import java.sql.*;

public class NoteDAO {
    private Connection connection;

    public NoteDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createNoteTable() {
        try {
            Statement createNoteTable = connection.createStatement();
            createNoteTable.execute(
                    "CREATE TABLE IF NOT EXISTS notes (" +
                            "noteID INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "userID INTEGER NOT NULL," +
                            "title TEXT NOT NULL," +
                            "dateCreated TEXT NOT NULL," +
                            "imageLink TEXT NOT NULL," +
                            "FOREIGN KEY (userID) REFERENCES users(userID))"
            );
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }

    public void insert(Notes note) {
        try {
            PreparedStatement insertNote = connection.prepareStatement(
                    "INSERT INTO notes (userID, title, dateCreated, imageLink) VALUES (?, ?, ?, ?)"
            );
            insertNote.setInt(1, note.getUserID());
            insertNote.setString(2, note.getTitle());
            insertNote.setString(3, note.getDateCreated());
            insertNote.setString(4, note.getImageLink());
            insertNote.execute();
        } catch (SQLException ex) {
            System.err.println(ex);
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
