package com.mz.bf.data;

import com.mz.bf.addbill.FatoraDetail;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {FatoraDetail.class},version = 2,exportSchema = false)
public abstract class DatabaseClass extends RoomDatabase {
    public abstract Dao getDao();
}
