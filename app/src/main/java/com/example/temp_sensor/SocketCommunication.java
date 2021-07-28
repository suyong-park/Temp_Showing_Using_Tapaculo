package com.example.temp_sensor;

import android.app.Activity;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketCommunication extends Thread {

    private Socket socket;
    Activity activity;

    String ip_str;
    String port_str;

    public SocketCommunication(Activity activity, String ip_str, String port_str) {
        this.activity = activity;
        this.ip_str = ip_str;
        this.port_str = port_str;
    }

    public void run() {
        try {
            int port = Integer.parseInt(port_str);
            socket = new Socket(ip_str, port);

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("연결됨");
                    Toast.makeText(activity, "연결됨", Toast.LENGTH_LONG).show();

                    try {
                        String OutData = "ATDC\r\n";
                        OutputStreamWriter outputStream = new OutputStreamWriter(socket.getOutputStream(), "EUC-KR");
                        outputStream.write(OutData);
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SocketSendData socketSendData = new SocketSendData();
                    socketSendData.start();
                }
            });
        } catch (UnknownHostException e) { // 소켓 생성시 전달되는 호스트의 IP를 식별할 수 없음.
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("HOST IP 식별 불가");
                    e.printStackTrace();
                    Toast.makeText(activity, "HOST IP 식별 불가", Toast.LENGTH_LONG).show();
                }
            });
        } catch (IOException e) { // 소켓 생성 과정에서 IO Error 발생. 주로 네트워크 응답 없음.
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("IO Error, 네트워크 무응답");
                    e.printStackTrace();
                    Toast.makeText(activity, "IO Error, 네트워크 무응답", Toast.LENGTH_LONG).show();
                }
            });
        } catch (SecurityException e) { // Security Manager에서 허용되지 않은 기능 수행됨.
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("보안 문제.");
                    e.printStackTrace();
                    Toast.makeText(activity, "보안 문제.", Toast.LENGTH_LONG).show();
                }
            });
        } catch (IllegalArgumentException e) { // 소켓 생성시 전달되는 포트 번호가 허용 범위를 벗어남 (0 ~ 65535)
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("포트 범위 벗어남");
                    e.printStackTrace();
                    Toast.makeText(activity, "포트 범위 벗어남", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    class SocketSendData extends Thread {

        public SocketSendData() {
            //s1 = (TextView) findViewById(R.id.s1);
            //s2 = (TextView) findViewById(R.id.s2);
        }

        public void run() {

            try {
                String OutData = "ATDC\n\r";
                char ascii_char;
                int ascii;
                String value = "";

                while(true) {

                    ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                    outputStream.writeObject(OutData);
                    outputStream.flush();

                    InputStream inputStream = socket.getInputStream();
                    ascii = inputStream.read();

                    if(ascii == 13) {
                        System.out.println("들어옴?");
                        try {
                            if(socket != null)
                                socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                    ascii_char = new Character((char) ascii);
                    value += String.valueOf(ascii_char);
                }
                System.out.println("아스키 코드 변환 값 : " + value);

                //s1.setText(return_value);
                //s2.setText(value2);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}