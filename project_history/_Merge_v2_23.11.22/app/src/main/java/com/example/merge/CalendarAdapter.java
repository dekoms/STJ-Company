package com.example.merge;

import static com.example.merge.CalendarUtil.*;
import static com.example.merge.CalendarFragment.*;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.*;
import com.google.firebase.auth.*;

import java.util.*;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>{

    ArrayList<Date> dayList;
    ArrayList<TodoListItem> todoList;
    TodoListAdapter todoListAdapter;
    DatabaseReference databaseReference;
    CalendarViewHolder lastHolder;
    int flag = 0;
    HashSet<String> stringDateList;

    public CalendarAdapter(ArrayList<Date> dayList, ArrayList<TodoListItem> todoList, TodoListAdapter todoListAdapter, DatabaseReference databaseReference, HashSet<String> stringDateList){
        this.dayList = dayList;
        this.todoList = todoList;
        this.todoListAdapter = todoListAdapter;
        this.databaseReference = databaseReference;
        this.stringDateList = stringDateList;
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
        String displayDate = yearMonthDayFromDate(displayYear, displayMonth, displayDay);



        //달력 cell에 할 일 유무 표시
        //오락가락 맨처음 HashSet저장이 제대로 안됨.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(String i:stringDateList){
                    Log.e("aaaa", i);
                    if(i.equals(displayDate)){
                        holder.createState(displayDate);
                    }
                }
            }
        });



        //현재 년 월 일
        int currentYear = currentDateInfo.get(Calendar.YEAR);
        int currentMonth = currentDateInfo.get(Calendar.MONTH)+1;
        int currentDay = currentDateInfo.get(Calendar.DAY_OF_MONTH);

        //비교해서 년, 월 같으면 진한색 아니면 연한색으로 변경
        if(displayYear==currentYear && displayMonth==currentMonth){
            holder.parentView.setBackgroundColor(Color.parseColor("#D5D5D5"));

            //날짜까지 맞으면 색상 표시
            if(displayDate.equals(currentDate)) {
                holder.itemView.setBackgroundColor((Color.parseColor("#CEFBC9")));
            }
        } else{
            holder.parentView.setBackgroundColor(Color.parseColor("#F6F6F6"));
        }


        //날을 변수에 담기
        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);
        holder.day.setText(String.valueOf(dayNo));


        //날짜 색상 지정
        holder.setDayColor(position);


        int lastFlag = flag;
        //날짜 클릭 이벤트
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastFlag != flag){
                    lastHolder.setDayColor(lastHolder.getAdapterPosition());
                }

                holder.day.setTextColor(Color.YELLOW);
                flag++;
                lastHolder = holder;


                clickedDate = yearMonthDayFromDate(displayYear, displayMonth, displayDay);

                holder.alertCalendarDialog(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dayList.size();
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder{

        View parentView;
        TextView day;

        public CalendarViewHolder(@NonNull View itemView) {
            super(itemView);
            this.parentView = itemView.findViewById(R.id.parent_view);
            this.day = itemView.findViewById(R.id.tv_day);
        }

        public void setDayColor(int position){
            if((position + 1) % 7 == 0){    //토요일
                day.setTextColor(Color.BLUE);
            } else if(position == 0 || position % 7 == 0) {  //일요일
                day.setTextColor(Color.RED);
            } else{
                day.setTextColor(Color.BLACK);
            }
        }

        public void alertCalendarDialog(int position){
            //recyclerView, LinearLayout 순으로 탈출
            ViewGroup calendarDialogViewGroup = (ViewGroup) parentView.getParent().getParent();
            LayoutInflater inflater = LayoutInflater.from(calendarDialogViewGroup.getContext());

            CalendarDialog calendarDialog = new CalendarDialog(inflater, todoList, todoListAdapter, databaseReference, position);
            calendarDialog.run();
        }

        public void createState(String displayDate){
            ViewGroup calendarDialogViewGroup = (ViewGroup) parentView.getParent().getParent();
            LayoutInflater inflater = LayoutInflater.from(calendarDialogViewGroup.getContext());

            CalendarState calendarState = new CalendarState(inflater, databaseReference, displayDate, stringDateList);
            calendarState.createState();
        }

    }
}
