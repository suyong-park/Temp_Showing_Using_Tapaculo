package com.example.temp_sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<Channels[]> arrayChannels = new ArrayList<>();
    ArrayList<Sensors> arraySensors = new ArrayList<>();

    MainActivity mainActivity;
    TextView device_info;

    public static Context CONTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CONTEXT = this;
        mainActivity = this;

        // 센서에 따라 화면을 변동시킴
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        String api_key_str = PreferenceManager.getString(mainActivity, "api_key_str");
        String api_secret_str = PreferenceManager.getString(mainActivity, "api_secret_str");
        String mac_str = PreferenceManager.getString(mainActivity, "mac_str");

        Connect_Tapaculo tapaculo = Request.getRetrofit().create(Connect_Tapaculo.class);

        device_info = (TextView) findViewById(R.id.device_location);
        device_info.setText(PreferenceManager.getString(mainActivity, "device_info"));

        new Thread(new Runnable() {

            @Override
            public void run() {
                while(true) {
                    try {
                        Call<GetInfo> call = tapaculo.getInfo(api_key_str, api_secret_str, mac_str);
                        call.enqueue(new Callback<GetInfo>() {

                            @Override
                            public void onResponse(Call<GetInfo> call, Response<GetInfo> response) {

                                GetInfo result = response.body();
                                if (result == null) {
                                    Request.AlertBuild(mainActivity, "통신 실패", "네트워크 상태를 확인하세요.")
                                            .setPositiveButton(getResources().getString(R.string.positive_alert), null)
                                            .show();
                                    finish();
                                    return;
                                }
                                System.out.println("응답값 : " + response.body().toString());

                                if (result.getStatus().equals("true")) {

                                    System.out.println("통신 성공");

                                    Sensors[] sensors = result.getSensors();
                                    Channels[] channels;

                                    for (int i = 0; i < sensors.length; i++) {
                                        channels = sensors[i].getChannels();
                                        arraySensors.add(sensors[i]);
                                        arrayChannels.add(channels);
                                    }

                                    TextView data_1 = (TextView) findViewById(R.id.show_data_1);
                                    TextView data_2 = (TextView) findViewById(R.id.show_data_2);

                                    LinearLayout include_data_layout = (LinearLayout) findViewById(R.id.include_data_linear);
                                    LinearLayout first_data_layout = (LinearLayout) findViewById(R.id.first_data_layout);
                                    LinearLayout second_data_layout = (LinearLayout) findViewById(R.id.second_data_layout);

                                    int showing_sensor_num = PreferenceManager.getInt(mainActivity, "selected_total_sensor_num");
                                    for (int i = 0; i < arrayChannels.size(); i++) {
                                        PreferenceManager.setInt(mainActivity, "device_sensor_num", arrayChannels.get(i).length);
                                        for (int j = 0; j < arrayChannels.get(i).length; j++) {
                                            if (showing_sensor_num == -1 || showing_sensor_num == 2) { // 센서를 몇 개 보여줄지 아직 세팅하지 않은 경우 or 2개 보여주는 경우
                                                System.out.println("센서 선택 개수 : " + showing_sensor_num);
                                                data_1.setVisibility(View.VISIBLE);
                                                data_2.setVisibility(View.VISIBLE);
                                            } else if (showing_sensor_num == 0) { // 1번째 센서를 보여주기로 결정한 경우
                                                System.out.println("온도 선택");
                                                data_2.setVisibility(View.GONE);
                                                data_1.setVisibility(View.VISIBLE);
                                            } else if (showing_sensor_num == 1) { // 2번째 센서를 보여주기로 결정한 경우
                                                System.out.println("습도 선택");
                                                data_1.setVisibility(View.GONE);
                                                data_2.setVisibility(View.VISIBLE);
                                            }

                                            if (j == 0) {
                                                if(showing_sensor_num == 2 || showing_sensor_num == -1)
                                                    data_1.setText(arrayChannels.get(i)[j].getCh_value() + arrayChannels.get(i)[j].getCh_unit());
                                                else {
                                                    first_data_layout.setVisibility(View.GONE);
                                                    data_1.setText(arrayChannels.get(i)[j].getCh_value());
                                                    data_1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 610);
                                                    data_1.setTypeface(Typeface.DEFAULT_BOLD);

                                                    if (data_1.getParent() != null)
                                                        ((ViewGroup) data_1.getParent()).removeView(data_1);

                                                    include_data_layout.addView(data_1);
                                                    include_data_layout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                                }
                                            } else if (j == 1) {
                                                if(showing_sensor_num == 2 || showing_sensor_num == -1)
                                                    data_2.setText(arrayChannels.get(i)[j].getCh_value() + arrayChannels.get(i)[j].getCh_unit());
                                                else {
                                                    second_data_layout.setVisibility(View.GONE);
                                                    data_2.setText(arrayChannels.get(i)[j].getCh_value());
                                                    data_2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 610);
                                                    data_2.setTypeface(Typeface.DEFAULT_BOLD);

                                                    if (data_2.getParent() != null)
                                                        ((ViewGroup) data_2.getParent()).removeView(data_2);

                                                    include_data_layout.addView(data_2);
                                                    include_data_layout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                                }
                                            }
                                            PreferenceManager.setString(mainActivity, "ch" + j + "_name", arrayChannels.get(i)[j].getCh_name());
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<GetInfo> call, Throwable t) {
                                if(!mainActivity.isFinishing())
                                    Request.AlertBuild(mainActivity, "Communication Fail", "Communication Fail. Check internet.")
                                            .setPositiveButton(getResources().getString(R.string.positive_alert), null)
                                            .show();
                                finish();
                                return;
                            }
                        });

                        int splrate = PreferenceManager.getInt(mainActivity, "refresh_value");

                        Thread.sleep(splrate * 1000); // splrate을 기준으로 센서값 표현하는 페이지 새로고침
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        device_info.setText(PreferenceManager.getString(mainActivity, "device_info"));

        Button setting_btn = (Button) findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainActivity, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        EditText admin_value = new EditText(mainActivity);
        LinearLayout container = new LinearLayout(mainActivity);
        container.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(60, 0, 60, 0);

        admin_value.setLayoutParams(lp);
        admin_value.setHint("관리자 인증번호 4자리");
        admin_value.setInputType(InputType.TYPE_CLASS_NUMBER);

        container.addView(admin_value);

        Request.AlertBuild(mainActivity, "경고", "나가려면 관리자 인증번호 4자리를 입력하세요.")
                .setView(container)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int is_admin_value = PreferenceManager.getInt(mainActivity, "admin_value");

                        if(admin_value.length() != 4 || is_admin_value != Integer.parseInt(admin_value.getText().toString())) {
                            Snackbar.make(findViewById(R.id.linear_main), "관리자 인증번호가 다릅니다.", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        else
                            Snackbar.make(findViewById(R.id.linear_main), "정말로 나갈까요?", Snackbar.LENGTH_LONG).setAction("네", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).show();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }
}