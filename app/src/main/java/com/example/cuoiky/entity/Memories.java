package com.example.cuoiky.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "memories")
public class Memories {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String date;
    private String image;

    public Memories() {

    }

    public Memories(String title, String date, String image) {
        this.title = title;
        this.date = date;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
