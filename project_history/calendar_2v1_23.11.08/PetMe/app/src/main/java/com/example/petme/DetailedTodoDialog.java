package com.example.petme;

import static com.example.petme.CalendarFragment.clickedDate;
import static com.example.petme.CalendarFragment.currentUser;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class DetailedTodoDialog extends AlertDialog {

    LayoutInflater inflater;
    ArrayList<TodoListItem> todoList;
    TodoListAdapter todoListAdapter;
    DatabaseReference databaseReference;

    public DetailedTodoDialog(LayoutInflater inflater, ArrayList<TodoListItem> todoList, TodoListAdapter todoListAdapter, DatabaseReference databaseReference) {
        super(inflater.getContext());
        this.inflater = inflater;
        this.todoList = todoList;
        this.todoListAdapter = todoListAdapter;
        this.databaseReference = databaseReference;
    }

    //TodoListItem 정보를 갖고 있는 수정 기능 때 필요한 생성자??? todoList의 position을 활용할 수는 없나?
    public DetailedTodoDialog(LayoutInflater inflater, ArrayList<TodoListItem> todoList, TodoListAdapter todoListAdapter, DatabaseReference databaseReference, TodoListItem todoListItem) {
        super(inflater.getContext());
        this.inflater = inflater;
        this.todoList = todoList;
        this.todoListAdapter = todoListAdapter;
        this.databaseReference = databaseReference;
        //todoListItem
    }

    public void run(){
        //다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        View view = inflater.inflate(R.layout.todo_form, null);
        builder.setView(view);

        EditText et_title = (EditText) view.findViewById(R.id.et_title);
        Switch st_allDay = (Switch) view.findViewById(R.id.st_allDay);
        TimePicker tp_start_time = (TimePicker) view.findViewById(R.id.tp_start_time);
        TimePicker tp_end_time = (TimePicker) view.findViewById(R.id.tp_end_time);
        EditText et_memo = (EditText) view.findViewById(R.id.et_memo);
        Button cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
        Button save_btn = (Button) view.findViewById(R.id.save_btn);

        tp_start_time.setIs24HourView(true);
        tp_end_time.setIs24HourView(true);

        tp_start_time.setHour(8);
        tp_start_time.setMinute(0);
        tp_end_time.setHour(9);
        tp_end_time.setMinute(0);


        //다이얼로그 생성
        AlertDialog alertDialog = builder.create();

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_title.getText().toString();

                boolean allDay;
                String startTime = null;
                String endTime = null;
                if(st_allDay.isChecked()){
                    allDay = true;
                } else{
                    allDay = false;
                    startTime = String.format("%02d:%02d", tp_start_time.getHour(), tp_start_time.getMinute());
                    endTime = String.format("%02d:%02d", tp_end_time.getHour(), tp_end_time.getMinute());
                }

                String memo = et_memo.getText().toString();

                TodoListItem todoListItem;
                if(st_allDay.isChecked()){
                    todoListItem = new TodoListItem(title, allDay, memo);
                } else{
                    todoListItem = new TodoListItem(title, startTime, endTime, memo);
                }

                if(title.equals("")){
                    Toast.makeText(inflater.getContext(), "제목을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else{
                    todoListAdapter.addItem(todoListItem);
                    databaseReference.child("Todo").child(clickedDate).child(currentUser).child(todoListItem.getTitle()).setValue(todoListItem);
                    alertDialog.dismiss();
                }
            }
        });

        //다이얼로그 형태 지우기
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

    }

    /*
    Todo 업데이트
    //데이터 불러오기에 해당하는 부분
    //데이터 불러와서 수정하기
    String title = todoList.get(getTodoAdapterPosition()).getTitle;
    et_todo.setText(title);
     */
}
