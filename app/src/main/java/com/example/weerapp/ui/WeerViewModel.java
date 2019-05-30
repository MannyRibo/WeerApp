package com.example.weerapp.ui;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.weerapp.model.WeerObject;

import java.util.List;

public class WeerViewModel extends AndroidViewModel {

    private WeerObjectRepository mRepository;

    private LiveData<List<WeerObject>> mWeerobjects;

    public WeerViewModel(@NonNull Application application) {
        super(application);
        mRepository = new WeerObjectRepository(application.getApplicationContext());
        mWeerobjects = mRepository.getAllWeerObjects();
    }

    public LiveData<List<WeerObject>> getWeerObjects() {
        return mWeerobjects;
    }

    public void insert(WeerObject weerObject) {
        mRepository.insert(weerObject);
    }

    public void update(WeerObject weerObject) {
        mRepository.update(weerObject);
    }

    public void delete(WeerObject weerObject) {
        mRepository.delete(weerObject); }

    public void deleteAll() {
        mRepository.deleteAll(); }

    }
