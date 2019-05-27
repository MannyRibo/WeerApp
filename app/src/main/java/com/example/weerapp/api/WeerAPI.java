package com.example.weerapp.api;

import com.example.weerapp.model.Main;
import com.example.weerapp.model.WeerObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeerAPI {

    @GET(".")
    Call<WeerObject> getWeerObjectfromStadNaam(
                    @Query("q") String stadNaam,
                    @Query("appid") String apiKey,
                    @Query("units") String units
            );

    @GET(".")
    Call<WeerObject> getWeerObjectfromCoordinaten(
            @Query("lat") Double latitude,
            @Query("lon") Double longitude,
            @Query("appid") String apiKey,
            @Query("units") String units
    );
}
