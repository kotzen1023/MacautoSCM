package com.macauto.macautoscm.data;


public class HistoryItem {
    private int action;
    private String title;
    private String msg;
    private String date;
    private boolean read_sp;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isRead_sp() {
        return read_sp;
    }

    public void setRead_sp(boolean read_sp) {
        this.read_sp = read_sp;
    }
}
