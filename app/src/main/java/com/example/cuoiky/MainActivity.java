package com.example.cuoiky;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.cuoiky.database.DatabaseApp;
import com.example.cuoiky.entity.UserPair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    TextView txt_dem,txt_user1, txt_user2;
    ImageView img_user1, img_user2;
    UserPair userpair = null;
    private static final int PICK_IMAGE_1 = 1;
    private static final int PICK_IMAGE_2 = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        // Lấy ImageView từ layout
        ImageView avatarImageView = findViewById(R.id.avatarImageView);
        // Thiết lập ảnh từ mipmap cho ImageView
        avatarImageView.setImageResource(R.mipmap.ic_launcher);
        // Kiểm tra trong database có thông tin chưa
        int isCheckUser = DatabaseApp.getInstance(MainActivity.this).userPaierDao().count();
        if(isCheckUser == 0) {
            Intent intent = new Intent(MainActivity.this, add_userActivity.class );
            startActivity(intent);
            finish();
        }
        getWidget();
        // Đổ data vào view
        UserPair user = DatabaseApp.getInstance(MainActivity.this).userPaierDao().get();
        userpair = user;
        txt_user1.setText(user.getMale());
        txt_user2.setText(user.getFemale());
        CountDayLove counter = new CountDayLove();

        String content = "Đang yêu \n" + counter.countDay(this) + "\n" + "ngày";
        txt_dem.setText(content);
        // Sửa data
        img_user1.setOnClickListener(view -> openImageChooser(PICK_IMAGE_1));
        img_user2.setOnClickListener(view -> openImageChooser(PICK_IMAGE_2));
        txt_user1.setOnClickListener(view -> openInputDialog(1,"nick name 1"));
        txt_user2.setOnClickListener(view ->  openInputDialog(2,"nick name 2"));
        // Load ảnh lên view nếu có
        loadSavedImages();

    }
    private void openInputDialog(int i,String nickname) {
        // Tạo EditText để nhập dữ liệu
        EditText edtName = new EditText(this);
        if(i == 1) {
            edtName.setText(txt_user1.getText().toString());
            edtName.setSelection(edtName.getText().length()); // Đặt con trỏ ở cuối văn bản
        }
        else {
            edtName.setText(txt_user2.getText().toString());
        }
        // Tạo Dialog
        AlertDialog.Builder builder  = new AlertDialog.Builder(this);
        builder.setTitle("Nhập nick name " + i);
        builder.setMessage("Vui lòng nhập tên người thương nhớ:");

        // Thêm EditText vào Dialog
        builder.setView(edtName);
        // Thêm sự kiện để bôi đen văn bản khi hộp thoại hiển thị
        edtName.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                edtName.selectAll(); // Bôi đen toàn bộ văn bản
            }
        });
        // Nút "Lưu" để xác nhận dữ liệu
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            // Lấy dữ liệu từ EditText
            String inputData = edtName.getText().toString().trim();

            // Hiển thị dữ liệu vào TextView
            if (!inputData.isEmpty()) {
                if(i  == 1) {
                    txt_user1.setText(inputData);
                    DatabaseApp.getInstance(MainActivity.this).userPaierDao().updateMale(inputData);
                }
                else if(i == 2) {
                    txt_user2.setText(inputData);
                    DatabaseApp.getInstance(MainActivity.this).userPaierDao().updateFemale(inputData);

                }
            } else {
                // Xử lý khi người dùng không nhập gì
            }
        });
        // Nút "Hủy" để đóng Dialog
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());

        // Hiển thị Dialog
        AlertDialog dialog = builder.create();
        // Đảm bảo con trỏ tự động trỏ vào EditText khi Dialog hiển thị
        dialog.setOnShowListener(d -> {
            edtName.requestFocus();
            edtName.selectAll(); // Bôi đen toàn bộ văn bản (nếu cần)
        });

        dialog.show();

        // Mở bàn phím tự động
        edtName.postDelayed(() -> {
            edtName.requestFocus();
            showKeyboard(edtName);
        }, 100);
    }
    private void openImageChooser(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ( resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData(); // URI của ảnh được chọn
            if(requestCode == PICK_IMAGE_1) {
                img_user1.setImageURI(imageUri);
                saveImageToApp(imageUri, "avt_user1.jpg");
            } else if(requestCode == PICK_IMAGE_2) {
                img_user2.setImageURI(imageUri);
                saveImageToApp(imageUri, "avt_user2.jpg");
            }
        }
    }
    private void saveImageToApp(Uri imageUri, String fileName) {
        try {
            // Lấy input stream từ URI
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            // Tạo file trong thư mục lưu trữ của ứng dụng
            File file = new File(getFilesDir(), fileName);
            FileOutputStream outputStream = new FileOutputStream(file);

            // Sao chép dữ liệu từ inputStream sang outputStream
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            // Đóng stream
            outputStream.close();
            inputStream.close();

            Toast.makeText(this, "Ảnh đã được lưu!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lưu ảnh thất bại!", Toast.LENGTH_SHORT).show();
        }
    }
    private void loadSavedImages() {
        File file1 = new File(getFilesDir(), "avt_user1.jpg");
        if (file1.exists()) {
            img_user1.setImageBitmap(BitmapFactory.decodeFile(file1.getAbsolutePath()));
        }

        File file2 = new File(getFilesDir(), "avt_user2.jpg");
        if (file2.exists()) {
            img_user2.setImageBitmap(BitmapFactory.decodeFile(file2.getAbsolutePath()));
        }
    }
    // Phương thức mở bàn phím
    private void showKeyboard(EditText editText) {
        editText.post(() -> {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Tao menu
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toobar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.note) {
            Intent intent = new Intent(MainActivity.this, send_message_activity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    void getWidget() {
        txt_dem = findViewById(R.id.txt_dem);
        txt_user1 = findViewById(R.id.txt_user1);
        txt_user2 = findViewById(R.id.txt_user2);
        img_user1 = findViewById(R.id.avt_user1);
        img_user2 = findViewById(R.id.avt_user2);
    }

}