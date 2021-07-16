package com.example.temp_sensor;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Connect_Tapaculo {
    // HTTP 통신을 위한 인터페이스 구현

    String Base_URL = "https://oa.tapaculo365.com/tp365/v1/";
    // 접속하고자 하는 Base_URL. 여기까지는 고정된 주소여야 함. GET, POST하기 위한 방식에서 추가적으로 가변적인 주소를 제공하면 됨. 아래와 같음.

    @FormUrlEncoded // 인자를 url-encoded 형태로 보낼 때 사용함. key=value&key=value와 같은 형태임.
    @POST("device/get_info") // POST 방식의 통신
    Call<GetInfo> getInfo (
            @Field("api_key") String api_key,
            @Field("api_secret") String api_secret,
            @Field("MAC") String mac
    );
}
