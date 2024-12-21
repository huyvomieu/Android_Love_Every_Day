package com.example.cuoiky.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cuoiky.entity.UserPair;

@Dao
public interface UserPairDAO {
    @Query("SELECT COUNT(*) FROM userpair")
    int count();
    @Query("SELECT  * FROM userpair limit(1)")
    UserPair get();
    @Query("UPDATE userpair SET male = :maleName ")
    void updateMale(String maleName);
    @Query("UPDATE userpair SET female = :feMaleName ")
    void updateFemale(String feMaleName);
    @Insert
    void insert(UserPair userpair);
}
