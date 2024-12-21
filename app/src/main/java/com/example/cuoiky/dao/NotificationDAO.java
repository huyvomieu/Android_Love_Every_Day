package com.example.cuoiky.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cuoiky.entity.Notification;

import java.util.List;

@Dao
public interface NotificationDAO {
    @Insert
    void insert(Notification n);
    @Query("select * from notification")
    List<Notification> getList();
    @Query("DELETE FROM notification WHERE id = :id")
    void delete(int id);
}
