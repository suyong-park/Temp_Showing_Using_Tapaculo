package com.example.temp_sensor;

import android.os.CountDownTimer;

public class Timer extends CountDownTimer {
    // 스레드 역할 대행

    public Timer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {

    }
}
