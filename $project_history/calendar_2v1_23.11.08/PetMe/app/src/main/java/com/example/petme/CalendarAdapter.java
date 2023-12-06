package com.example.petme;

import static com.example.petme.CalendarFragment.clickedDate;
import static com.example.petme.CalendarUtil.*;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.resources.Compatibility;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    ArrayList<Date> dayList;
    ArrayList<TodoListItem> todoList;
    TodoListAdapter todoListAdapter;
    DatabaseReference databaseReference;

    public CalendarAdapter(ArrayList<Date> dayList, ArrayList<TodoListItem> todoList, TodoListAdapter todoListAdapter, DatabaseReference databaseReference){
        this.dayList = dayList;
        this.todoList = todoList;
        this.todoListAdapter = todoListAdapter;
        this.databaseReference = databaseReference;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.calendar_cell, parent, false);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        //날짜를 변수에 담기
        Date day = dayList.get(position);

        //날짜 및 시간 정보 저장
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(day);

        //넘어온 년 월
        int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH)+1;
        int displayYear = dateCalendar.get(Calendar.YEAR);

        //현재 년 월 일
        int currentYear = currentDateInfo.get(Calendar.YEAR);
        int currentMonth = currentDateInfo.get(Calendar.MONTH)+1;
        int currentDay = currentDateInfo.get(Calendar.DAY_OF_MONTH);


        //비교해서 년, 월 같으면 진한색 아니면 연한색으로 변경
        if(displayYear==currentYear && displayMonth==currentMonth){
            holder.parentView.setBackgroundColor(Color.parseColor("#D5D5D5"));

            //날짜까지 맞으면 색상 표시
            if(displayDay==currentDay) {
                holder.itemView.setBackgroundColor((Color.parseColor("#CEFBC9")));

            }
        } else{
            holder.parentView.setBackgroundColor(Color.parseColor("#F6F6F6"));
        }


        //날을 변수에 담기
        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        holder.day.setText(String.valueOf(dayNo));


        //텍스트 색상 지정
        if((position + 1) % 7 == 0){    //토요일
            holder.day.setTextColor(Color.BLUE);
        } else if(position == 0 || position % 7 == 0) {  //일요일
            holder.day.setTextColor(Color.RED);
        }

        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Drawable drawable = (Drawable) ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.clicked_cell);
                holder.itemView.setBackground(drawable);

                clickedDate = yearMonthDayFromDate(displayYear, displayMonth, displayDay);

                //CalendarDialog 실행
                holder.alertCalendarDialog();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{

        TextView day;
        View parentView;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            this.day = itemView.findViewById(R.id.tv_day);
            this.parentView = itemView.findViewById(R.id.parent_view);
        }
        public void alertCalendarDialog(){
            //recyclerView, LinearLayout 순으로 탈출
            ViewGroup calendarDialogViewGroup = (ViewGroup) parentView.getParent().getParent();
            LayoutInflater inflater = LayoutInflater.from(calendarDialogViewGroup.getContext());

            CalendarDialog calendarDialog = new CalendarDialog(inflater, todoList, todoListAdapter, databaseReference);
            calendarDialog.run();
        }
    }
}
