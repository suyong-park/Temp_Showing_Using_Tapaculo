package com.example.temp_sensor;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;

public class SocketMainActivity extends AppCompatActivity {

    SocketMainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_main);

        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        String port = intent.getStringExtra("port");
        activity = this;

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        SocketCommunication sc = new SocketCommunication(activity, ip, port);
        sc.start();
    }
}
