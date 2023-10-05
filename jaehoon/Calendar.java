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

    TextView yearMonth; //년월 텍스트뷰
    RecyclerView recyclerView_calendar;

    RecyclerView recyclerView_todo;
    TodoListAdapter todoListAdapter;
    LinearLayoutManager linearLayoutManager;
    ArrayList<TodoListItem> list;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUesr;
    String currentUser;

    Dialog show;

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


        TextView title = rootView.findViewById(R.id.title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(rootView.getContext(), "2023-09-08", Toast.LENGTH_SHORT).show();
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


//        show = new Dialog(rootView.getContext());
//        show.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        show.setContentView(R.layout.todo_form);
//
//        rootView.findViewById(R.id.showBtn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showDialog();
//            }
//        });
//        //다이얼로그 사용할 때 todo_list.xml 사용하기


        show = new Dialog(rootView.getContext());
        show.requestWindowFeature(Window.FEATURE_NO_TITLE);
        show.setContentView(R.layout.layout_calendar_cell_dialog);

        rootView.findViewById(R.id.showBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialog = LayoutInflater.from(getContext()).inflate(R.layout.layout_calendar_cell_dialog, (LinearLayout)getView().findViewById(R.id.dialogLinearLayout));
                //getContext(), view.getContext()
                @SuppressLint("ResourceType")
                View dialogParent = (LinearLayout) getView().findViewById(R.layout.layout_calendar_cell_dialog);

                recyclerView_todo = dialog.findViewById(R.id.recyclerView_todo);
                recyclerView_todo.setHasFixedSize(true);    //성능 강화
                linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                recyclerView_todo.setLayoutManager(linearLayoutManager);

                //Todo 객체를 담을 리스트(어댑터 쪽으로)
                list = new ArrayList<>();

                todoListAdapter = new TodoListAdapter(getContext(), list);
                recyclerView_todo.setAdapter(todoListAdapter);


                //더미 데이터
                TodoListItem item = new TodoListItem(0, "8시", "산책 하기");
                todoListAdapter.addItem(item);
                todoListAdapter.addItem(item);
                todoListAdapter.notifyDataSetChanged();

                //tlqkf? 똑같다네.. 지피티가 시키는대로 하나 만들자
                if(getView().getContext() == getContext()){
                    Toast.makeText(getContext(), "tlqkf", Toast.LENGTH_SHORT).show();
                }
                
                String str = String.valueOf(list.size());
                Toast.makeText(getView().getContext(), str, Toast.LENGTH_SHORT).show();
                customDialog();
            }
        });


        return rootView;
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


    public void customDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);

        //TextView, EditText, Button에 findViewById로 접근 하기 위함.
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_calendar_cell_dialog, (LinearLayout)getView().findViewById(R.id.dialogLinearLayout));
        //여기서는 사용 안 될 듯? 메인에서 어댑터 연결할 때 쓸 듯?
        //View viewParent = LayoutInflater.from(getContext()).inflate(R.layout.layout_calendar_cell_dialog, (LinearLayout)getView().findViewById(R.id.dialogLinearLayout)).getRootView();


        builder.setView(view);

        ((TextView)view.findViewById(R.id.calendar_cell_dialog_title_background)).setText("며칠?");
        EditText add_todo = (EditText) view.findViewById(R.id.add_todo);
        ((Button)view.findViewById(R.id.insert_btn)).setText("+");


        //데이터 불러오기
        databaseReference.child("Todo").child("2023-09-21").child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();   //기존 배열리스트가 존재하지 않게 초기화
                for(DataSnapshot todos : snapshot.getChildren()){   //반복문으로 리스트 원소 추출
                    TodoListItem todo = todos.getValue(TodoListItem.class); //만들어뒀던 TodoListItem 객체에 데이터를 담는다.

                    todoListAdapter.addItem(todo);
                }
                todoListAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비를 가져오는 중 에러 발생 시
                Log.e("CalendarMain", String.valueOf(error.toException()));
            }
        });


        AlertDialog alertDialog = builder.create();

        view.findViewById(R.id.insert_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todo = add_todo.getText().toString();
                if(todo.equals("")){
                    //인텐트? 다이얼로그?? inflate!!!
                    alertDialog.dismiss();

                    if(alertDialog.getWindow() != null){
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }

                    return;
                }
                TodoListItem todoListTest = new TodoListItem(0, "8시", todo);

                todoListAdapter.addItem(todoListTest);

                databaseReference.child("Todo").child("2023-09-21").child(currentUser).child(todo).setValue(todoListTest);

                add_todo.setText("");

                todoListAdapter.notifyDataSetChanged();

            }
        });

        //다이얼로그 형태 지우기
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
    }

}
