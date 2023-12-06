package com.example.merge;

import java.util.Calendar;

public class CalendarUtil {
    public static Calendar currentDateInfo;     //현재의 날짜 및 시간 정보 저장

    //날짜 형식 설정 (format: yyyy-mm-dd)
    public static String yearMonthDayFromDate(int y, int m, int d){
        String year = String.valueOf(y);
        String month = String.valueOf(m);
        String day = String.valueOf(d);

        if(m < 10)
            month = "0" + month;
        if(d < 10)
            day = "0" + day;

        String yearMonthDay = year + "-" + month + "-" + day;

        return yearMonthDay;
    }

}
