package com.example.weerapp.api;

import com.example.weerapp.BuildConfig;
import com.example.weerapp.model.WeerObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeerObjectenRepository {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather/";
    private static final String API_KEY = BuildConfig.ApiKey;
    private static final String UNITS = "metric";

    private static WeerObjectenRepository repository;

    private WeerAPI api;

    private WeerObjectenRepository(WeerAPI api) {
        this.api = api;
    }

    public static WeerObjectenRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new WeerObjectenRepository(retrofit.create(WeerAPI.class));
        }

        return repository;
    }

    public void getWeerObjectfromStadNaam(String stadNaam, final OnGetWeerObjectCallback callback) {
        api.getWeerObjectfromStadNaam(stadNaam, API_KEY, UNITS)
                .enqueue(new Callback<WeerObject>() {
                    @Override
                    public void onResponse(Call<WeerObject> call, Response<WeerObject> response) {
                        if (response.isSuccessful()) {

                            WeerObject data = response.body();

                            // random long genereren en meegeven aan het object
                            long leftLimit = 1L;
                            long rightLimit = 1000000L;
                            long generatedLong = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));

                            data.setId(generatedLong);


                        if (data != null) {
                                callback.onSuccess(data);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeerObject> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getWeerObjectfromCoordinaten(Double lat, Double lon, final OnGetWeerObjectCallback callback) {
        api.getWeerObjectfromCoordinaten(lat, lon, API_KEY, UNITS)
                .enqueue(new Callback<WeerObject>() {
                    @Override
                    public void onResponse(Call<WeerObject> call, Response<WeerObject> response) {
                        if (response.isSuccessful()) {

                            WeerObject data = response.body();

                            if (data != null) {
                                callback.onSuccess(data);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<WeerObject> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}
