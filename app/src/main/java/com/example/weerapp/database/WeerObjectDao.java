package com.example.weerapp.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.weerapp.model.WeerObject;

import java.util.List;

@Dao
public interface WeerObjectDao {

    @Query("SELECT * FROM weerobject")
    LiveData<List<WeerObject>> getAllWeerObjects();

    @Insert
    void insertWeerObject(WeerObject weerobject);

    @Delete
    void deleteWeerObject(WeerObject weerobject);

    @Query("DELETE FROM weerobject")
    void deleteAll();

    @Update
    void updateWeerObject(WeerObject weerobject);
}
