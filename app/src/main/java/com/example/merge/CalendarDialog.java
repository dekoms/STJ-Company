package com.example.merge;

import static com.example.merge.CalendarFragment.clickedDate;
import static com.example.merge.CalendarFragment.currentUser;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CalendarDialog extends AlertDialog {

    private LayoutInflater inflater;
    private ArrayList<TodoListItem> todoList;
    private TodoListAdapter todoListAdapter;
    private DatabaseReference databaseReference;
    private int position;

    public CalendarDialog(LayoutInflater inflater, ArrayList<TodoListItem> todoList, TodoListAdapter todoListAdapter, DatabaseReference databaseReference, int position){
        super(inflater.getContext());
        this.inflater = inflater;
        this.todoList = todoList;
        this.todoListAdapter = todoListAdapter;
        this.databaseReference = databaseReference;
        this.position = position;
    }

    public CalendarDialog(LayoutInflater inflater, ArrayList<TodoListItem> todoList, DatabaseReference databaseReference, int position){
        super(inflater.getContext());
        this.inflater = inflater;
        this.todoList = todoList;
        this.databaseReference = databaseReference;
        this.position = position;
    }

    public void run() {
        //다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);

        View view = inflater.inflate(R.layout.layout_calendar_dialog,null);
        builder.setView(view);

        TextView calendar_cell_dialog_title = (TextView) view.findViewById(R.id.calendar_dialog_day);
        TextView calendar_dialog_dayOfTheWeek = (TextView) view.findViewById(R.id.calendar_dialog_dayOfTheWeek);
        EditText add_todo = (EditText) view.findViewById(R.id.simple_title);
        Button insert_btn = (Button) view.findViewById(R.id.insert_btn);

        String clickedDay = clickedDate.substring(8);
        if(Integer.parseInt(clickedDay) < 10)
            clickedDay = " " + clickedDay.substring(1);
        calendar_cell_dialog_title.setText(clickedDay);

        String dayOfTheWeek = null;
        switch (position % 7){
            case 0:
                dayOfTheWeek = "일";
                break;
            case 1:
                dayOfTheWeek = "월";
                break;
            case 2:
                dayOfTheWeek = "화";
                break;
            case 3:
                dayOfTheWeek = "수";
                break;
            case 4:
                dayOfTheWeek = "목";
                break;
            case 5:
                dayOfTheWeek = "금";
                break;
            case 6:
                dayOfTheWeek = "토";
                break;
        }
        calendar_dialog_dayOfTheWeek.setText(dayOfTheWeek + "요일");

        insert_btn.setText("+");


        //리사이클러뷰 설정
        RecyclerView recyclerviewTodo = (RecyclerView) view.findViewById(R.id.recyclerview_todo);
        recyclerviewTodo.setHasFixedSize(true);    //성능 강화
        //레이아웃 설정
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        //레이아웃 적용
        recyclerviewTodo.setLayoutManager(linearLayoutManager);
        //어댑터 적용
        recyclerviewTodo.setAdapter(todoListAdapter);


        //데이터 실시간 통신
        databaseReference.child("Todo").child(clickedDate).child(currentUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                todoList.clear();   //기존 배열리스트가 존재하지 않게 초기화
                for(DataSnapshot todos : snapshot.getChildren()){   //반복문으로 리스트 원소 추출
                    TodoListItem todo = todos.getValue(TodoListItem.class); //만들어뒀던 TodoListItem 객체에 데이터를 담는다.

                    todoListAdapter.addItem(todo);
                }
                todoListAdapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //디비를 가져오는 중 에러 발생 시
                Log.e("FirebaseTodo", String.valueOf(error.toException()));
            }
        });


        //다이얼로그 생성
        AlertDialog alertDialog = builder.create();

        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = add_todo.getText().toString();
                if(title.equals("")){
                    DetailedTodoDialog detailedTodoDialog = new DetailedTodoDialog(inflater, todoList, todoListAdapter, databaseReference);
                    detailedTodoDialog.run();
                    return;
                }
                TodoListItem todoListItem = new TodoListItem(title);

                todoListAdapter.addItem(todoListItem);
                databaseReference.child("Todo").child(clickedDate).child(currentUser).child(title).setValue(todoListItem);

                databaseReference.child("TodoCell").child(currentUser).child(clickedDate).child(title).setValue(todoListItem);

                add_todo.setText("");
            }
        });

        //다이얼로그 형태 지우기
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

    }

    public void delete(){
        String title = todoList.get(position).getTitle();
        databaseReference.child("Todo").child(clickedDate).child(currentUser).child(title).removeValue();

        databaseReference.child("TodoCell").child(currentUser).child(clickedDate).child(title).removeValue();
    }

}
