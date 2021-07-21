package com.example.temp_sensor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    SettingActivity settingActivity;
    AlertDialog.Builder builder;
    AlertDialog con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("설정");

        settingActivity = SettingActivity.this;

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

        int device_sensor_num = PreferenceManager.getInt(settingActivity, "device_sensor_num");
        ArrayList<CheckBox> arrayBox = new ArrayList<>();

        LinearLayout checkBoxlayer = (LinearLayout) findViewById(R.id.checkbox_linear);
        for(int i = 0; i < device_sensor_num; i++) { // 센서 이름 체크박스 동적으로 생성
            String ch_name = PreferenceManager.getString(settingActivity, "ch" + i + "_name");
            if(ch_name.equals("") || ch_name.equals(null))
                break;

            CheckBox sensorBox = new CheckBox(settingActivity);
            sensorBox.setId(i);
            sensorBox.setText(ch_name);
            arrayBox.add(sensorBox);

            if(sensorBox.getParent() != null)
                ((ViewGroup) sensorBox.getParent()).removeView(sensorBox);
            checkBoxlayer.addView(sensorBox);
        }

        builder = new MaterialAlertDialogBuilder(settingActivity);
        con = builder.create();
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int count = 0;
                for(int i = 0; i < arrayBox.size(); i++)
                    if (arrayBox.get(i).isChecked())
                        count++;

                if(count == 0) {
                    builder.setTitle("경고")
                            .setMessage("화면에 띄울 센서를 선택하세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null)
                            .show();
                    return;
                }

                if(count > 2) {
                    builder.setTitle("경고")
                            .setMessage("센서는 최대 2개까지 선택할 수 있습니다.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null)
                            .show();
                    return;
                }

                String temp = "", data = "";
                for(int i = 0; i < arrayBox.size(); i++)
                    if(arrayBox.get(i).isChecked()) {
                        if(count == 2) {
                            if(!temp.equals(""))
                                data = temp + "," + arrayBox.get(i).getText().toString();
                            temp = arrayBox.get(i).getText().toString();
                        }
                        if (count == 1) {
                            count = i;
                            data = arrayBox.get(i).getText().toString();
                        }
                    }

                PreferenceManager.setInt(settingActivity, "selected_total_sensor_num", count);
                PreferenceManager.setString(settingActivity, "device_info", device_info.getText().toString());

                String finalData = data;
                builder.setTitle("설정")
                        .setMessage("설정이 완료되었습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(settingActivity, MainActivity.class);
                                PreferenceManager.setString(settingActivity, "selected_title_data", finalData);
                                ((MainActivity)MainActivity.CONTEXT).finish();
                                finish();
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(con != null && con.isShowing())
            con.dismiss();
    }
}
