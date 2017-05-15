package com.example.util;

import java.text.NumberFormat;
import java.util.Calendar;

/**
 * Created by mqm on 2017/5/15.
 */
public class DateUtil {
    public static String simpleDate(){
        String simpleDate = "";
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
        return String.format("%02d", year)+ String.format("%02d", month)+String.format("%02d", day);
    }
}
