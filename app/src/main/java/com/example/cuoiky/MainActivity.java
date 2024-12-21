package com.example.cuoiky;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import com.example.cuoiky.database.DatabaseApp;
import com.example.cuoiky.entity.UserPair;
import com.example.cuoiky.util.MemoriesReminder;
import com.example.cuoiky.util.NotificationHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {
    TextView txt_dem,txt_user1, txt_user2;
    ImageView img_user1, img_user2;
    UserPair userpair = null;
    private static final int PICK_IMAGE_1 = 1;
    private static final int PICK_IMAGE_2 = 2;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Tạo Notification Channel để cho phép đặt thông báo (chỉ hoạt động trên Android 8.0 trở lên)
        NotificationHelper.createNotificationChannel(this);

        // Lấy ImageView từ layout
        ImageView avatarImageView = findViewById(R.id.avatarImageView);
        // Thiết lập ảnh từ mipmap cho ImageView
        avatarImageView.setImageResource(R.mipmap.ic_launcher);
        getWidget();

        // Kiểm tra trong database có thông tin chưa
        int isCheckUser = DatabaseApp.getInstance(MainActivity.this).userPaierDao().count();
        if(isCheckUser == 0) {
            Intent intent = new Intent(MainActivity.this, AddUserActivity.class );
            startActivity(intent);
            finish();
        }
        else {
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
        checkNotificationPermission();
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
        if(id == R.id.add) {
            Intent intent = new Intent(MainActivity.this, ShareMemoriesActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.note) {
            Intent intent = new Intent(MainActivity.this, SendMessageActivity.class);
            startActivity(intent);
            return true;
        }
        if(id == R.id.settings) {
            MemoriesReminder.setMemoriesReminder(MainActivity.this,90, "6/12/2023", "thaoo");
            return true;
        }
        if(id == R.id.share) {
            View v = findViewById(R.id.share);
            // Tạo Popup Menu
            PopupMenu popupMenu = new PopupMenu(MainActivity.this,v );
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.share_menu, popupMenu.getMenu());

            // Thiết lập vị trí của menu (Dưới Button)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                popupMenu.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            }

            // Xử lý sự kiện chọn mục menu
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem itemPop) {
                    int idPop = itemPop.getItemId();
                    if(idPop == R.id.save_image) {
                        takeScreenshotAndSave();
                        return true;
                    }
                    if(idPop == R.id.share_in_facebook) {
                        shareContentToFacebook();
                        return true;
                    }
                    return false;
                }
            });

            // Hiển thị menu
            popupMenu.show();
//
            return true;
        }
        if(id == R.id.notification) {
            Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareContentToFacebook() {
        String content = "";
        UserPair user = DatabaseApp.getInstance(MainActivity.this).userPaierDao().get();
        CountDayLove counter = new CountDayLove(); // hàm đếm số ngày yêu
        content += user.getFemale() + " và " + user.getMale() + " đang yêu nhau được " + counter.countDay(MainActivity.this) + " ngày!\n"
                + "Hãy tải app để tận hưởng khoảng khắc này nhé!!!\n" + "#loveeveryday";
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_TEXT, content);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.setPackage("com.facebook.katana"); // Chỉ mở Facebook
            startActivity(shareIntent);
        } catch (ActivityNotFoundException e) {
            // Nếu Facebook không được cài đặt, mở trình duyệt
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/sharer/sharer.php?u=" + Uri.encode(content)));
            startActivity(browserIntent);
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission("android.permission.POST_NOTIFICATIONS") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{"android.permission.POST_NOTIFICATIONS"}, 1);
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission("android.permission.READ_MEDIA_IMAGES") != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{"android.permission.READ_MEDIA_IMAGES"}, 100);
            }
        }


    }


    public void takeScreenshotAndSave() {
        try {
            // 1. Chụp màn hình
            View rootView = getWindow().getDecorView().getRootView();
            rootView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(rootView.getDrawingCache());
            rootView.setDrawingCacheEnabled(false);

            // 2. Lưu ảnh vào MediaStore
            ContentResolver resolver = getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "screenshot_" + System.currentTimeMillis() + ".png");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/Screenshots");

            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

            if (imageUri != null) {
                uri = imageUri;
                OutputStream outputStream = resolver.openOutputStream(imageUri);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                if (outputStream != null) outputStream.close();
                // Thông báo ảnh đã lưu
                Toast.makeText(this, "Ảnh đã được lưu vào điện thoại!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to save screenshot", Toast.LENGTH_SHORT).show();
        }
    }
    void getWidget() {
        txt_dem = findViewById(R.id.txt_dem);
        txt_user1 = findViewById(R.id.txt_user1);
        txt_user2 = findViewById(R.id.txt_user2);
        img_user1 = findViewById(R.id.avt_user1);
        img_user2 = findViewById(R.id.avt_user2);
    }

}