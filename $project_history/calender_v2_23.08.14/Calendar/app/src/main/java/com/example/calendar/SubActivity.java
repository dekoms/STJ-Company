package com.example.calendar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
    ListView sub_list;
    EditText sub_et_todo;
    Button sub_btn_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        Intent subIntent = new Intent(this.getIntent());
        String year = subIntent.getStringExtra("year");
        String month = subIntent.getStringExtra("month");
        String day = subIntent.getStringExtra("day");


        sub_date = findViewById(R.id.sub_date);
        String date = year+"년 "+month+"월 "+day+"일";
        sub_date.setText(date);


        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        sub_list = findViewById(R.id.sub_list);

        List<String> todos = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, todos);
        sub_list.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        sub_et_todo = findViewById(R.id.sub_et_todo);
        sub_btn_add = findViewById(R.id.sub_btn_add);


        sub_btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todos.add(sub_et_todo.getText().toString());
                adapter.notifyDataSetChanged();
                sub_et_todo.setText("");
            }
        });

    }
}