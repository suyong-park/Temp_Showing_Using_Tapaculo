package com.example.temp_sensor;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.show_group);

        LinearLayout setting_layout = (LinearLayout) findViewById(R.id.setting_layout);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int check_id) {
                if(check_id == R.id.show_radio_one)
                    Snackbar.make(setting_layout, "1개 센서값만 보여드립니다.", Snackbar.LENGTH_LONG).show();
                else if(check_id == R.id.show_radio_two)
                    Snackbar.make(setting_layout, "2개 센서값을 보여드립니다.", Snackbar.LENGTH_LONG).show();
            }
        });

    }
}
