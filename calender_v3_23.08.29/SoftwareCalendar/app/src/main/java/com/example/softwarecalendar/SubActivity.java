package com.example.softwarecalendar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SubActivity extends AppCompatActivity {

    Button btn_back;
    TextView sub_date;
    ListView sub_listView;
    EditText sub_et_todo;
    Button sub_btn_add;

    String[] times = {"8시", "8시", "8시", "8시", "8시", "8시", "8시", "8시", "8시", "8시"};
    String[] contents = {"산책 시키기", "산책 시키기", "산책 시키기", "산책 시키기", "산책 시키기",
            "산책 시키기", "산책 시키기", "산책 시키기", "산책 시키기", "산책 시키기"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);







        Intent subIntent = new Intent(this.getIntent());
        String year = subIntent.getStringExtra("year");
        String month = subIntent.getStringExtra("month");
        String day = subIntent.getStringExtra("day");
        //static ArrayList<Todo> list 사용해보기
        ArrayList<Todo> subList = (ArrayList<Todo>) subIntent.getSerializableExtra("adapterList");




        sub_date = findViewById(R.id.sub_date);
        String date = year+"년 "+month+"월 "+day+"일";
        sub_date.setText(date);


        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subIntent.putExtra("updatedAdapterList", subList);
                setResult(0, subIntent);
                finish();
            }
        });


        sub_listView = findViewById(R.id.sub_listView);
        TodoAdapter adapter = new TodoAdapter(this, subList, R.layout.listview_layout);
        sub_listView.setAdapter(adapter);

        sub_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SubActivity.this, contents[i], Toast.LENGTH_SHORT).show();
            }
        });

        sub_et_todo = findViewById(R.id.sub_et_todo);
        sub_btn_add = findViewById(R.id.sub_btn_add);


        sub_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Todo todo = new Todo(subList.size(), "", "8시", sub_et_todo.getText().toString(), "2023-08-22");
                subList.add(todo);
                adapter.notifyDataSetChanged();
                sub_et_todo.setText("");
            }
        });



    }
}