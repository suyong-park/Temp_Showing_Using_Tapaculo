package com.dekist.radionodepanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import static androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES;
import static androidx.appcompat.app.AppCompatDelegate.setDefaultNightMode;

public class StartSelectActivity extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startselect);

        //PreferenceManager.clear(this); // 테스트 목적의 코드 라인

        Button cloud_btn = (Button) findViewById(R.id.cloud);
        Button local_btn = (Button) findViewById(R.id.local);

        setDefaultNightMode(MODE_NIGHT_YES);

        cloud_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StartSelectActivity.this, CloudVerifyActivity.class);
                startActivity(intent);
            }
        });

        /*
        local_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(StartSelectActivity.this, SocketVerifyActivity.class);
                startActivity(intent);
            }
        });

         */
    }
}
