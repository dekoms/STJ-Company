package com.example.merge;

public class TodoListItem {

    private boolean isDone = false;
    private String title;
    private boolean allDay;
    private String startTime;
    private String endTime;
    private String memo;

    public TodoListItem(){
    }

    public TodoListItem(String title) {
        this.title = title;
        setAllDay(true);
        setStartTime("");
        setEndTime("");
        setMemo("");
    }
    public TodoListItem(String title, boolean allDay, String memo){
        this.title = title;
        this.allDay = allDay;
        setStartTime("");
        setEndTime("");
        this.memo = memo;
    }
    public TodoListItem(String title, String startTime, String endTime, String memo){
        this.title = title;
        setAllDay(false);
        this.startTime = startTime;
        this.endTime = endTime;
        this.memo = memo;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(boolean allDay) {
        this.allDay = allDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
