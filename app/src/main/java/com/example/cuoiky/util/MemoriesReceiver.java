package com.example.cuoiky.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.cuoiky.NotificationActivity;
import com.example.cuoiky.R;
import com.example.cuoiky.database.DatabaseApp;
import com.example.cuoiky.entity.Notification;

public class MemoriesReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Tạo thông báo
        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        int id = intent.getIntExtra("id", 0);
        String contentText = "Ngày kỉ niệm '" + title + "' '" + time + "'" + "của bạn đã đến. Hãy chuẩn bị đi hẹn hò thuiiiii!";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "memories_channel")
                .setSmallIcon(R.drawable.baseline_alarm_24) // Icon thông báo
                .setContentTitle("Đã đến ngày kỉ niệm " + "'" + title + "'" + " !")
                .setContentText(contentText)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // PendingIntent để mở ứng dụng khi nhấn vào thông báo
        Intent appIntent = new Intent(context, NotificationActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_IMMUTABLE );
        builder.setContentIntent(pendingIntent);

        // Hiển thị thông báo
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
        Notification n = new Notification();
        n.setContent(contentText);
        DatabaseApp.getInstance(context).notificationDAO().insert(n);

    }
}
