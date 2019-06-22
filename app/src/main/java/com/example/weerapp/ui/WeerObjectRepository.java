package com.example.weerapp.ui;

import android.arch.lifecycle.LiveData;
import android.content.Context;

import com.example.weerapp.database.WeerObjectDao;
import com.example.weerapp.database.WeerObjectRoomDatabase;
import com.example.weerapp.model.WeerObject;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WeerObjectRepository {

    private WeerObjectRoomDatabase mAppDatabase;

    private WeerObjectDao mWeerObjectDao;

    private LiveData<List<WeerObject>> mWeerObjects;

    private LiveData<Integer> rowCount;

    private Executor mExecutor = Executors.newSingleThreadExecutor();

    public WeerObjectRepository (Context context) {

        mAppDatabase = WeerObjectRoomDatabase.getDatabase(context);

        mWeerObjectDao = mAppDatabase.weerObjectDao();

        mWeerObjects = mWeerObjectDao.getAllWeerObjects();

        rowCount = mWeerObjectDao.getRowCount();

    }

    public LiveData<List<WeerObject>> getAllWeerObjects() {
        return mWeerObjects;
    }

    public void insert(final WeerObject weerObject) {

        mExecutor.execute(new Runnable() {

            @Override
            public void run() {
                mWeerObjectDao.insertWeerObject(weerObject);
            }
        });

    }

    public void update(final WeerObject weerObject) {

        mExecutor.execute(new Runnable() {

            @Override
            public void run() {
                mWeerObjectDao.updateWeerObject(weerObject);
            }
        });
    }


    public void delete(final WeerObject weerObject) {

        mExecutor.execute(new Runnable() {

            @Override
            public void run() {
                mWeerObjectDao.deleteWeerObject(weerObject);
            }

        });
    }

    public void deleteAll() {

        mExecutor.execute(new Runnable() {

            @Override
            public void run() {
                mWeerObjectDao.deleteAll();
            }
        });
    }

    public LiveData<Integer> getRowCount() {
        return rowCount;
    }
}