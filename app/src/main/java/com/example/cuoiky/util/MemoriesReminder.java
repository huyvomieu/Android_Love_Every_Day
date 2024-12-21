package com.example.cuoiky.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

public class MemoriesReminder {
    public static void setMemoriesReminder(Context context, int id, String memoriesDay, String title) {
        try {
            // Chuyển đổi ngày kỉ niệm thành đối tượng Calendar
            String[] DateParts = memoriesDay.split("/");
            int year = Integer.parseInt(DateParts[2]);
            int month = Integer.parseInt(DateParts[1]) - 1; // Tháng trong Calendar bắt đầu từ 0
            int day = Integer.parseInt(DateParts[0]);

            Calendar memoriesCalendar = Calendar.getInstance();
            memoriesCalendar.set(Calendar.YEAR, year);
            memoriesCalendar.set(Calendar.MONTH, month);
            memoriesCalendar.set(Calendar.DAY_OF_MONTH, day);

            // Đặt ngày kỉ niệm cho năm hiện tại
            Calendar currentCalendar = Calendar.getInstance();
            memoriesCalendar.set(Calendar.YEAR, currentCalendar.get(Calendar.YEAR));

            // Nếu ngày kỉ niệm đã qua thì lên lịch cho năm sau
            if (memoriesCalendar.before(currentCalendar)) {
                memoriesCalendar.add(Calendar.YEAR, 1);
            }

            Intent intent = new Intent(context, MemoriesReceiver.class); // Receiver để nhận thông báo
            intent.putExtra("title", title);
            intent.putExtra("time", memoriesDay);
            intent.putExtra("id", id);
            // Sử dụng id của memories làm ID cho thông báo
            int REQUEST_CODE = id;

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, PendingIntent.FLAG_MUTABLE);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Log.e("test", memoriesDay);
            // Set AlarmManager
            if (alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, memoriesCalendar.getTimeInMillis(), pendingIntent);
            }
        } catch (Exception e) {
            Log.e("MemoriesReceiver", "Error setting memories reminder", e);
        }
    }
}
