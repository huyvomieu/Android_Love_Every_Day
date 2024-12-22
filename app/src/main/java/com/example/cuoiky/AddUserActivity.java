package com.example.cuoiky;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cuoiky.database.DatabaseApp;
import com.example.cuoiky.entity.UserPair;

import java.util.Calendar;

public class AddUserActivity extends AppCompatActivity {
    Button btn_setday,btn_dem;
    TextView txt_dem, txt_saveDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_user);
        btn_setday = findViewById(R.id.btn_setday);
        btn_dem = findViewById(R.id.btn_dem);
        txt_dem = findViewById(R.id.txt_dem);
        txt_saveDate = findViewById(R.id.txt_saveDate);
        btn_setday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        btn_dem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserPair pair = new UserPair("nickname 1", "nickname 2", txt_saveDate.getText().toString());
                DatabaseApp.getInstance(AddUserActivity.this).userPaierDao().insert(pair);
                Intent intent = new Intent(AddUserActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void showDatePickerDialog() {
        // Lấy ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Lưu ý: Tháng bắt đầu từ 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        // Hiển thị DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    // selectedMonth + 1 vì tháng bắt đầu từ 0
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    txt_saveDate.setText(selectedDate);
                    CountDayLove couter = new CountDayLove(); // Lớp đếm ngày yêu
                    // Đếm ngày khi truyền ngày bắt đầu yêu vào
                    long countDay = couter.countDay2(this,selectedDay,selectedMonth+1, selectedYear);
                    txt_dem.setText(countDay+"");
                }, year, month, day);
        datePickerDialog.show();
    }

}