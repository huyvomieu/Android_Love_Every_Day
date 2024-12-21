package com.example.cuoiky.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuoiky.R;
import com.example.cuoiky.entity.Memories;

import org.w3c.dom.Text;

import java.io.File;
import java.util.List;

public class CustomMemoriesAdapter extends BaseAdapter {
    Context context;
    List<Memories> list;

    public CustomMemoriesAdapter(Context context, List<Memories> list) {
        this.context = context;
        this.list = list;
    }

    @Override

    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_memories, viewGroup, false);
        }
        ImageView img_icon = convertView.findViewById(R.id.img_icon);
        TextView txt_title = convertView.findViewById(R.id.txt_title);
        TextView txt_time = convertView.findViewById(R.id.txt_time);
        TextView txt_date = convertView.findViewById(R.id.txt_date);
        Memories m = list.get(i);
        txt_title.setText(m.getTitle());
        txt_time.setText(m.getDate());
        txt_date.setText(m.getDate());
        TextView txt_id = new TextView(context);
        txt_id.setVisibility(View.GONE);
        txt_id.setText(m.getId()+"");
        File file = new File(m.getImage());
        if (file.exists()) {
            img_icon.setImageURI(Uri.fromFile(file));
        } else {
//            Log.e("LoadFileError", "File does not exist: " + filePath);
        }
//        img_icon.setImageURI(Uri.parse(m.getImage()));
        return convertView;
    }
}
