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

public class SocketVerifyActivity extends AppCompatActivity {

    AlertDialog.Builder builder;
    AlertDialog con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket_verify);

        EditText ip_enter = (EditText) findViewById(R.id.ip_enter);
        EditText port_enter = (EditText) findViewById(R.id.port_enter);
        Button socket_start_btn = (Button) findViewById(R.id.start_socket_btn);

        socket_start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocketVerifyActivity.this, SocketMainActivity.class);
                intent.putExtra("ip", ip_enter.getText().toString());
                intent.putExtra("port", port_enter.getText().toString());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        builder = new MaterialAlertDialogBuilder(SocketVerifyActivity.this);
        con = builder.create();

        builder.setTitle("확인")
                .setMessage("정말 선택 화면으로 나가시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(con != null && con.isShowing())
            con.dismiss();
    }
}
