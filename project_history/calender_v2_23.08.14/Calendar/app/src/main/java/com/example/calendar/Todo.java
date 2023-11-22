package com.example.calendar;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

//데이터 모델
@Entity
public class Todo {
    @PrimaryKey(autoGenerate = true)
    private int id = 0;
    private String todo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

}
