package com.example.cuoiky;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cuoiky.adapter.CustomMemoriesAdapter;
import com.example.cuoiky.database.DatabaseApp;
import com.example.cuoiky.entity.Memories;

import java.util.List;

public class ShareMemoriesActivity extends AppCompatActivity {
    Button btn_add;
    TextView back;
    ListView lv_memories;
    List<Memories> list;
    CustomMemoriesAdapter adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_share_memories);
        getwidGet();
        setListView();
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShareMemoriesActivity.this, AddMemoriesActivity.class);
                startActivityForResult(intent,1);
            }
        });
        registerForContextMenu(lv_memories);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lv_memories) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu_memories, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position; // Vị trí của item được nhấn giữ

        if(item.getItemId() == R.id.action_delete) {
//            String id = lv_memories.g
            int id = list.get(position).getId();
            DatabaseApp.getInstance(ShareMemoriesActivity.this).memoriesDAO().delete(id);
            list = DatabaseApp.getInstance(ShareMemoriesActivity.this).memoriesDAO().getListMemories();
            setListView();
            Toast.makeText(this, "Xoa thanh cong!", Toast.LENGTH_SHORT).show();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            list = DatabaseApp.getInstance(ShareMemoriesActivity.this).memoriesDAO().getListMemories();
            adp = new CustomMemoriesAdapter(ShareMemoriesActivity.this, list);
            lv_memories.setAdapter(adp);
    }

    private void setListView() {
        list = DatabaseApp.getInstance(ShareMemoriesActivity.this).memoriesDAO().getListMemories();
        if(list.size() > 0) {
            adp = new CustomMemoriesAdapter(ShareMemoriesActivity.this,list);
            lv_memories.setAdapter(adp);
        }
    }
    void getwidGet() {
        btn_add = findViewById(R.id.btn_add_memory);
        lv_memories = findViewById(R.id.lv_memories);
        back = findViewById(R.id.back);
    }
}