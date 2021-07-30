package com.dekist.radionodepanel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CloudSettingActivity extends AppCompatActivity {

    CloudSettingActivity cloudSettingActivity;
    AlertDialog.Builder builder;
    AlertDialog con;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_setting);
        setTitle("설정");

        cloudSettingActivity = CloudSettingActivity.this;

        Slider network_on = (Slider) findViewById(R.id.network_on_slider);
        Slider network_off = (Slider) findViewById(R.id.network_off_slider);

        EditText device_info = (EditText) findViewById(R.id.device_enter);
        Button setting_btn = (Button) findViewById(R.id.setting_finish_btn);

        device_info.setText(PreferenceManager.getString(cloudSettingActivity, "device_info"));

        float volume_on = PreferenceManager.getFloat(cloudSettingActivity, "network_on_volume");
        float volume_off = PreferenceManager.getFloat(cloudSettingActivity, "network_off_volume");

        network_on.setValue((int) volume_on);
        network_off.setValue((int) volume_off);

        View view = (View) findViewById(R.id.setting_layout);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Request.downKeyboard(cloudSettingActivity);
                    return true;
                }
                return false;
            }
        });

        int device_sensor_num = PreferenceManager.getInt(cloudSettingActivity, "device_sensor_num");
        ArrayList<CheckBox> arrayBox = new ArrayList<>();
        ArrayList<TextInputEditText> arrayInputEdit = new ArrayList<>();

        // 아래 부분은 CheckBox를 동적으로 생성하는 부분
        LinearLayout checkBoxlayer = (LinearLayout) findViewById(R.id.checkbox_linear);
        for(int i = 0; i < device_sensor_num; i++) { // 센서 이름 체크박스 동적으로 생성
            String ch_name = PreferenceManager.getString(cloudSettingActivity, "ch" + i + "_name");
            if(ch_name.equals("") || ch_name.equals(null))
                break;

            CheckBox sensorBox = new CheckBox(cloudSettingActivity);
            sensorBox.setId(i);
            sensorBox.setText(ch_name);
            arrayBox.add(sensorBox);

            if(sensorBox.getParent() != null)
                ((ViewGroup) sensorBox.getParent()).removeView(sensorBox);
            checkBoxlayer.addView(sensorBox);
        }

        // 아래 부분은 TextInputLayout 동적으로 생성하는 부분 ==> 이를 통해 사용자가 센서 이름을 동적으로 변경할 수 있음.
        LinearLayout editTextlayer = (LinearLayout) findViewById(R.id.edit_ch_name_area);
        for(int i = 0; i < device_sensor_num; i++) {
            String ch_edit_name = PreferenceManager.getString(cloudSettingActivity, "ch" + i + "_name");
            if(ch_edit_name.equals("") || ch_edit_name.equals(null))
                break;

            LinearLayout linearLayout = new LinearLayout(cloudSettingActivity);
            TextView textView = new TextView(cloudSettingActivity);
            TextInputLayout textInputLayout = new TextInputLayout(cloudSettingActivity);
            textInputLayout.setBoxBackgroundColor(ContextCompat.getColor(cloudSettingActivity, android.R.color.black));
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

            linearLayout.setOrientation(LinearLayout.VERTICAL);

            textView.setText(ch_edit_name);

            textInputLayout.setLayoutParams(input_params);
            textInputLayout.setHint(ch_edit_name);

            textInputEditText.setInputType(InputType.TYPE_CLASS_TEXT);

            arrayInputEdit.add(textInputEditText);
        }

        builder = new MaterialAlertDialogBuilder(cloudSettingActivity);
        con = builder.create();

        network_on.addOnSliderTouchListener(onSliderTouchListener);
        network_off.addOnSliderTouchListener(onSliderTouchListener);

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

                String index_check_box = "";
                for(int i = 0; i < arrayBox.size(); i++) {
                    if (!arrayInputEdit.get(i).getText().toString().trim().isEmpty() && !arrayBox.get(i).isChecked()) {
                        builder.setTitle("경고")
                                .setMessage("이름을 바꿀 센서는 체크박스에서 체크되어야 합니다.")
                                .setPositiveButton("확인", null)
                                .show();
                        return;
                    }
                    else if (arrayBox.get(i).isChecked()) {
                        if (count == 2) { // 2개의 체크박스를 선택
                            index_check_box += String.valueOf(i);
                            index_check_box += ",";
                        }
                        else if (count == 1) // 1개의 체크박스를 선택
                            index_check_box += String.valueOf(i);
                    }
                }
                if(index_check_box.contains(","))
                    index_check_box = index_check_box.substring(0, 3);

                for(int i = 0; i < arrayInputEdit.size(); i++)
                    if(!arrayInputEdit.get(i).getText().toString().trim().isEmpty())
                        PreferenceManager.setString(cloudSettingActivity, "ch" + i + "_name", arrayInputEdit.get(i).getText().toString().trim());

                PreferenceManager.setString(cloudSettingActivity, "selected_total_sensor_id", index_check_box);
                PreferenceManager.setInt(cloudSettingActivity, "selected_total_sensor_num", count);
                PreferenceManager.setString(cloudSettingActivity, "device_info", device_info.getText().toString().trim());

                builder.setTitle("설정")
                        .setMessage("설정이 완료되었습니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int id) {
                                Intent intent = new Intent(cloudSettingActivity, MainActivity.class);
                                ((MainActivity) MainActivity.CONTEXT).finish();
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

    Slider.OnSliderTouchListener onSliderTouchListener = new Slider.OnSliderTouchListener() {
        @Override
        public void onStartTrackingTouch(@NonNull @NotNull Slider slider) {
        }

        @Override
        public void onStopTrackingTouch(@NonNull @NotNull Slider slider) {
            switch (slider.getId()) {
                case R.id.network_on_slider:
                    PreferenceManager.setFloat(cloudSettingActivity, "network_on_volume", (int) slider.getValue());
                    break;
                case R.id.network_off_slider:
                    PreferenceManager.setFloat(cloudSettingActivity, "network_off_volume", (int) slider.getValue());
                    break;
            }
        }
    };
}
