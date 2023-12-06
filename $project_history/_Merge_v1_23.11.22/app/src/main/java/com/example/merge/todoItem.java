package com.example.merge;

public class todoItem {
    String todo;
    String startTime, endTime;
    String memo;
    public todoItem(String todo, String startTime, String endTime, String memo) {
        this.todo = todo;
        this.startTime = startTime;
        this.endTime = endTime;
        this.memo = memo;
    }

    public String getTodo() {
        return todo;
    }

    public String getStartTime() {
        return startTime;
    }
    public String getEndTime() { return endTime; }
    public String getMemo() { return memo; }

}
