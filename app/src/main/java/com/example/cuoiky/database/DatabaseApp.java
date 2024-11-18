package com.example.cuoiky.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cuoiky.dao.UserPairDAO;
import com.example.cuoiky.entity.UserPair;

@Database(entities = {UserPair.class}, version = 1)
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

}

