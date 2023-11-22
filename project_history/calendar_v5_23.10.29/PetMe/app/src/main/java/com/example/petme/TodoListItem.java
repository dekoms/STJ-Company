package com.example.petme;

public class TodoListItem {
    int resID;
    boolean isDone;
    String userID;
    String time;
    String content;
    String doDate;

    public TodoListItem() {}

    public TodoListItem(String time, String content) {
        //this.resID = resID;
        this.time = time;
        this.content = content;
        setDone(false);
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
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
