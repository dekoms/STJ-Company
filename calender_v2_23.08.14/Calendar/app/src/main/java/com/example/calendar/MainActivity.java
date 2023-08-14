package com.example.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TodoDao mTodoDao;
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

        TodoDatabase database = Room.databaseBuilder(getApplicationContext(), TodoDatabase.class, "STJ_db")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();

        mTodoDao = database.todoDao();      //인터페이스 객체 할당
        Todo todo = new Todo();


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

        list = findViewById(R.id.list);

        List<String> todos = new ArrayList<>();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, todos);
        list.setAdapter(adapter);

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


        todo.setTodo("산책 시키기");
        mTodoDao.setInsertTodo(todo);
        todo.setTodo("병원 다녀 오기");
        mTodoDao.setInsertTodo(todo);


        et_todo = findViewById(R.id.et_todo);
        btn_add = findViewById(R.id.btn_add);

        todos.add("산책 시키기");
        todos.add("병원 다녀 오기");
        adapter.notifyDataSetChanged();

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todo.setTodo(et_todo.getText().toString());
                mTodoDao.setInsertTodo(todo);


                todos.add(et_todo.getText().toString());
                adapter.notifyDataSetChanged();
                et_todo.setText("");
            }
        });

    }

    class CalendarAdapter extends BaseAdapter {
        private ArrayList<Todo> todos = new ArrayList<>();

        public void addTodo(Todo todo){
            todos.add(todo);
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount();
        }

        @Override
        public int getCount() {
            return todos.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            return null;
        }
    }

}
