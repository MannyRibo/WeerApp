package com.example.weerapp.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.weerapp.model.Main;
import com.example.weerapp.model.WeerObject;

@Database(entities = {WeerObject.class}, version = 1, exportSchema = false)
public abstract class WeerObjectRoomDatabase extends RoomDatabase {

    private final static String NAME_DATABASE = "weerobject_database";

    public abstract WeerObjectDao weerObjectDao();

    private static volatile WeerObjectRoomDatabase INSTANCE;

    public static WeerObjectRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeerObjectRoomDatabase.class) {
                if (INSTANCE == null) {
                    // Create database here
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            WeerObjectRoomDatabase.class, NAME_DATABASE)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
