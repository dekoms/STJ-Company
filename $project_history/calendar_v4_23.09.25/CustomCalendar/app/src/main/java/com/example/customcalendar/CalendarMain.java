package com.example.customcalendar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarMain extends AppCompatActivity {

    TextView yearMonth; //년월 텍스트뷰
    RecyclerView recyclerView_calendar;

    RecyclerView recyclerView_todo;
    TodoListAdapter todoListAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<TodoListItem> list;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    Dialog show;
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



        TextView date = findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CalendarMain.this, "2023-09-08", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView_todo = findViewById(R.id.recyclerView_todo);
        recyclerView_todo.setHasFixedSize(true);    //성능 강화
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView_todo.setLayoutManager(linearLayoutManager);

        //Todo 객체를 담을 리스트(어댑터 쪽으로)
        list = new ArrayList<>();



        database = FirebaseDatabase.getInstance();  //파이어베이스 데이터베이스 연동

        databaseReference = database.getReference("Todo");  //DB 테이블 연결
        //databaseReference.setValue("Hello,World");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는 곳
                list.clear();   //기존 배열리스트가 존재하지 않게 초기화
                for(DataSnapshot snapshot1 : snapshot.getChildren()){   //반복문으로 리스트 원소 추출
                    TodoListItem todo = snapshot1.getValue(TodoListItem.class); //만들어뒀던 TodoListItem 객체에 데이터를 담는다.
                    list.add(todo); //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                todoListAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비를 가져오는 중 에러 발생 시
                DatabaseError databaseError = null;
                Log.e("CalendarMain", String.valueOf(databaseError.toException()));

            }
        });



        todoListAdapter = new TodoListAdapter(this, list);
        recyclerView_todo.setAdapter(todoListAdapter);

        //아이콘 Date Range 사용하기
        todoListAdapter.addItem(new TodoListItem(R.drawable.ic_action_name, 1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(R.drawable.ic_action_name, 1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(R.drawable.ic_action_name, 1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(R.drawable.ic_action_name, 1, "stj", "8시", "산책 하기", "2023-08-22"));
        todoListAdapter.addItem(new TodoListItem(R.drawable.ic_action_name, 1, "stj", "8시", "산책 하기", "2023-08-22"));


        EditText add_todo = findViewById(R.id.add_todo);
        Button insert_btn = findViewById(R.id.insert_btn);

        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todo = add_todo.getText().toString();

                todoListAdapter.addItem(new TodoListItem(R.drawable.ic_action_name, 1, "stj", "8시", todo, "2023-08-22"));

                add_todo.setText("");
            }
        });





        show = new Dialog(CalendarMain.this);
        show.requestWindowFeature(Window.FEATURE_NO_TITLE);
        show.setContentView(R.layout.todo_form);

        findViewById(R.id.showBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog1();
            }
        });

//        //다이얼로그 사용할 때 todo_list.xml 사용하기
//        one_click = new Dialog(CalendarMain.this);
//        one_click.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        one_click.setContentView(R.layout.todo_list);
//
//        findViewById(R.id.title).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDialog2();
//            }
//        });

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
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 7);

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
        show.show();

        Button cancelBtn = show.findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CalendarMain.this, "취소됨", Toast.LENGTH_SHORT).show();

                show.dismiss();
            }
        });

        Button saveBtn = show.findViewById(R.id.save_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CalendarMain.this, "저장됨", Toast.LENGTH_SHORT).show();

                show.dismiss();
            }
        });

    }

//    public void showDialog2(){
//        one_click.show();
//
//        recyclerView_todo = one_click.findViewById(R.id.recyclerView_todo);
//        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//        recyclerView_todo.setLayoutManager(linearLayoutManager);
//
//        todoListAdapter = new TodoListAdapter(getBaseContext(), list);
//        recyclerView_todo.setAdapter(todoListAdapter);
//
//        todoListAdapter.addItem(new TodoListItem(R.drawable.ic_action_name, 1, "stj", "8시", "산책 하기", "2023-08-22"));
//        todoListAdapter.addItem(new TodoListItem(R.drawable.ic_action_name, 1, "stj", "8시", "산책 하기", "2023-08-22"));
//        todoListAdapter.addItem(new TodoListItem(R.drawable.ic_action_name, 1, "stj", "8시", "산책 하기", "2023-08-22"));
//        todoListAdapter.addItem(new TodoListItem(R.drawable.ic_action_name, 1, "stj", "8시", "산책 하기", "2023-08-22"));
//        todoListAdapter.addItem(new TodoListItem(R.drawable.ic_action_name, 1, "stj", "8시", "산책 하기", "2023-08-22"));
//
//
//        TextView date = one_click.findViewById(R.id.date);
//        date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(CalendarMain.this, "2023-09-08", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//    }


}