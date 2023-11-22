package com.example.calendar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CalendarView cal;
    TextView date;
    ListView list;
    EditText et_todo;
    Button btn_add;

    boolean isDate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cal = findViewById(R.id.cal);
        date = findViewById(R.id.date);

        long currentTime = System.currentTimeMillis();
        Date today = new Date(currentTime);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy년 MM월 dd일");
        String getTime = sdf.format(today);

        date.setText(getTime);
        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                month++;
                String Month = String.valueOf(month);
                String Day = String.valueOf(day);

                Month = (Month.length() < 2) ? ("0" + month) : (Month);
                Day = (Day.length() < 2) ? ("0" + day) : (Day);

                date.setText(year+"년 "+Month+"월 "+Day+"일");
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(MainActivity.this, SubActivity.class);
                mainIntent.putExtra("year", date.getText().toString().substring(0,4));
                mainIntent.putExtra("month", date.getText().toString().substring(6,8));
                mainIntent.putExtra("day", date.getText().toString().substring(10,12));
                startActivity(mainIntent);
            }
        });


        //Toast.makeText(getApplicationContext(), date.getText().toString().substring(9,10), Toast.LENGTH_LONG).show();

        list = findViewById(R.id.list);

        List<String> todos = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, todos);
        list.setAdapter(adapter);


        et_todo = findViewById(R.id.et_todo);
        btn_add = findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todos.add(et_todo.getText().toString());
                adapter.notifyDataSetChanged();
                et_todo.setText("");
            }
        });


        todos.add("산책 시키기");
        todos.add("병원 다녀오기");
        adapter.notifyDataSetChanged();
    }

}