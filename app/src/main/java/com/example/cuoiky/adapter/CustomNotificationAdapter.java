package com.example.cuoiky.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cuoiky.R;
import com.example.cuoiky.entity.Memories;
import com.example.cuoiky.entity.Notification;

import java.util.List;

public class CustomNotificationAdapter extends BaseAdapter {
    Context context;
    List<Notification> list;

    public CustomNotificationAdapter(Context context, List<Notification> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_notification, viewGroup, false);
        }
        TextView txt_item_notifi = convertView.findViewById(R.id.txt_item_notifi);
        Notification n = list.get(i);
        txt_item_notifi.setText(n.getContent());
        return convertView;
    }
}
