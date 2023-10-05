package com.example.petme;

public class TodoListItem {
    int resID;
    int id;
    String userID;
    String time;
    String content;
    String doDate;

    public TodoListItem() {}

    public TodoListItem(int id, String time, String content) {
        //this.resID = resID;
        this.id = id;
        this.time = time;
        this.content = content;
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
