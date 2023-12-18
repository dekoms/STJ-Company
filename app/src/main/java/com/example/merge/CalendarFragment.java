package com.example.merge;

import static com.example.merge.CalendarUtil.currentDateInfo;
import static com.example.merge.CalendarUtil.yearMonthDayFromDate;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class CalendarFragment extends Fragment {

    private TextView yearMonth;
    private RecyclerView recyclerviewCalendar;
    private Button preBtn;
    private Button nextBtn;
    private HashSet<String> stringDateList;
    private CalendarAdapter calendarAdapter;
    public static String clickedDate;  //format: yyyy-mm-dd
    public static String currentDate;

    private ArrayList<TodoListItem> todoList;
    private TodoListAdapter todoListAdapter;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    public static String currentUser;


    public CalendarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_calendar_fragment, container, false);


        database = FirebaseDatabase.getInstance();  //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("PetMe");  //DB 테이블 연결

        firebaseAuth = FirebaseAuth.getInstance();  //파이어베이스 인증 초기화
        firebaseUser = firebaseAuth.getCurrentUser();   //현재 인증처리된 객체 가져오기


        yearMonth = (TextView) rootView.findViewById(R.id.year_month);
        preBtn = (Button) rootView.findViewById(R.id.pre_btn);
        nextBtn = (Button) rootView.findViewById(R.id.next_btn);
        recyclerviewCalendar = (RecyclerView) rootView.findViewById(R.id.recyclerView_calendar);

        //현재의 날짜 및 시간 정보 저장
        currentDateInfo = Calendar.getInstance();

        //현재 년 월 일
        int currentYear = currentDateInfo.get(Calendar.YEAR);
        int currentMonth = currentDateInfo.get(Calendar.MONTH)+1;
        int currentDay = currentDateInfo.get(Calendar.DAY_OF_MONTH);
        currentDate = clickedDate = yearMonthDayFromDate(currentYear, currentMonth, currentDay);


        //리사이클러뷰에서 사용할 어댑터 설정
        todoList = new ArrayList<>();
        todoListAdapter = new TodoListAdapter(todoList, databaseReference);


        AuthAsync authAsync = new AuthAsync();
        authAsync.execute();


        return rootView;
    }

    class AuthAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            //현재 사용자 닉네임 가져오기
            String userKey = firebaseUser.getUid();
            databaseReference.child("UserAccount").child(userKey).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot userDatas : snapshot.getChildren()){
                        UserAccount user = snapshot.getValue(UserAccount.class);
                        currentUser = user.getName();
                    }

                    CalendarAsync calendarAsync = new CalendarAsync();
                    calendarAsync.execute();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("FirebaseCurrentUser", String.valueOf(error.toException()));
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {

        }
    }

    class CalendarAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            stringDateList = new HashSet<>(42);

            databaseReference.child("TodoCell").child(currentUser).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    stringDateList.clear();
                    for(DataSnapshot children: snapshot.getChildren()){
                        stringDateList.add(children.getKey());
                    }
                    //어댑터 새로고침, 데이터 변경 반영
                    calendarAdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    //디비를 가져오는 중 에러 발생 시
                    Log.e("FirebaseTodoCell", String.valueOf(error.toException()));
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            //화면 설정
            setMonthView(todoList, todoListAdapter, databaseReference, stringDateList);

            preBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentDateInfo.add(Calendar.MONTH, -1);
                    setMonthView(todoList, todoListAdapter, databaseReference, stringDateList);
                }
            });

            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentDateInfo.add(Calendar.MONTH, 1);
                    setMonthView(todoList, todoListAdapter, databaseReference, stringDateList);
                }
            });
        }
    }

    //년월 형식 설정
    public String yearMonthFromDate(Calendar currentCal){
        int year = currentCal.get(Calendar.YEAR);
        int month = currentCal.get(Calendar.MONTH)+1;

        String yearMonth = year + " "+ month+ "월";

        return yearMonth;
    }

    //dayList에 날짜 저장
    public ArrayList<Date> daysInMonthArray(){

        ArrayList<Date> dayList = new ArrayList<>();

        //날짜 복사해서 변수 생성
        Calendar monthCalInfo =(Calendar) currentDateInfo.clone();

        //1일로 설정(9월 1일)
        monthCalInfo.set(Calendar.DAY_OF_MONTH, 1);

        //요일 가져와서 -1(일요일:1, 월요일:2)
        int firstDayOfMonth = monthCalInfo.get(Calendar.DAY_OF_WEEK) - 1;

        //날짜 설정(5일 전)
        monthCalInfo.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        //날짜 생성
        while(dayList.size() < 42){
            //리스트에 날짜 등록
            dayList.add(monthCalInfo.getTime());

            //1일씩 늘린 날짜로 변경(1일->2일->3일)
            monthCalInfo.add(Calendar.DAY_OF_MONTH, 1);
        }

        return dayList;
    }

    //화면 설정
    public void setMonthView(ArrayList<TodoListItem> todoList, TodoListAdapter todoListAdapter, DatabaseReference databaseReference, HashSet<String> stringDateList){
        //년월 텍스트뷰 설정
        yearMonth.setText(yearMonthFromDate(currentDateInfo));

        //레이아웃 설정(열 7개)
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplication(), 7);
        //레이아웃 적용
        recyclerviewCalendar.setLayoutManager(layoutManager);

        //해당 월 날짜 가져오기
        ArrayList<Date> dayList = daysInMonthArray();
        //어댑터 데이터 적용(준비)
        calendarAdapter = new CalendarAdapter(dayList, todoList, todoListAdapter, databaseReference, stringDateList);
        //어댑터 적용(사용)
        recyclerviewCalendar.setAdapter(calendarAdapter);
    }
}
