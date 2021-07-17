package com.example.temp_sensor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ArrayList<Channels[]> arrayChannels = new ArrayList<>();
    ArrayList<Sensors> arraySensors = new ArrayList<>();

    TextView device_info;

    public static Context CONTEXT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CONTEXT = this;

        // 센서에 따라 화면을 변동시킴
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        String api_key_str = PreferenceManager.getString(MainActivity.this, "api_key_str");
        String api_secret_str = PreferenceManager.getString(MainActivity.this, "api_secret_str");
        String mac_str = PreferenceManager.getString(MainActivity.this, "mac_str");

        Connect_Tapaculo tapaculo = Request.getRetrofit().create(Connect_Tapaculo.class);
        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(MainActivity.this);

        device_info = (TextView) findViewById(R.id.device_location);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        System.out.println("반복됩니다.");
                        Call<GetInfo> call = tapaculo.getInfo(api_key_str, api_secret_str, mac_str);
                        call.enqueue(new Callback<GetInfo>() {

                            @Override
                            public void onResponse(Call<GetInfo> call, Response<GetInfo> response) {

                                GetInfo result = response.body();
                                if (result == null) {
                                    builder.setTitle("Fail")
                                            .setMessage("Status Fail. Please Recheck your value.")
                                            .setPositiveButton(getResources().getString(R.string.positive_alert), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    finish();
                                                }
                                            })
                                            .show();
                                    return;
                                }

                                if (result.getStatus().equals("true")) {

                                    Sensors[] sensors = result.getSensors();
                                    Channels[] channels;

                                    for (int i = 0; i < sensors.length; i++) {
                                        channels = sensors[i].getChannels();
                                        arraySensors.add(sensors[i]);
                                        arrayChannels.add(channels);
                                    }

                                    TextView data_1 = (TextView) findViewById(R.id.show_data_1);
                                    TextView data_2 = (TextView) findViewById(R.id.show_data_2);

                                    for (int i = 0; i < arrayChannels.size(); i++)
                                        for (int j = 0; j < arrayChannels.get(i).length; j++) {
                                            if (j >= 2)
                                                break;
                                            else if (j == 0)
                                                data_1.setText(arrayChannels.get(i)[j].getCh_value() + arrayChannels.get(i)[j].getCh_unit());
                                            else if (j == 1)
                                                data_2.setText(arrayChannels.get(i)[j].getCh_value() + arrayChannels.get(i)[j].getCh_unit());
                                        }
                                }
                            }

                            @Override
                            public void onFailure(Call<GetInfo> call, Throwable t) {
                                builder.setTitle("Fail")
                                        .setMessage("Communication Fail. Check internet.")
                                        .setPositiveButton(getResources().getString(R.string.positive_alert), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        })
                                        .show();
                                return;
                            }
                        });

                        int splrate = PreferenceManager.getInt(MainActivity.this, "refresh_value");

                        System.out.println("반복 시간 : " + splrate + "초 입니다.");
                        Thread.sleep(splrate * 1000); // splrate을 기준으로 센서값 표현하는 페이지 새로고침
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        device_info.setText(PreferenceManager.getString(MainActivity.this, "device_info"));

        int showing_sensor_num = PreferenceManager.getInt(MainActivity.this, "sensor_num");

        if(showing_sensor_num == 0 || showing_sensor_num == -1) { // 센서를 몇 개 보여줄지 아직 세팅하지 않은 경우

        }
        else if(showing_sensor_num == 1) { // 센서를 1개 보여주기로 결정한 경우
            // TODO : 센서값을 몇 개를 볼지 그에 대한 액션을 정의. --> visual 속성을 정의
        }
        else if(showing_sensor_num == 2) { // 센서를 2개 보여주기로 결정한 경우

        }

        Button setting_btn = (Button) findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        device_info.setText(PreferenceManager.getString(MainActivity.this, "device_info"));
    }

    @Override
    public void onBackPressed() {

        EditText admin_value = new EditText(MainActivity.this);
        LinearLayout container = new LinearLayout(MainActivity.this);
        container.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(60, 0, 60, 0);

        admin_value.setLayoutParams(lp);
        admin_value.setHint("관리자 인증번호 4자리");
        admin_value.setInputType(InputType.TYPE_CLASS_NUMBER);

        container.addView(admin_value);

        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(MainActivity.this);
        builder.setTitle("경고")
                .setMessage("나가려면 관리자 인증번호 4자리를 입력하세요.")
                .setView(container)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int is_admin_value = PreferenceManager.getInt(MainActivity.this, "admin_value");

                        if(admin_value.length() != 4) {
                            Snackbar.make(findViewById(R.id.linear_main), "관리자 인증번호가 다릅니다.", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        else if(is_admin_value != Integer.parseInt(admin_value.getText().toString())) {
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