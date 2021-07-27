package com.example.temp_sensor;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketMainActivity extends AppCompatActivity {

    SocketMainActivity activity;
    private Socket socket;

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

        SocketConnect sc = new SocketConnect(activity, ip, port);
        sc.start();
    }

    class SocketConnect extends Thread {

        Activity activity;

        String ip_str;
        String port_str;

        public SocketConnect(Activity activity, String ip_str, String port_str) {
            this.activity = activity;
            this.ip_str = ip_str;
            this.port_str = port_str;
        }

        public void run() {
            int port = Integer.parseInt(port_str);

            try {
                socket = new Socket(ip_str, port);

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "연결됨", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (UnknownHostException e) { // 소켓 생성시 전달되는 호스트의 IP를 식별할 수 없음.
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "HOST IP 식별 불가", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IOException e) { // 소켓 생성 과정에서 IO Error 발생. 주로 네트워크 응답 없음.
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "IO Error, 네트워크 무응답", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (SecurityException e) { // Security Manager에서 허용되지 않은 기능 수행됨.
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "보안 문제.", Toast.LENGTH_LONG).show();
                    }
                });
            } catch (IllegalArgumentException e) { // 소켓 생성시 전달되는 포트 번호가 허용 범위를 벗어남 (0 ~ 65535)
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "포트 범위 벗어남", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }

    class SocketStart extends Thread {

    }

    @Override
    protected void onStop() {
        super.onStop();

        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


