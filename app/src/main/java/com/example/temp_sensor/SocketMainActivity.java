package com.example.temp_sensor;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class SocketMainActivity extends AppCompatActivity {

    public Socket socket;
    SocketMainActivity socketMainActivity;
    AlertDialog.Builder builder;
    AlertDialog con;
    Timer timer;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_main);

        socketMainActivity = this;

        Intent intent = getIntent();
        String ip = intent.getStringExtra("ip");
        String port = intent.getStringExtra("port");

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
/*
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
91 190
            }
        };
        timer.schedule(timerTask, 1000);
 */

        builder = new MaterialAlertDialogBuilder(socketMainActivity);
        con = builder.create();
        SocketConnect connect = new SocketConnect(socketMainActivity, ip, port, socket, builder);
        connect.start();

        try {
            connect.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(con != null && con.isShowing())
            con.dismiss();
    }
}


