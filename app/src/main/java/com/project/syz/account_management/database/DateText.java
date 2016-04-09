package com.project.syz.account_management.database;

/**
 * Created by 2 on 2016/4/8.
 */
public class DateText {
    private String date;
    private String text;

    public DateText() {

    }

    public DateText(String date, String text) {
        super();
        this.date = date;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
