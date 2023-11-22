package com.example.petme;

import static com.example.petme.Calendar.clickedDate;
import static com.example.petme.Calendar.currentUser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomDialog extends AlertDialog {

    View rootView;
    RecyclerView recyclerView_todo;
    TodoListAdapter todoListAdapter;
    ArrayList<TodoListItem> list;
    DatabaseReference databaseReference;

    public CustomDialog(Context context, View rootView, TodoListAdapter todoListAdapter, ArrayList<TodoListItem> list, DatabaseReference databaseReference) {
        super(context);
        this.rootView = rootView;
        this.todoListAdapter = todoListAdapter;
        this.list = list;
        this.databaseReference = databaseReference;
    }

    public void run() {
        //다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext(), R.style.AlertDialogTheme);

        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.calendar_cell_dialog);
        View view = LayoutInflater.from(rootView.getContext()).inflate(R.layout.layout_calendar_cell_dialog, linearLayout);
        //View view = getLayoutInflater().inflate(R.layout.todo_form, null);
        //builder.setView(view);

        TextView calendar_cell_dialog_title = (TextView) rootView.findViewById(R.id.calendar_cell_dialog_title_background);
        EditText add_todo = (EditText) rootView.findViewById(R.id.add_todo);
        Button insert_btn = (Button) rootView.findViewById(R.id.insert_btn);

        String day = clickedDate.substring(8);
        calendar_cell_dialog_title.setText(day);
        insert_btn.setText("+");


        //리사이클러뷰 설정
        recyclerView_todo = findViewById(R.id.recyclerView_todo);
        recyclerView_todo.setHasFixedSize(true);    //성능 강화
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_todo.setLayoutManager(linearLayoutManager);

        recyclerView_todo.setAdapter(todoListAdapter);

        //데이터 불러오기
        databaseReference.child("Todo").child(clickedDate).child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
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


        //다이얼로그 생성
        AlertDialog alertDialog = builder.create();

        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todo = add_todo.getText().toString();
                if(todo.equals("")){
                    //인텐트? 다이얼로그?? inflate!!!

                    return;
                }
                TodoListItem todoListTest = new TodoListItem("8시", todo);

                todoListAdapter.addItem(todoListTest);

                databaseReference.child("Todo").child(clickedDate).child(currentUser).child(todo).setValue(todoListTest);

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
/*
생성자에 rootView 없었음.
//다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);

        LinearLayout linearLayout = findViewById(R.id.dialogLinearLayout);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_calendar_cell_dialog, (LinearLayout) findViewById(R.id.calendar_cell_dialog));
        //builder.setView(view);

        TextView calendar_cell_dialog_title = (TextView) findViewById(R.id.calendar_cell_dialog_title_background);
        EditText add_todo = (EditText) findViewById(R.id.add_todo);
        Button insert_btn = (Button) findViewById(R.id.insert_btn);

        String day = clickedDate.substring(8);
        calendar_cell_dialog_title.setText(day);
        insert_btn.setText("+");


        //리사이클러뷰 설정
        recyclerView_todo = findViewById(R.id.recyclerView_todo);
        recyclerView_todo.setHasFixedSize(true);    //성능 강화
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView_todo.setLayoutManager(linearLayoutManager);

        recyclerView_todo.setAdapter(todoListAdapter);

        //데이터 불러오기
        databaseReference.child("Todo").child(clickedDate).child(currentUser).addListenerForSingleValueEvent(new ValueEventListener() {
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


        //다이얼로그 생성
        AlertDialog alertDialog = builder.create();

        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String todo = add_todo.getText().toString();
                if(todo.equals("")){
                    //인텐트? 다이얼로그?? inflate!!!


                    return;
                }
                TodoListItem todoListTest = new TodoListItem("8시", todo);

                todoListAdapter.addItem(todoListTest);

                databaseReference.child("Todo").child(clickedDate).child(currentUser).child(todo).setValue(todoListTest);

                add_todo.setText("");

                todoListAdapter.notifyDataSetChanged();

            }
        });

        //다이얼로그 형태 지우기
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();
 */