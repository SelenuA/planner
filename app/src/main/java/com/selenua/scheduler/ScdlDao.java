package com.selenua.scheduler;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ScdlDao
{
    @Insert(onConflict = REPLACE)
    void insert(ScdlData scdlData);

    @Delete
    void delete(ScdlData scdlData);

    @Delete
    void reset(List<ScdlData> scdlData);

    @Query("UPDATE schedule_list SET title = :sTitle,  info = :sInfo,  startDate = :cStartDate, endDate = :cEndDate WHERE ID = :sID")
    void update(int sID, String sTitle, String sInfo, String cStartDate, String cEndDate);

    @Query("SELECT * FROM schedule_list")
    List<ScdlData> getAll();
}