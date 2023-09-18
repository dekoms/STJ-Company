package com.example.customcalendar;

public class TodoListItem {
    int todoID;
    String userID;
    String time;
    String content;
    String doDate;

    public TodoListItem(int todoID, String userID, String time, String content, String doDate) {
        this.todoID = todoID;
        this.userID = userID;
        this.time = time;
        this.content = content;
        this.doDate = doDate;
    }
}
