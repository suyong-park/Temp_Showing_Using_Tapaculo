package com.example.temp_sensor;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.Set;

public class SettingActivity extends AppCompatActivity {

    int id = 0;
    SettingActivity settingActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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

        int default_select_sensor_num = PreferenceManager.getInt(settingActivity, "sensor_num");
        if(default_select_sensor_num == 1) {
            RadioButton show_one = (RadioButton) findViewById(R.id.show_radio_one);
            show_one.setChecked(true);
        }
        else {
            RadioButton show_two = (RadioButton) findViewById(R.id.show_radio_two);
            show_two.setChecked(true);
        }

        LinearLayout setting_layout = (LinearLayout) findViewById(R.id.setting_layout);
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(settingActivity);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int check_id) {

                String[] ch_names = {PreferenceManager.getString(settingActivity, "ch1_name"),
                        PreferenceManager.getString(settingActivity, "ch2_name")};

                if(check_id == R.id.show_radio_one) {
                    id = 1;
                    builder.setTitle("선택")
                            .setPositiveButton("선택", null)
                            .setSingleChoiceItems(ch_names, 1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Snackbar.make(setting_layout, ch_names[i] + "값만 보여드립니다.", Snackbar.LENGTH_LONG).show();
                                    PreferenceManager.setString(settingActivity, "ch_name", ch_names[i]);
                                }
                            })
                            .setCancelable(false)
                            .show();
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
                PreferenceManager.setInt(settingActivity, "sensor_num", id);
                PreferenceManager.setString(settingActivity, "device_info", device_info.getText().toString());

                Set<String> hashSet = new HashSet<>();
                hashSet.add(PreferenceManager.getString(settingActivity, "ch1_name"));
                hashSet.add(PreferenceManager.getString(settingActivity, "ch2_name"));
                PreferenceManager.setStringSet(settingActivity, "hashSet", hashSet);

                builder.setTitle("설정")
                        .setMessage("설정이 완료되었습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ((MainActivity)MainActivity.CONTEXT).onResume();
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        });
    }
}
