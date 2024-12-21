package com.example.cuoiky.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cuoiky.entity.Memories;

import java.util.List;

@Dao
public interface MemoriesDAO {
    @Query("SELECT * FROM memories")
    List<Memories> getListMemories();
    @Insert
    void insert(Memories m);

    @Query("DELETE FROM memories where id= :id")
    void delete(int id);

}
