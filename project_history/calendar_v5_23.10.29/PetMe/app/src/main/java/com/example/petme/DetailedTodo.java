package com.example.petme;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DetailedTodo extends AlertDialog {

    RecyclerView recyclerView_todo;
    TodoListAdapter todoListAdapter;
    ArrayList<TodoListItem> list;
    DatabaseReference databaseReference;

    public DetailedTodo(Context context, TodoListAdapter todoListAdapter, ArrayList<TodoListItem> list, DatabaseReference databaseReference) {
        super(context);
        this.todoListAdapter = todoListAdapter;
        this.list = list;
        this.databaseReference = databaseReference;
    }

    public void run(){
        //getLayoutInflater().getContext()

        //다이얼로그 설정
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.todo_form, null);
        builder.setView(view);

        //LinearLayout linearLayout = findViewById(R.id.dialogLinearLayout);
        //View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_calendar_cell_dialog, (LinearLayout) findViewById(R.id.calendar_cell_dialog));
        //builder.setView(view);


        //다이얼로그 생성
        AlertDialog alertDialog = builder.create();

        EditText et_todo = view.findViewById(R.id.et_todo);
        Button btn_add = view.findViewById(R.id.btn_add);
        TimePicker timePicker1 = view.findViewById(R.id.time_picker1);
        TimePicker timePicker2 = view.findViewById(R.id.time_picker2);
        EditText et_memo = view.findViewById(R.id.et_memo);
        Button btn_memo = view.findViewById(R.id.btn_memo);
        Button cancel_btn = view.findViewById(R.id.cancel_button);
        Button save_btn = view.findViewById(R.id.save_button);


        timePicker1.setIs24HourView(true);
        timePicker2.setIs24HourView(true);

        timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {


            }
        });


        timePicker2.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hour, int minute) {


            }
        });



        //다이얼로그 형태 지우기
        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        alertDialog.show();

    }

}
