package com.example.calendar;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//Data Access Object
@Dao
public interface TodoDao {

    @Insert //삽입
    void setInsertTodo(Todo todo);

    @Update //수정
    void setUpdateTodo(Todo todo);

    @Delete //삭제
    void setDeleteTodo(Todo todo);

    //조회
    @Query("SELECT * FROM Todo")
    List<Todo> getTodoAll();

}
