package com.example.cuoiky.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cuoiky.dao.MemoriesDAO;
import com.example.cuoiky.dao.NotificationDAO;
import com.example.cuoiky.dao.UserPairDAO;
import com.example.cuoiky.entity.Memories;
import com.example.cuoiky.entity.Notification;
import com.example.cuoiky.entity.UserPair;

@Database(entities = {UserPair.class, Memories.class, Notification.class}, version = 1)
public abstract class DatabaseApp extends RoomDatabase {
    private static final String DATABASE_NAME = "love_every_day.db";
    private static DatabaseApp instance;

    public static synchronized DatabaseApp getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), DatabaseApp.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
    public  abstract UserPairDAO userPaierDao();
    public  abstract MemoriesDAO memoriesDAO();
    public  abstract NotificationDAO notificationDAO();

}

