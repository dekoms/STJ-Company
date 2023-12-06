package com.example.softwarecalendar;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Todo implements Serializable{
    int todoID;
    String userID;
    String time;
    String content;
    String doDate;

    public Todo(int todoID, String userID, String time, String content, String doDate) {
        this.todoID = todoID;
        this.userID = userID;
        this.time = time;
        this.content = content;
        this.doDate = doDate;
    }

    public int getTodoID() {
        return todoID;
    }

    public void setTodoID(int todoID) {
        this.todoID = todoID;
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
