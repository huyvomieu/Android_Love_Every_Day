package com.example.cuoiky;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cuoiky.adapter.CustomNotificationAdapter;
import com.example.cuoiky.database.DatabaseApp;
import com.example.cuoiky.entity.Notification;

import java.nio.file.Files;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {
    TextView back3;
    ListView lv_notifi;
    CustomNotificationAdapter adp;
    List<Notification> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        getWidGet();
        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setListView();
    }

    private void setListView() {
        list = DatabaseApp.getInstance(this).notificationDAO().getList();
        adp = new CustomNotificationAdapter(NotificationActivity.this,list);
        lv_notifi.setAdapter(adp);
    }

    private void getWidGet() {
        back3 = findViewById(R.id.back3);
        lv_notifi = findViewById(R.id.lv_notifi);
    }
}