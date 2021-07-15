package com.example.temp_sensor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Request {

    static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Connect_Tapaculo.Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
