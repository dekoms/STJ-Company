package com.example.calendar;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Todo.class}, version = 2)
public abstract class TodoDatabase extends RoomDatabase {
    public abstract TodoDao todoDao();

}
