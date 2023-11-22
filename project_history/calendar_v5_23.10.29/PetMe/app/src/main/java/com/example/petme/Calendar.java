package com.example.petme;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Calendar extends Fragment{

    TextView title;
    TextView yearMonth; //년월 텍스트뷰
    RecyclerView recyclerView_calendar;
    static String clickedDate;

    RecyclerView recyclerView_todo;
    TodoListAdapter todoListAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<TodoListItem> list;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUesr;
    static String currentUser;

    public Calendar() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.calendar_main, container, false);

        yearMonth = rootView.findViewById(R.id.yearMonth);
        Button pre_btn = rootView.findViewById(R.id.pre_btn);
        Button next_btn = rootView.findViewById(R.id.next_btn);
        recyclerView_calendar = rootView.findViewById(R.id.recyclerView_calendar);


        CalendarUtil.selectedDate = java.util.Calendar.getInstance();

        //현재 년 월 일
        int todayDay = CalendarUtil.selectedDate.get(java.util.Calendar.DAY_OF_MONTH);
        int todayMonth = CalendarUtil.selectedDate.get(java.util.Calendar.MONTH)+1;
        int todayYear = CalendarUtil.selectedDate.get(java.util.Calendar.YEAR);

        String currentDate = getDate(todayYear, todayMonth, todayDay);

        clickedDate = currentDate;

        Toast.makeText(rootView.getContext(), clickedDate, Toast.LENGTH_SHORT).show();

        //화면 설정
        setMonthView();

        pre_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtil.selectedDate.add(java.util.Calendar.MONTH, -1);
                setMonthView();
            }
        });

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarUtil.selectedDate.add(java.util.Calendar.MONTH, 1);
                setMonthView();
            }
        });


        //연습해보기
        title = rootView.findViewById(R.id.title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailedTodo detailedTodo = new DetailedTodo(getContext(), todoListAdapter, list, databaseReference);
                detailedTodo.run();
            }
        });



        database = FirebaseDatabase.getInstance();  //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("PetMe");  //DB 테이블 연결

        firebaseAuth = FirebaseAuth.getInstance();  //파이어베이스 인증 초기화
        firebaseUesr = firebaseAuth.getCurrentUser();   //현재 인증처리된 객체 가져오기

        //현재 사용자 닉네임 가져오기
        String userKey = firebaseUesr.getUid();
        databaseReference.child("UserAccount").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot userDatas : snapshot.getChildren()){
                    UserAccount user = snapshot.getValue(UserAccount.class);
                    currentUser = user.getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("CurrentUser", String.valueOf(error.toException()));
            }
        });





//        Dialog show = new Dialog(rootView.getContext());
//        show.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        show.setContentView(R.layout.todo_form);

        //리사이클러뷰에서 사용할 어댑터 설정
        list = new ArrayList<>();
        todoListAdapter = new TodoListAdapter(getContext(), list);

        rootView.findViewById(R.id.showBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog customDialog = new CustomDialog(rootView.getContext(), rootView, todoListAdapter, list, databaseReference);
                customDialog.run();
            }
        });


        return rootView;
    }

    public String getDate(int y, int m, int d){
        String year = String.valueOf(y);
        String month = String.valueOf(m);
        String day = String.valueOf(d);

        if(m<10)
            month = "0" + month;
        if(d<10)
            day = "0" + day;

        String date = year + "-" + month + "-" + day;

        return date;
    }

    //날짜 타입 설정
    private String yearMonthFromDate(java.util.Calendar calendar){
        int year = calendar.get(java.util.Calendar.YEAR);
        int month = calendar.get(java.util.Calendar.MONTH)+1;

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
        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity().getApplication(), 7);

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
        java.util.Calendar monthCalendar =(java.util.Calendar) CalendarUtil.selectedDate.clone();

        //1일로 설정(9월 1일)
        monthCalendar.set(java.util.Calendar.DAY_OF_MONTH, 1);

        //요일 가져와서 -1(일요일:1, 월요일:2)
        int firstDayOfMonth = monthCalendar.get(java.util.Calendar.DAY_OF_WEEK) - 1;

        //날짜 설정(5일 전)
        monthCalendar.add(java.util.Calendar.DAY_OF_MONTH, -firstDayOfMonth);


        //날짜 생성
        while(dayList.size() < 42){
            //리스트에 날짜 등록
            dayList.add(monthCalendar.getTime());

            //1일씩 늘린 날짜로 변경(1일->2일->3일)
            monthCalendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
        }

        return dayList;
    }



//    public void showDialog(){
//        show.show();
//
//        Button cancelBtn = show.findViewById(R.id.cancel_button);
//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "취소됨", Toast.LENGTH_SHORT).show();
//
//                show.dismiss();
//            }
//        });
//
//        Button saveBtn = show.findViewById(R.id.save_button);
//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "저장됨", Toast.LENGTH_SHORT).show();
//
//                show.dismiss();
//            }
//        });
//
//    }



    /*
        //리사이클러뷰 설정하는 법
        recyclerView_todo = dialog.findViewById(R.id.recyclerView_todo);
        recyclerView_todo.setHasFixedSize(true);    //성능 강화
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_todo.setLayoutManager(linearLayoutManager);

        //리사이클러뷰에서 사용할 어댑터 설정
        list = new ArrayList<>();
        todoListAdapter = new TodoListAdapter(getContext(), list);
        recyclerView_todo.setAdapter(todoListAdapter);
    */
}
