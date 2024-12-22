package com.example.cuoiky;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cuoiky.database.DatabaseApp;
import com.example.cuoiky.entity.Memories;
import com.example.cuoiky.util.MemoriesReminder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

public class AddMemoriesActivity extends AppCompatActivity {
    private static final int  PICK_IMAGE_REQUEST = 1;
    ImageView img_Choose;
    EditText edt_Title;
    TextView txt_addDate, txt_title_Img, back2;
    Button btn_save;
    CountDayLove countDayLove = new CountDayLove();
    private  static int ID_IMAGE = 1;
    Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_memories);
        getWidGet();
        txt_addDate.setText(countDayLove.getStringNow());
        txt_addDate.setOnClickListener(view -> showDatePickerDialog());
        img_Choose.setOnClickListener(view -> chooseImg());
        btn_save.setOnClickListener(view -> saveMemories());
        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    void saveMemories() {
        try {
            String title = edt_Title.getText().toString();
            String date = txt_addDate.getText().toString(); // Lay ngày kỉ niệm
            // Lưu tên file ảnh ngẫu nhiên với thời gian hiện tại
            String nameImage = "Image" + System.currentTimeMillis() + ".jpg";
            // Lấy uri Upload lên
            String imgUri = saveFileFromUri(imageUri, nameImage);
            if(title.length() == 0) {
                Toast.makeText(this, "Vui lòng nhập tiêu đề yêu", Toast.LENGTH_SHORT).show();
                return;
            }
            if(date.length() == 0) {
                Toast.makeText(this, "Vui lòng nhập ngày kỉ niệm", Toast.LENGTH_SHORT).show();
                return;
            }
            if(imgUri == null) {
                Toast.makeText(this, "Vui lòng tải ảnh kỉ niệm lên!", Toast.LENGTH_SHORT).show();
                return;
            }
            Memories m = new Memories(title,date,imgUri);
            DatabaseApp.getInstance(AddMemoriesActivity.this).memoriesDAO().insert(m);
            Toast.makeText(this, "Thêm kỉ niệm thành công", Toast.LENGTH_SHORT).show();
            // Cài đặt ngày thông báo kỷ niệm
            MemoriesReminder.setMemoriesReminder(AddMemoriesActivity.this,m.getId(), m.getDate(),m.getTitle());
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent); // Gửi kết quả và trạng thái OK
            finish(); // Quay lại ShareMemoriesActivity
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
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
//                    // selectedMonth + 1 vì tháng bắt đầu từ 0
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    txt_addDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }
    private void chooseImg() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            img_Choose.setImageURI(imageUri);
            txt_title_Img.setText("");
        }
    }
    private void getWidGet() {
        img_Choose = findViewById(R.id.img_Choose);
        edt_Title = findViewById(R.id.edtAddTitle);
        txt_addDate = findViewById(R.id.txt_AddDate);
        txt_title_Img = findViewById(R.id.txt_title_Img);
        btn_save = findViewById(R.id.btnSave);
        back2 = findViewById(R.id.back3);
    }
    private String saveFileFromUri(Uri uri, String fileName) {
        try {
            // Mở InputStream từ URI
            InputStream inputStream = getContentResolver().openInputStream(uri);

            // Tạo file trong bộ nhớ của ứng dụng
            File file = new File(getFilesDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);

            // Sao chép dữ liệu
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Đóng stream
            inputStream.close();
            outputStream.close();
            return file.getAbsolutePath(); // Trả về đường dẫn của tệp
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Nếu xảy ra lỗi, trả về null
        }
    }
}