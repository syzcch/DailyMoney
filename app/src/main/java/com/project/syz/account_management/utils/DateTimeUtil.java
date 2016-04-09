package com.project.syz.account_management.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * Created by 2 on 2016/4/8.
 */
public class DateTimeUtil {
    public static int getCurrentMonth(){
        String  dateStr,month;
        Date date;
        SimpleDateFormat sDateFormat = new  SimpleDateFormat("yyyy-MM-dd");
        date = new Date(System.currentTimeMillis());
        dateStr =  sDateFormat.format(date);
        month = dateStr.substring(5,7);
        return Integer.parseInt(month);
    }
}
