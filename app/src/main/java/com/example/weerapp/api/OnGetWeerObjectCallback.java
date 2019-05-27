package com.example.weerapp.api;

import com.example.weerapp.model.Main;
import com.example.weerapp.model.WeerObject;

public interface OnGetWeerObjectCallback {

    void onSuccess(WeerObject weerObject);

    void onError();
}
