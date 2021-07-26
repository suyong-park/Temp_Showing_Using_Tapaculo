package com.example.temp_sensor;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
        ArrayList<TextInputLayout> arrayInputLayout = new ArrayList<>();
        ArrayList<TextInputEditText> arrayInputEdit = new ArrayList<>();

        // 아래 부분은 CheckBox를 동적으로 생성하는 부분
        LinearLayout checkBoxlayer = (LinearLayout) findViewById(R.id.checkbox_linear);
        for(int i = 0; i < device_sensor_num; i++) { // 센서 이름 체크박스 동적으로 생성
            String ch_name = PreferenceManager.getString(settingActivity, "ch" + i + "_name");
            if(ch_name.equals("") || ch_name.equals(null))
                break;

            CheckBox sensorBox = new CheckBox(settingActivity);
            sensorBox.setId(i);
            System.out.println(i + "번째 체크박스 값 : " + ch_name);
            sensorBox.setText(ch_name);
            arrayBox.add(sensorBox);

            if(sensorBox.getParent() != null)
                ((ViewGroup) sensorBox.getParent()).removeView(sensorBox);
            checkBoxlayer.addView(sensorBox);
        }

        // 아래 부분은 TextInputLayout 동적으로 생성하는 부분 ==> 이를 통해 사용자가 센서 이름을 동적으로 변경할 수 있음.
        LinearLayout editTextlayer = (LinearLayout) findViewById(R.id.edit_ch_name_area);
        for(int i = 0; i < device_sensor_num; i++) {
            String ch_edit_name = PreferenceManager.getString(settingActivity, "ch" + i + "_name");
            if(ch_edit_name.equals("") || ch_edit_name.equals(null))
                break;

            LinearLayout linearLayout = new LinearLayout(settingActivity);
            TextView textView = new TextView(settingActivity);
            TextInputLayout textInputLayout = new TextInputLayout(settingActivity);
            textInputLayout.setBoxBackgroundColor(ContextCompat.getColor(settingActivity, android.R.color.black));
            textInputLayout.setBoxBackgroundMode(TextInputLayout.BOX_BACKGROUND_OUTLINE);
            TextInputEditText textInputEditText = new TextInputEditText(textInputLayout.getContext());

            LinearLayout.LayoutParams edit_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            LinearLayout.LayoutParams input_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            if(textView.getParent() != null)
                ((ViewGroup) textView.getParent()).removeView(textView);
            linearLayout.addView(textView, edit_params);

            if(textInputEditText.getParent() != null)
                ((ViewGroup) textInputEditText.getParent()).removeView(textInputEditText);
            textInputLayout.addView(textInputEditText, edit_params);

            if(linearLayout.getParent() != null)
                ((ViewGroup) linearLayout.getParent()).removeView(linearLayout);
            editTextlayer.addView(linearLayout, edit_params);

            if(textInputLayout.getParent() != null)
                ((ViewGroup) textInputLayout.getParent()).removeView(textInputLayout);
            linearLayout.addView(textInputLayout);

            System.out.println(i + "번째 텍스트인풋 값 : " + ch_edit_name);

            linearLayout.setOrientation(LinearLayout.VERTICAL);

            textView.setText(ch_edit_name);

            textInputLayout.setLayoutParams(input_params);
            textInputLayout.setHint(ch_edit_name);

            textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);

            arrayInputLayout.add(textInputLayout);
            arrayInputEdit.add(textInputEditText);
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
                if(!Request.isNetworkConnected(settingActivity)) {
                    builder.setTitle("경고")
                            .setMessage("네트워크가 연결되지 않았습니다. 연결 후 다시 시도하세요.")
                            .setPositiveButton("확인", null)
                            .show();
                    return;
                }
                else
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
