package com.project.syz.account_management.database;

import com.project.syz.account_management.R;

/**
 * Created by Roger on 2016/4/8.
 */
public class DateText {
    private String date;
    private String text;
    private String type;

    public static int[] images= new int[]{
        R.drawable.icon_living, R.drawable.icon_tuition,
                R.drawable.icon_appliance, R.drawable.icon_traffic,
                R.drawable.icon_repay,R.drawable.icon_medicine,
                R.drawable.icon_food,R.drawable.icon_recreation,
                R.drawable.icon_gift,R.drawable.icon_others};

    public DateText() {

    }

    public DateText(String date, String text,String type) {
        super();
        this.date = date;
        this.text = text;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
