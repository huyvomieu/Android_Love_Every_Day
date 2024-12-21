package com.example.cuoiky;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.cuoiky.util.MemoriesReceiver;
import com.example.cuoiky.util.SendMessageReceiver;

import java.util.ArrayList;
import java.util.List;

public class SendMessageActivity extends AppCompatActivity {
    TextView back1;
    Button btn_gui;
    EditText edt_message;
    ListView lv;
    ArrayAdapter<String> adp;
    List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_send_message);
        getWidget();
        back1.setOnClickListener(v -> finish());
        btn_gui.setOnClickListener(v -> sendMessage());
        adp = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,list);
        lv.setAdapter(adp);
    }

    private void sendMessage() {
        String text = edt_message.getText().toString();
        if(text.length() == 0) {
            return;
        }
        Intent intent = new Intent(this, SendMessageReceiver.class); // Receiver để nhận thông báo
        int id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_MUTABLE);
        // Tạo thông báo
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "memories_channel")
                .setSmallIcon(R.drawable.baseline_favorite_24) // Icon thông báo
                .setContentTitle("Tin nhắn mới") // Tiêu đề
                .setContentText(text) // Nội dung
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Mức ưu tiên
                .setContentIntent(pendingIntent) // Intent khi nhấn vào thông báo
                .setAutoCancel(true); // Tự động hủy khi người dùng nhấn

        // Hiển thị thông báo
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
        edt_message.setText("");
        list.add(text);
        adp.notifyDataSetChanged();
    }

    private void getWidget() {
        back1 = findViewById(R.id.back3);
        edt_message = findViewById(R.id.edt_message);
        btn_gui = findViewById(R.id.btn_gui);
        lv = findViewById(R.id.lv_message);
    }
}