package com.example.cuoiky.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.cuoiky.R;


public class SendMessageReceiver  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Tạo thông báo
        String text = intent.getStringExtra("title");
        int id = intent.getIntExtra("id",1);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "memories_channel")
                .setSmallIcon(R.drawable.baseline_favorite_24) // Icon thông báo
                .setContentTitle("Tin nhắn mới")
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);
        // Hiển thị thông báo
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }
}
