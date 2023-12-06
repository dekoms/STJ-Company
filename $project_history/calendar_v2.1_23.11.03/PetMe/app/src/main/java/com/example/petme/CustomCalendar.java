package com.example.petme;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

public class CustomCalendar {

//    public String getDate(int y, int m, int d){
//        String year = String.valueOf(y);
//        String month = String.valueOf(m);
//        String day = String.valueOf(d);
//
//        if(m<10)
//            month = "0" + month;
//        if(d<10)
//            day = "0" + day;
//
//        String date = year + "-" + month + "-" + day;
//
//        return date;
//    }
//
//    //날짜 타입 설정
//    private String yearMonthFromDate(java.util.Calendar calendar){
//        int year = calendar.get(java.util.Calendar.YEAR);
//        int month = calendar.get(java.util.Calendar.MONTH)+1;
//
//        String yearMonth = year + " "+ month+ "월";
//
//        return yearMonth;
//    }
//
//    //화면 설정
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void setMonthView(){
//
//        //년월 텍스트뷰 설정
//        yearMonth.setText(yearMonthFromDate(CalendarUtil.selectedDate));
//
//        //해당 월 날짜 가져오기
//        ArrayList<Date> dayList = daysInMonthArray();
//
//        //어댑터 데이터 적용
//        CalendarAdapter adapter = new CalendarAdapter(dayList);
//
//        //레이아웃 설정(열 7개)
//        RecyclerView.LayoutManager manager = new GridLayoutManager(getActivity().getApplication(), 7);
//
//        //레이아웃 적용
//        recyclerView_calendar.setLayoutManager(manager);
//
//        //어댑터 적용
//        recyclerView_calendar.setAdapter(adapter);
//    }
//
//    //날짜 생성
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private ArrayList<Date> daysInMonthArray(){
//
//        ArrayList<Date> dayList = new ArrayList<>();
//
//        //날짜 복사해서 변수 생성
//        java.util.Calendar monthCalendar =(java.util.Calendar) CalendarUtil.selectedDate.clone();
//
//        //1일로 설정(9월 1일)
//        monthCalendar.set(java.util.Calendar.DAY_OF_MONTH, 1);
//
//        //요일 가져와서 -1(일요일:1, 월요일:2)
//        int firstDayOfMonth = monthCalendar.get(java.util.Calendar.DAY_OF_WEEK) - 1;
//
//        //날짜 설정(5일 전)
//        monthCalendar.add(java.util.Calendar.DAY_OF_MONTH, -firstDayOfMonth);
//
//
//        //날짜 생성
//        while(dayList.size() < 42){
//            //리스트에 날짜 등록
//            dayList.add(monthCalendar.getTime());
//
//            //1일씩 늘린 날짜로 변경(1일->2일->3일)
//            monthCalendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
//        }
//
//        return dayList;
//    }

}
