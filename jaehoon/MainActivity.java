package com.example.recalendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TodoListAdapter todoListAdapter;

    Dialog double_click;
    Dialog one_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        double_click = new Dialog(MainActivity.this);
        double_click.requestWindowFeature(Window.FEATURE_NO_TITLE);
        double_click.setContentView(R.layout.double_click);

        findViewById(R.id.showBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog1();
            }
        });

        one_click = new Dialog(MainActivity.this);
        one_click.requestWindowFeature(Window.FEATURE_NO_TITLE);
        one_click.setContentView(R.layout.one_click);

        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog2();
            }
        });

    }



    public void showDialog1(){
        double_click.show();

        Button cancelBtn = double_click.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "취소됨", Toast.LENGTH_SHORT).show();

                double_click.dismiss();
            }
        });

        Button saveBtn = double_click.findViewById(R.id.save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "저장됨", Toast.LENGTH_SHORT).show();

                double_click.dismiss();
            }
        });

    }

    public void showDialog2(){
        one_click.show();

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        todoListAdapter = new TodoListAdapter(getApplicationContext());
        recyclerView.setAdapter(todoListAdapter);

        todoListAdapter.addItem(new TodoListItem(1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(1, "stj", "8시", "산책 하기", "2023-08-22"));





        TextView date = one_click.findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "2023-09-08", Toast.LENGTH_SHORT).show();
            }
        });


    }
}