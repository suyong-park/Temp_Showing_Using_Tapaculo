package com.example.temp_sensor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;

public class SocketVerifyActivity extends AppCompatActivity {

    SocketVerifyActivity socketVerifyActivity;
    AlertDialog.Builder builder;
    AlertDialog con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_verify);

        socketVerifyActivity = this;

        builder = new MaterialAlertDialogBuilder(socketVerifyActivity);
        con = builder.create();

        EditText ip_enter = (EditText) findViewById(R.id.ip_enter);
        EditText port_enter = (EditText) findViewById(R.id.port_enter);
        Button socket_start_btn = (Button) findViewById(R.id.start_socket_btn);

        if(PreferenceManager.getBoolean(socketVerifyActivity, "is_first_socket")) {
            ip_enter.setText(PreferenceManager.getString(socketVerifyActivity, "IP"));
            port_enter.setText(PreferenceManager.getString(socketVerifyActivity, "PORT"));
        }

        socket_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ip_enter.getText().toString().isEmpty() || port_enter.getText().toString().isEmpty()) {
                    builder.setTitle("경고")
                            .setMessage("모든 정보를 입력해 주셔야 합니다.")
                            .setPositiveButton("확인", null)
                            .show();
                    return;
                }

                PreferenceManager.setString(socketVerifyActivity, "IP", ip_enter.getText().toString());
                PreferenceManager.setString(socketVerifyActivity, "PORT", port_enter.getText().toString());
                PreferenceManager.setBoolean(socketVerifyActivity, "is_first_socket", true);

                Intent intent = new Intent(socketVerifyActivity, SocketMainActivity.class);
                intent.putExtra("ip", ip_enter.getText().toString());
                intent.putExtra("port", port_enter.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        builder.setTitle("확인")
                .setMessage("정말 선택 화면으로 나가시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setNegativeButton("취소", null)
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(con != null && con.isShowing()) {
            System.out.println("다이얼로그 종료");
            con.dismiss();
        }
    }
}
