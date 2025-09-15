package com.example.javafx_backend_test;

public class Notes {
    private int noteID;
    private int userID;
    private String title;
    private String imageLink;
    private String dateCreated;

    public Notes(int userID, String title, String imageLink) {
        this.userID = userID;
        this.title = title;
        this.imageLink = imageLink;
    }

    // get information from other fields
    public int getNoteID() { return noteID; }
    public int getUserID() { return userID; }
    public String getTitle() { return title; }
    public String getImageLink() { return imageLink; }
    public String getDateCreated() { return dateCreated; }

    public void setNoteID(int noteID) { this.noteID = noteID; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }
}
