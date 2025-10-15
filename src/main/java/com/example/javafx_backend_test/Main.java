package com.example.javafx_backend_test;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        //
        Connection connection = DatabaseConnection.getInstance();
        UserDAO userDAO = new UserDAO();
        userDAO.createUserTable();
        userDAO.close();
        // everything in between these 2 comments are for creating user database
        NoteDAO noteDAO = new NoteDAO();
        noteDAO.createNoteTable();

        quizUI.main(args);
    }
}