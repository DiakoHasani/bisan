package com.example.bisan.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTool {

    public static Date ConvertStringToDate(String str){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static long GetDurationFromNow(Date date){
        Calendar calendar=Calendar.getInstance();
        Date dateNow=calendar.getTime();

        long diff = dateNow.getTime() - date.getTime();
        long seconds = diff / 1000;
        long minutes = seconds / 60;
        return minutes / 60;//IN HOUR
    }

    public static String GetCurrentDateTime(){
        String str="";
        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR),
                month=calendar.get(Calendar.MONTH)+1,
                day=calendar.get(Calendar.DAY_OF_MONTH),
                hour=calendar.get(Calendar.HOUR_OF_DAY),
                minute=calendar.get(Calendar.MINUTE),
                milisecond=calendar.get(Calendar.MILLISECOND);

        str=str+year;
        if(month>9) str=str+"-"+month; else str=str+"-0"+month;
        if(day>9) str=str+"-"+day; else str=str+"-0"+day;
        if(hour>9) str=str+" "+hour; else str=str+" 0"+hour;
        if(minute>9) str=str+":"+minute; else str=str+":0"+minute;
        str=str+":"+milisecond;

        return str;
    }

}
