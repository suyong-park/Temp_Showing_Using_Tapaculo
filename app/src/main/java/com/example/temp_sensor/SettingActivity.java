package com.example.temp_sensor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class SettingActivity extends AppCompatActivity {

    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.show_group);
        EditText device_info = (EditText) findViewById(R.id.device_enter);
        Button setting_btn = (Button) findViewById(R.id.setting_finish_btn);

        LinearLayout setting_layout = (LinearLayout) findViewById(R.id.setting_layout);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int check_id) {

                if(check_id == R.id.show_radio_one) {
                    id = 1;

                    /*
                    TODO : 1개를 보려면 어떤 것을 볼 지 결정하는 코드 작성
                     */


                    Snackbar.make(setting_layout, "1개 센서값만 보여드립니다.", Snackbar.LENGTH_LONG).show();
                }
                else if(check_id == R.id.show_radio_two) {
                    id = 2;
                    Snackbar.make(setting_layout, "2개 센서값을 보여드립니다.", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setInt(SettingActivity.this, "sensor_num", id);
                PreferenceManager.setString(SettingActivity.this, "device_info", device_info.getText().toString());

                Snackbar.make(setting_layout, "설정이 완료됐습니다.", Snackbar.LENGTH_LONG).show();
                finish();
            }
        });
    }
}
