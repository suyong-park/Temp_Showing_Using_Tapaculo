package com.example.temp_sensor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;

public class CloudMainActivity extends AppCompatActivity {

    LinearProgressIndicator indicator;
    CloudMainActivity cloudMainActivity;
    AlertDialog.Builder builder;
    AlertDialog con;
    CloudTimer cloudTimer;

    TextView loading;
    TextView device_info;
    TextView isNetwork;
    TextView isServerOn;
    TextView isDataReceive;
    TextView isDataReceive_Time;
    TextView data_1;
    TextView data_2;
    TextView data_unit_1;
    TextView data_unit_2;
    TextView data_unit_ko_1;
    TextView data_unit_ko_2;
    Button refresh_btn;

    LinearLayout include_data_layout;
    LinearLayout first_data_layout;

    public static Context CONTEXT;
    public boolean is_to_setting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_main);

        CONTEXT = this;
        cloudMainActivity = this;

        String api_key_str = PreferenceManager.getString(cloudMainActivity, "api_key_str");
        String api_secret_str = PreferenceManager.getString(cloudMainActivity, "api_secret_str");
        String search_str = PreferenceManager.getString(cloudMainActivity, "sensors");

        Connect_Tapaculo tapaculo = Request.getRetrofit().create(Connect_Tapaculo.class);

        device_info = (TextView) findViewById(R.id.device_location);
        device_info.setText(PreferenceManager.getString(cloudMainActivity, "device_info"));

        loading = (TextView) findViewById(R.id.loading_text);
        isNetwork = (TextView) findViewById(R.id.network_state_text);
        isServerOn = (TextView) findViewById(R.id.server_state_text);
        isDataReceive = (TextView) findViewById(R.id.device_data_receive_state);
        isDataReceive_Time = (TextView) findViewById(R.id.device_data_receive_state_time);
        data_1 = (TextView) findViewById(R.id.show_data_1);
        data_2 = (TextView) findViewById(R.id.show_data_2);
        data_unit_1 = (TextView) findViewById(R.id.show_data_unit_1);
        data_unit_2 = (TextView) findViewById(R.id.show_data_unit_2);
        data_unit_ko_1 = (TextView) findViewById(R.id.show_data_unit_ko_1);
        data_unit_ko_2 = (TextView) findViewById(R.id.show_data_unit_ko_2);
        refresh_btn = (Button) findViewById(R.id.refresh_btn);

        include_data_layout = (LinearLayout) findViewById(R.id.include_data_linear);
        first_data_layout = (LinearLayout) findViewById(R.id.first_data_layout);

        int refresh_value = PreferenceManager.getInt(cloudMainActivity, "refresh_value");

        cloudTimer = new CloudTimer(Long.MAX_VALUE, refresh_value * 60000, cloudMainActivity, tapaculo, api_key_str, api_secret_str, search_str);
        cloudTimer.start();

        device_info.setText(PreferenceManager.getString(cloudMainActivity, "device_info"));

        builder = new MaterialAlertDialogBuilder(cloudMainActivity);
        con = builder.create();
        Button setting_btn = (Button) findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Request.isNetworkConnected(cloudMainActivity)) {
                    builder.setTitle("경고")
                            .setMessage("네트워크가 끊어져 있습니다. 네트워크를 연결하고 통신될 때까지 기다려 주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null)
                            .show();
                    return;
                }
                is_to_setting = true;
                Intent intent = new Intent(cloudMainActivity, CloudSettingActivity.class);
                startActivity(intent);
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    /*
    data_1 : 1
    data_2 : 2
    data_unit_1 : 3
    data_unit_2 : 4
    data_unit_ko_1 : 5
    data_unit_ko_2 : 6
    isNetwork : 7
    isDataReceive : 8
    isDataReceive_Time : 9
    isServerOn : 10
    loading : 11
    refresh_btn : 12
    first_data_layout : 1
    include_data_layout : 2
    */
    public void setText(String text, int text_id) { // setText UI 담당
        switch (text_id) {
            case 1 :
                data_1.setText(text);
                break;
            case 2 :
                data_2.setText(text);
                break;
            case 3 :
                data_unit_1.setText(text);
                break;
            case 4 :
                data_unit_2.setText(text);
                break;
            case 5 :
                data_unit_ko_1.setText(text);
                break;
            case 6 :
                data_unit_ko_2.setText(text);
                break;
            case 9 :
                isDataReceive_Time.setText(text);
                break;
        }
    }

    public void setTextSize(int text_id, int size) {
        switch (text_id) {
            case 7 :
                isNetwork.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
                break;
            case 8 :
                isDataReceive.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
                break;
            case 9 :
                isDataReceive_Time.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
                break;
        }
    }

    public void setVisibility(boolean is_visual, int text_id) { // 시각화 담당
        switch (text_id) {
            case 0 :
                if(is_visual)
                    first_data_layout.setVisibility(View.VISIBLE);
                else
                    first_data_layout.setVisibility(View.GONE);
                break;
            case 1 :
                if(is_visual)
                    data_1.setVisibility(View.VISIBLE);
                else
                    data_1.setVisibility(View.GONE);
                break;
            case 2 :
                if(is_visual)
                    data_2.setVisibility(View.VISIBLE);
                else
                    data_2.setVisibility(View.GONE);
                break;
            case 4 :
                if(is_visual)
                    data_unit_2.setVisibility(View.VISIBLE);
                else
                    data_unit_2.setVisibility(View.GONE);
                break;
            case 6 :
                if(is_visual)
                    data_unit_ko_2.setVisibility(View.VISIBLE);
                else
                    data_unit_ko_2.setVisibility(View.GONE);
                break;
            case 7 :
                if(is_visual)
                    isNetwork.setVisibility(View.VISIBLE);
                else
                    isNetwork.setVisibility(View.GONE);
                break;
            case 8 :
                if(is_visual)
                    isDataReceive.setVisibility(View.VISIBLE);
                else
                    isDataReceive.setVisibility(View.GONE);
                break;
            case 9 :
                if(is_visual)
                    isDataReceive_Time.setVisibility(View.VISIBLE);
                else
                    isDataReceive_Time.setVisibility(View.GONE);
                break;
            case 10 :
                if(is_visual)
                    isServerOn.setVisibility(View.VISIBLE);
                else
                    isServerOn.setVisibility(View.GONE);
                break;
            case 11 :
                if(is_visual)
                    loading.setVisibility(View.VISIBLE);
                else
                    loading.setVisibility(View.GONE);
                break;
            case 12 :
                if(is_visual)
                    refresh_btn.setVisibility(View.VISIBLE);
                else
                    refresh_btn.setVisibility(View.GONE);
                break;
        }
    }

    public void setProgress(Activity activity) {
        if(!activity.isFinishing()) {
            indicator = (LinearProgressIndicator) findViewById(R.id.linear_progress);
            indicator.show();
        }
    }

    public void hideProgress() {
        indicator.hide();
    }

    public void setTypeface() { // text bold 등의 font 속성 담당
        data_1.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setLayoutGravity() {
        include_data_layout.setGravity(Gravity.CENTER);
    }

    public void setLayoutOrientation() {
        include_data_layout.setOrientation(LinearLayout.HORIZONTAL);
    }

    public void addView(int id) {
        switch (id) {
            case 1 :
                if (data_1.getParent() != null)
                    ((ViewGroup) data_1.getParent()).removeView(data_1);
                include_data_layout.addView(data_1);
                break;
            case 3 :
                if (data_unit_1.getParent() != null)
                    ((ViewGroup) data_unit_1.getParent()).removeView(data_unit_1);
                include_data_layout.addView(data_unit_1);
                break;
            case 5 :
                if (data_unit_ko_1.getParent() != null)
                    ((ViewGroup) data_unit_ko_1.getParent()).removeView(data_unit_ko_1);
                include_data_layout.addView(data_unit_ko_1);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(con != null && con.isShowing())
            con.dismiss();
    }

    @Override
    public void onBackPressed() {
        EditText admin_value = new EditText(cloudMainActivity);
        LinearLayout container = new LinearLayout(cloudMainActivity);
        container.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(60, 0, 60, 0);

        admin_value.setLayoutParams(lp);
        admin_value.setHint("관리자 인증번호 4자리");
        admin_value.setInputType(InputType.TYPE_CLASS_NUMBER);

        container.addView(admin_value);

        builder = new MaterialAlertDialogBuilder(cloudMainActivity);
        builder.setTitle("경고")
                .setMessage("나가려면 관리자 인증번호 4자리를 입력하세요.")
                .setCancelable(false)
                .setView(container)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int is_admin_value = PreferenceManager.getInt(cloudMainActivity, "admin_value");

                        if(admin_value.length() != 4 || is_admin_value != Integer.parseInt(admin_value.getText().toString())) {
                            Snackbar.make(findViewById(R.id.linear_main), "관리자 인증번호가 다릅니다.", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        else
                            Snackbar.make(findViewById(R.id.linear_main), "설정 화면에서 저장한 정보가 사라집니다. 그래도 나갈까요?", Snackbar.LENGTH_LONG).setAction("네", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    int device_sensor_num = PreferenceManager.getInt(cloudMainActivity, "device_sensor_num");
                                    PreferenceManager.removeKey(cloudMainActivity, "device_sensor_num");
                                    for(int i = 0; i < device_sensor_num; i++)
                                        PreferenceManager.removeKey(cloudMainActivity, "ch" + i + "_name");
                                    PreferenceManager.removeKey(cloudMainActivity, "selected_total_sensor_num");
                                    PreferenceManager.removeKey(cloudMainActivity, "device_info");
                                    PreferenceManager.removeKey(cloudMainActivity, "is_from_setting");
                                    PreferenceManager.removeKey(cloudMainActivity, "selected_total_sensor_id");
                                    PreferenceManager.removeKey(cloudMainActivity, "selected_title_data");
                                    if(cloudTimer != null) {
                                        cloudTimer.cancel();
                                        cloudTimer.onFinish();
                                    }
                                    finish();
                                }
                            }).show();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();

        if(!is_to_setting) {
            ActivityManager activityManager = (ActivityManager) cloudMainActivity.getSystemService(Context.ACTIVITY_SERVICE);
            activityManager.moveTaskToFront(getTaskId(), 0);
        }
    }
}