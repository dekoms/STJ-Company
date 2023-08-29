package com.example.softwarecalendar;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface TodoApi {
    @FormUrlEncoded
    @POST("Todo.php")
    Call<TodoModel>insertTodo(
            @Field("userID")String userID,
            @Field("time")String time,
            @Field("content")String content,
            @Field("doDate")String doDate
    );
}
