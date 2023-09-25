package com.example.customcalendar;

public class TodoListItem {

    int resID;
    int todoID;
    String userID;
    String time;
    String content;
    String doDate;

    public TodoListItem(int resID, int todoID, String userID, String time, String content, String doDate) {
        this.resID = resID;
        this.todoID = todoID;
        this.userID = userID;
        this.time = time;
        this.content = content;
        this.doDate = doDate;
    }
}
