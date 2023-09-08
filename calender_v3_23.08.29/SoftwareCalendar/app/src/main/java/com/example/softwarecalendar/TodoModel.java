package com.example.softwarecalendar;

public class TodoModel {
    private String userID;
    private String time;
    private String content;
    private String doDate;

    public TodoModel(String userID, String time, String content, String doDate) {
        this.userID = userID;
        this.time = time;
        this.content = content;
        this.doDate = doDate;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDoDate() {
        return doDate;
    }

    public void setDoDate(String doDate) {
        this.doDate = doDate;
    }
}
