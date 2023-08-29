package com.example.softwarecalendar;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class TodoRequest extends StringRequest{
    //서버 URL 설정(PHP 파일 연동)
    final static private String URL = "http://stjcompany.dothome.co.kr/Todo.php";
    public Map<String, String> map;

    public TodoRequest(int todoID, String userID, String time, String content, String doDate, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("todoID", todoID + "" );
        map.put("userID", userID);
        map.put("time", time);
        map.put("content", content);
        map.put("doDate", doDate);
    }

    @Nullable
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
