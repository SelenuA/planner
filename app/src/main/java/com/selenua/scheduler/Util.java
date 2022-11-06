package com.selenua.scheduler;

import android.util.Log;

import java.util.Calendar;

public class Util {
    public static String calendarToString(Calendar cal) {
        return String.format("%02d/%02d/%04d", cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.YEAR));
    }
    public static Calendar stringToCalendar(String str){
        int start=0;
        int end=0;
        while(str.charAt(end)!='/'){
            ++end;
        }
        int month;
        try{
            month = Integer.parseInt(str.substring(start,end));
        }catch(NumberFormatException e){
            month = 1;
        }
        start=end+1;
        end++;
        while(str.charAt(end)!='/'){
            ++end;
        }
        int day;
        try{
            day = Integer.parseInt(str.substring(start,end));
        }catch(NumberFormatException e){
            day = 1;
        }
        start=end+1;
        end=str.length();
        int year;
        try{
            year = Integer.parseInt(str.substring(start,end));
        }catch(NumberFormatException e){
            year = 1970;
        }
        Calendar cal = Calendar.getInstance();
        cal.set(year,month-1,day);
        return cal;
    }
    public static boolean isDateQuicker(Calendar first, Calendar next) {
        if(first.get(Calendar.YEAR) < next.get(Calendar.YEAR)){
            return true;
        }
        else if(first.get(Calendar.YEAR) == next.get(Calendar.YEAR) &&
                first.get(Calendar.MONTH) < next.get(Calendar.MONTH)){
            return true;
        }
        else if(first.get(Calendar.YEAR) == next.get(Calendar.YEAR) &&
                first.get(Calendar.MONTH) == next.get(Calendar.MONTH) &&
                first.get(Calendar.DAY_OF_MONTH) <= next.get(Calendar.DAY_OF_MONTH)){
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean checkDate(Calendar cal, Calendar first, Calendar last){
        if(isDateQuicker(first,cal) && isDateQuicker(cal,last)){
            return true;
        }
        else{
            return false;
        }
    }
}
