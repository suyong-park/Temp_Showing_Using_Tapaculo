package com.example.temp_sensor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SocketVerifyActivity extends AppCompatActivity {

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

}
