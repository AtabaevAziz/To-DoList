package com.example.to_dolist;

public class RecyclerHistoryEntity {
    private  int id;
    private String task;
    private String doneDate;

    public RecyclerHistoryEntity() {
    }

    public RecyclerHistoryEntity(int id, String task, String doneDate) {
        this.id = id;
        this.task = task;
        this.doneDate = doneDate;
    }

    public int getId() {return id;}

    public String getTask() {
        return task;
    }

    public String getDoneDate() {
        return doneDate;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setDoneDate(String doneDate) {
        this.doneDate = doneDate;
    }


}
