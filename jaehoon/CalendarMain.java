package com.example.customcalendar;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarMain extends AppCompatActivity {

    TextView yearMonth; //년월 텍스트뷰
    RecyclerView recyclerView_calendar;
    //Calendar calendar;

    RecyclerView recyclerView;
    TodoListAdapter todoListAdapter;

    Dialog double_click;
    Dialog one_click;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_main);


        yearMonth = findViewById(R.id.yearMonth);
        Button pre_btn = findViewById(R.id.pre_btn);
        Button next_btn = findViewById(R.id.next_btn);
        recyclerView_calendar = findViewById(R.id.recyclerView_calendar);


        CalendarUtil.selectedDate = Calendar.getInstance();

        //화면 설정
        setMonthView();

        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CalendarUtil.selectedDate.add(Calendar.MONTH, -1);
                setMonthView();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CalendarUtil.selectedDate.add(Calendar.MONTH, 1);
                setMonthView();
            }
        });




        double_click = new Dialog(CalendarMain.this);
        double_click.requestWindowFeature(Window.FEATURE_NO_TITLE);
        double_click.setContentView(R.layout.double_click);

        findViewById(R.id.showBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog1();
            }
        });

        one_click = new Dialog(CalendarMain.this);
        one_click.requestWindowFeature(Window.FEATURE_NO_TITLE);
        one_click.setContentView(R.layout.one_click);

        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog2();
            }
        });

    }

    //날짜 타입 설정
    private String yearMonthFromDate(Calendar calendar){
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;

        String yearMonth = year + " "+ month+ "월";

        return yearMonth;
    }

    //화면 설정
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView(){

        //년월 텍스트뷰 설정
        yearMonth.setText(yearMonthFromDate(CalendarUtil.selectedDate));

        //해당 월 날짜 가져오기
        ArrayList<Date> dayList = daysInMonthArray();

        //어댑터 데이터 적용
        CalendarAdapter adapter = new CalendarAdapter(dayList);

        //레이아웃 설정(열 7개)
        RecyclerView.LayoutManager manager = new GridLayoutManager(getApplicationContext(), 7);

        //레이아웃 적용
        recyclerView_calendar.setLayoutManager(manager);

        //어댑터 적용
        recyclerView_calendar.setAdapter(adapter);
    }

    //날짜 생성
    @RequiresApi(api = Build.VERSION_CODES.O)
    private ArrayList<Date> daysInMonthArray(){

        ArrayList<Date> dayList = new ArrayList<>();

        //날짜 복사해서 변수 생성
        Calendar monthCalendar =(Calendar) CalendarUtil.selectedDate.clone();

        //1일로 설정(9월 1일)
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        //요일 가져와서 -1(일요일:1, 월요일:2)
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        //날짜 설정(5일 전)
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);





        //날짜 생성
        while(dayList.size() < 42){
            //리스트에 날짜 등록
            dayList.add(monthCalendar.getTime());

            //1일씩 늘린 날짜로 변경(1일->2일->3일)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dayList;
    }





    public void showDialog1(){
        double_click.show();

        Button cancelBtn = double_click.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CalendarMain.this, "취소됨", Toast.LENGTH_SHORT).show();

                double_click.dismiss();
            }
        });

        Button saveBtn = double_click.findViewById(R.id.save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CalendarMain.this, "저장됨", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(CalendarMain.this, "2023-09-08", Toast.LENGTH_SHORT).show();
            }
        });


    }


}