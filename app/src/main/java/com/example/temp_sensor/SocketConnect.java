package com.example.temp_sensor;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketConnect extends Thread {

    AlertDialog.Builder builder;
    Socket socket;
    SocketMainActivity activity;

    String ip_str;
    String port_str;

    public SocketConnect(SocketMainActivity activity, String ip_str, String port_str, Socket socket, AlertDialog.Builder builder) {
        this.activity = activity;
        this.ip_str = ip_str;
        this.port_str = port_str;
        this.socket = socket;
        this.builder = builder;
    }

    public void run() {
        try {
            int port = Integer.parseInt(port_str);
            socket = new Socket(ip_str, port);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("연결됨");
                    SocketCommunication communication = new SocketCommunication(socket, activity);
                    communication.start();
                }
            });

        } catch (UnknownHostException e) { // 소켓 생성시 전달되는 호스트의 IP를 식별할 수 없음.
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("HOST IP 식별 불가");
                    e.printStackTrace();
                    builder.setTitle("경고")
                            .setMessage("소켓 생성시 전달되는 호스트의 IP를 식별할 수 없습니다.\nIP를 다시 확인해 주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    activity.finish();
                                }
                            })
                            .show();
                }
            });
        } catch (IOException e) { // 소켓 생성 과정에서 IO Error 발생. 주로 네트워크 응답 없음.
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("IO Error, 네트워크 무응답");
                    e.printStackTrace();
                    builder.setTitle("경고")
                            .setMessage("네트워크 응답이 없습니다.\n디바이스의 상태를 체크하세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    activity.finish();
                                }
                            })
                            .show();
                }
            });
        } catch (SecurityException e) { // Security Manager에서 허용되지 않은 기능 수행됨.
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("보안 문제.");
                    e.printStackTrace();
                    builder.setTitle("경고")
                            .setMessage("Security Manager에서 허용되지 않은 기능이 수행되었습니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    activity.finish();
                                }
                            })
                            .show();
                }
            });
        } catch (IllegalArgumentException e) { // 소켓 생성시 전달되는 포트 번호가 허용 범위를 벗어남 (0 ~ 65535)
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("포트 범위 벗어남");
                    e.printStackTrace();
                    builder.setTitle("경고")
                            .setMessage("소켓 생성시 전달되는 포트 번호가 허용 범위를 벗어났습니다.\n포트 허용 구간은 0 ~ 65535 입니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    activity.finish();
                                }
                            })
                            .show();
                }
            });
        }
    }
}