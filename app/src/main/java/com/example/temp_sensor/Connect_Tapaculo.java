package com.example.temp_sensor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Connect_Tapaculo {

    String Base_URL = "https://oa.tapaculo365.com/tp365/v1/";

    @FormUrlEncoded
    @POST("channel/get_values")
    Call<List<GetValues>> getValues (
            @Field("api_key") String api_key,
            @Field("api_secret") String api_secret,
            @Field("sensors") String sensors
    );
}
