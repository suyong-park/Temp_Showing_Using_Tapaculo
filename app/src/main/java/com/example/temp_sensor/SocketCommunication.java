package com.example.temp_sensor;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

class SocketCommunication extends Thread {

    Socket socket;
    SocketMainActivity activity;

    String value = "";

    public SocketCommunication(Socket socket, SocketMainActivity activity) {
        this.socket = socket;
        this.activity = activity;
    }

    public void run() {

        try {

            //String OutData = "ATDC\n\r";
            String OutData = "ATDC\r\n";
            char ascii_char;
            int ascii;
            int count = 0;

            while(true) {
                count += 1;

                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject(OutData);
                outputStream.flush();

                InputStream inputStream = socket.getInputStream();
                ascii = inputStream.read();

                /*
                if(ascii == 13) {
                    try {
                        if(socket != null)
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                 */

                if(count > 105) {

                    try {
                        if(socket != null)
                            socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    boolean isConnect = !socket.isClosed() && socket.isConnected();
                    if(isConnect)
                        System.out.println("Socket 연결됨");
                    else
                        System.out.println("Socket 닫힘");
                    break;
                }

                ascii_char = new Character((char) ascii);
                value += String.valueOf(ascii_char);
            }
            System.out.println("아스키 코드 변환 값 : " + value);

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("소켓 통신 종료 시점");
        //currentThread().interrupt();
    }
}
