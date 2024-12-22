package com.example.cuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);
        // Lấy đối tượng ImageView của logo
        ImageView splashLogo = findViewById(R.id.splash_logo);

        // Tạo hiệu ứng chuyển động từ dưới lên
        Animation animation = new TranslateAnimation(0, 0, 500, 0); // Di chuyển từ dưới lên
        animation.setDuration(1200); // Thời gian hiệu ứng (2 giây)
        animation.setFillAfter(true); // Giữ nguyên trạng thái sau khi hiệu ứng kết thúc

        // Áp dụng hiệu ứng cho logo
        splashLogo.startAnimation(animation);
        // Tạo một Handler để delay 3 giây trước khi chuyển đến MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Chuyển đến MainActivity sau 3 giây
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();  // Đóng SplashActivity để người dùng không quay lại
            }
        }, 3000);  // 3000ms = 3 giây
    }
}