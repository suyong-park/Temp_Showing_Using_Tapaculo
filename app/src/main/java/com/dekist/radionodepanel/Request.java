package com.dekist.radionodepanel;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Request {

    static Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(Connect_Tapaculo.Base_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    static void downKeyboard(Activity activity) {

        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();

        if (focusedView != null)
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}