package com.example.cuoiky;

import android.content.Context;

import com.example.cuoiky.database.DatabaseApp;

import java.util.Calendar;

public class CountDayLove {
    private int day;
    private int month;
    private int year;
    public CountDayLove() {
        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0 nên cần +1
        year = calendar.get(Calendar.YEAR);
    }
    public String getStringNow() {
        return day + "/" + month + "/" + year;
    }

    public long countDay(Context context) {
        String date = DatabaseApp.getInstance(context).userPaierDao().get().getStartDate();
        String[] dateParts = date.split("/");
        int sDay = Integer.parseInt(dateParts[0]);
        int sMonth = Integer.parseInt(dateParts[1]);
        int sYear = Integer.parseInt(dateParts[2]);
        Calendar startDate = Calendar.getInstance();
        startDate.set(sYear, sMonth, sDay);
        Calendar endDate = Calendar.getInstance();
        endDate.set(year, month, day);
        long startMillis = startDate.getTimeInMillis();
        long endMillis = endDate.getTimeInMillis();

        long difference = endMillis -startMillis;
        long daysBetween = difference / (1000 * 60 * 60 * 24);
        return daysBetween+1;
    }
    public long countDay2(Context context,int sDay, int sMonth, int sYear) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(sYear, sMonth, sDay);
        Calendar endDate = Calendar.getInstance();
        endDate.set(year, month, day);
        long startMillis = startDate.getTimeInMillis();
        long endMillis = endDate.getTimeInMillis();

        long difference = endMillis -startMillis;
        long daysBetween = difference / (1000 * 60 * 60 * 24);
        return daysBetween+1;
    }

}
