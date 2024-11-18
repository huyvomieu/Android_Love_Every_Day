package com.example.cuoiky.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "userpair")
public class UserPair {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String male;
    private String female;
    private String startDate;

    public UserPair(String male, String female, String startDate) {
        this.male = male;
        this.female = female;
        this.startDate = startDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFemale() {
        return female;
    }

    public void setFemale(String female) {
        this.female = female;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }
}
