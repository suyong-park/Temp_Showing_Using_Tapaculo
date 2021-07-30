package com.dekist.radionodepanel;

import android.os.CountDownTimer;

public class CloudTimer extends CountDownTimer {
    // 스레드 역할 대행

    String api_key_str;
    String api_secret_str;
    String mac_str;

    Connect_Tapaculo tapaculo;
    CloudMainActivity activity;

    public CloudTimer(long millisInFuture, long countDownInterval, CloudMainActivity activity, Connect_Tapaculo tapaculo, String api_key_str, String api_secret_str, String mac_str) {
        super(millisInFuture, countDownInterval);
        this.tapaculo = tapaculo;
        this.api_key_str = api_key_str;
        this.api_secret_str = api_secret_str;
        this.mac_str = mac_str;
        this.activity = activity;
    }

    @Override
    public void onTick(long millisUntilFinished) { // Timer가 호출됐을 때 countDownInterval 값을 주기로 반복 호출됨.
        CloudCommunication connect = new CloudCommunication(activity, tapaculo, api_key_str, api_secret_str, mac_str);
        connect.requestHttp();
    }

    @Override
    public void onFinish() { // Timer가 종료됐을 때 호출되는 함수
        System.out.println("타이머 종료됨");
    }
}
