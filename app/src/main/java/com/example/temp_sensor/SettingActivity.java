package com.example.temp_sensor;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class SettingActivity extends AppCompatActivity {

    int id;
    SettingActivity settingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("설정");

        settingActivity = SettingActivity.this;

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.show_group);
        EditText device_info = (EditText) findViewById(R.id.device_enter);
        Button setting_btn = (Button) findViewById(R.id.setting_finish_btn);

        device_info.setText(PreferenceManager.getString(settingActivity, "device_info"));

        View view = (View) findViewById(R.id.setting_layout);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Request.downKeyboard(settingActivity);
                    return true;
                }
                return false;
            }
        });

        RadioButton first_value = (RadioButton) findViewById(R.id.show_radio_first_value);
        RadioButton second_value = (RadioButton) findViewById(R.id.show_radio_second_value);
        RadioButton show_two_value = (RadioButton) findViewById(R.id.show_radio_two);

        switch (PreferenceManager.getInt(settingActivity, "sensor_num")) {
            case 0:
                first_value.setChecked(true);
                break;
            case 1:
                second_value.setChecked(true);
                break;
            default:
                show_two_value.setChecked(true);
                break;
        }

        first_value.setText(PreferenceManager.getString(settingActivity, "ch1_name"));
        second_value.setText(PreferenceManager.getString(settingActivity, "ch2_name"));

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int check_id) {
                switch (check_id) {
                    case R.id.show_radio_first_value:
                        id = 0;
                        break;
                    case R.id.show_radio_second_value:
                        id = 1;
                        break;
                    case R.id.show_radio_two:
                        id = 2;
                        break;
                }
            }
        });

        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.setInt(settingActivity, "sensor_num", id);
                PreferenceManager.setString(settingActivity, "device_info", device_info.getText().toString());

                Snackbar.make(view, "설정이 완료되었습니다.", Snackbar.LENGTH_LONG)
                        .setAction(null, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(settingActivity, MainActivity.class);
                                ((MainActivity)MainActivity.CONTEXT).finish();
                                finish();
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }
}
