package com.example.javafx_backend_test;

public class Notes {
    private int noteID;
    private int userID;
    private String title;
    private String dateCreated;
    private String imageLink;
    // ^ creates the fields that the notes need to store

    public Notes(int noteID, int userID, String title, String dateCreated, String imageLink) {
        this.noteID = noteID;
        this.userID = userID;
        this.title = title;
        this.dateCreated = dateCreated;
        this.imageLink = imageLink;
    }
    public Notes(int userID, String title, String dateCreated, String imageLink) {
        this.userID = userID;
        this.title = title;
        this.dateCreated = dateCreated;
        this.imageLink = imageLink;
    }
    public int getNoteID() {
        return noteID;
    }
    public int getUserID() {
        return userID;
    }
    public String getTitle() {
        return title;
    }
    public String getDateCreated() {
        return dateCreated;
    }
    public String getImageLink() {
        return imageLink;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
    public String toString() {
        return "Note{" +
                "noteID=" + noteID +
                ", userID=" + userID +
                ", title='" + title + "'" +
                ", dateCreated='" + dateCreated + "'" +
                ", imageLink='" + imageLink + "'" +
                '}';
    }
}
