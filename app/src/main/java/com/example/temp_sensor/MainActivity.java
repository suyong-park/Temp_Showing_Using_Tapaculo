package com.example.temp_sensor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    MainActivity mainActivity;
    TextView device_info;
    AlertDialog.Builder builder;
    AlertDialog con;
    ProgressDialog progressDialog;

    public static Context CONTEXT;
    Thread mThread;

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
        System.out.println("값 알아보기(지워져야함) : " + PreferenceManager.getString(mainActivity, "device_info"));
        device_info.setText(PreferenceManager.getString(mainActivity, "device_info"));

        TextView isNetwork = (TextView) findViewById(R.id.network_state_text);

        TextView data_1 = (TextView) findViewById(R.id.show_data_1);
        TextView data_2 = (TextView) findViewById(R.id.show_data_2);
        TextView data_unit_1 = (TextView) findViewById(R.id.show_data_unit_1);
        TextView data_unit_2 = (TextView) findViewById(R.id.show_data_unit_2);
        TextView data_unit_ko_1 = (TextView) findViewById(R.id.show_data_unit_ko_1);
        TextView data_unit_ko_2 = (TextView) findViewById(R.id.show_data_unit_ko_2);

        LinearLayout include_data_layout = (LinearLayout) findViewById(R.id.include_data_linear);
        LinearLayout first_data_layout = (LinearLayout) findViewById(R.id.first_data_layout);

        mThread = new Thread(new mainThread(mainActivity, api_key_str, api_secret_str, mac_str, tapaculo, isNetwork, data_1, data_2, data_unit_1, data_unit_2, data_unit_ko_1, data_unit_ko_2, first_data_layout, include_data_layout));
        mThread.start();

        device_info.setText(PreferenceManager.getString(mainActivity, "device_info"));

        builder = new MaterialAlertDialogBuilder(mainActivity);
        con = builder.create();
        Button setting_btn = (Button) findViewById(R.id.setting_btn);
        setting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Request.isNetworkConnected(mainActivity)) {
                    builder.setTitle("경고")
                            .setMessage("네트워크가 끊어져 있습니다. 네트워크를 연결하고 통신될 때까지 기다려 주세요.")
                            .setCancelable(false)
                            .setPositiveButton("확인", null)
                            .show();
                    return;
                }
                Intent intent = new Intent(mainActivity, SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("스레드 interrupted 여부 : " + mThread.interrupted());

        if(con != null && con.isShowing())
            con.dismiss();
        if(progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
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

        builder = new MaterialAlertDialogBuilder(mainActivity);
        builder.setTitle("경고")
                .setMessage("나가려면 관리자 인증번호 4자리를 입력하세요.")
                .setCancelable(false)
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
                            Snackbar.make(findViewById(R.id.linear_main), "설정 화면에서 저장한 정보가 사라집니다. 그래도 나갈까요?", Snackbar.LENGTH_LONG).setAction("네", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Thread.currentThread().interrupt();
                                    finish();
                                }
                            }).show();
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }
}

class mainThread implements Runnable {

    ArrayList<Channels[]> arrayChannels = new ArrayList<>();
    ArrayList<Sensors> arraySensors = new ArrayList<>();

    ProgressDialog progressDialog;
    Activity activity;
    TextView isNetwork;
    TextView data_1, data_2;
    TextView data_unit_1, data_unit_2;
    TextView data_unit_ko_1, data_unit_ko_2;
    LinearLayout include_data_layout;
    LinearLayout first_data_layout;

    int count = 0;
    Connect_Tapaculo tapaculo;
    String api_key_str, api_secret_str, mac_str;

    public mainThread(Activity activity, String api_key_str, String api_secret_str, String mac_str, Connect_Tapaculo tapaculo, TextView isNetwork, TextView data_1, TextView data_2, TextView data_unit_1, TextView data_unit_2, TextView data_unit_ko_1, TextView data_unit_ko_2, LinearLayout first_data_layout, LinearLayout include_data_layout) {
        this.activity = activity;
        this.api_key_str = api_key_str;
        this.api_secret_str = api_secret_str;
        this.mac_str = mac_str;
        this.tapaculo = tapaculo;
        this.isNetwork = isNetwork;
        this.data_1 = data_1;
        this.data_2 = data_2;
        this.data_unit_1 = data_unit_1;
        this.data_unit_2 = data_unit_2;
        this.data_unit_ko_1 = data_unit_ko_1;
        this.data_unit_ko_2 = data_unit_ko_2;
        this.first_data_layout = first_data_layout;
        this.include_data_layout = include_data_layout;
    }

    public void run() {

        while(!Thread.currentThread().isInterrupted()) {
            try {
                count += 1;
                if(count >= 2)
                    count = 2;
                System.out.println("통신 시도...");
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!activity.isFinishing()) {
                            progressDialog = new ProgressDialog(activity);
                            progressDialog.setMessage("통신 중입니다 ...");
                            progressDialog.setCancelable(false);
                            progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);
                            progressDialog.show();
                        }
                    }
                }, 0);

                Call<GetInfo> call = tapaculo.getInfo(api_key_str, api_secret_str, mac_str);
                call.enqueue(new Callback<GetInfo>() {

                    @Override
                    public void onResponse(Call<GetInfo> call, Response<GetInfo> response) {

                        GetInfo result = response.body();
                        if (result == null) {
                            progressDialog.dismiss();
                            try {
                                System.out.println("통신 실패");
                                System.out.println("재시도 ...");
                                isNetwork.setVisibility(View.VISIBLE);
                                isNetwork.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80);
                                Thread.sleep(15000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        if (result.getStatus().equals("true")) {

                            isNetwork.setVisibility(View.GONE);
                            System.out.println("통신 성공");

                            Sensors[] sensors = result.getSensors();
                            Channels[] channels;

                            for (int i = 0; i < sensors.length; i++) {
                                channels = sensors[i].getChannels();
                                arraySensors.add(sensors[i]);
                                arrayChannels.add(channels);
                            }

                            /*
                             * 아래 코드가 실질적으로 데이터를 setText 하는 부분
                             * 데이터 삽입에 문제가 있는지 이곳에서 확인할 것
                             */
                            int showing_sensor_num = PreferenceManager.getInt(activity, "selected_total_sensor_num");
                            for (int i = 0; i < arrayChannels.size(); i++) {
                                PreferenceManager.setInt(activity, "device_sensor_num", arrayChannels.get(i).length);
                                for (int j = 0; j < arrayChannels.get(i).length; j++) {
                                    if (showing_sensor_num == -1 || showing_sensor_num == 2) { // 센서를 몇 개 보여줄지 아직 세팅하지 않은 경우 or 2개 보여주는 경우
                                        data_1.setVisibility(View.VISIBLE);
                                        data_2.setVisibility(View.VISIBLE);
                                    } else if (showing_sensor_num == 0 || showing_sensor_num == 1) {
                                        data_unit_ko_2.setVisibility(View.GONE);
                                        data_unit_2.setVisibility(View.GONE);
                                        data_2.setVisibility(View.GONE);
                                        data_1.setVisibility(View.VISIBLE);
                                    }

                                    String[] temp = new String[2];
                                    if (PreferenceManager.getString(activity, "selected_title_data") == null || PreferenceManager.getString(activity, "selected_title_data").equals("")) {
                                        // 최초 접속인 경우
                                        if (j == 0) {
                                            data_unit_ko_1.setText(arrayChannels.get(i)[j].getCh_name());
                                            data_1.setText(arrayChannels.get(i)[j].getCh_value());
                                            data_unit_1.setText(arrayChannels.get(i)[j].getCh_unit());
                                        }
                                        if (j == 1) {
                                            data_unit_ko_2.setText(arrayChannels.get(i)[j].getCh_name());
                                            data_2.setText(arrayChannels.get(i)[j].getCh_value());
                                            data_unit_2.setText(arrayChannels.get(i)[j].getCh_unit());
                                        }
                                    } else if (PreferenceManager.getString(activity, "selected_title_data").contains(",")) {
                                        // 센서 2개를 선택한 경우
                                        temp = PreferenceManager.getString(activity, "selected_title_data").split(",");
                                        for (int k = 0; k < temp.length; k++)
                                            if (temp[k].equals(arrayChannels.get(i)[j].getCh_name())) { // 받아온 제목과 동일한지 확인
                                                if (k == 0) {
                                                    data_unit_ko_1.setText(arrayChannels.get(i)[j].getCh_name());
                                                    data_1.setText(arrayChannels.get(i)[j].getCh_value());
                                                    data_unit_1.setText(arrayChannels.get(i)[j].getCh_unit());
                                                }
                                                if (k == 1) {
                                                    data_unit_ko_2.setText(arrayChannels.get(i)[j].getCh_name());
                                                    data_2.setText(arrayChannels.get(i)[j].getCh_value());
                                                    data_unit_2.setText(arrayChannels.get(i)[j].getCh_unit());
                                                }
                                            }
                                    } else {
                                        // 센서 1개를 선택한 경우
                                        temp[0] = PreferenceManager.getString(activity, "selected_title_data"); // 이 경우 1번째 값은 null
                                        if (temp[0].equals(arrayChannels.get(i)[j].getCh_name())) {
                                            first_data_layout.setVisibility(View.GONE);
                                            data_unit_ko_1.setText(arrayChannels.get(i)[j].getCh_name());
                                            data_1.setText(arrayChannels.get(i)[j].getCh_value());
                                            data_1.setTypeface(Typeface.DEFAULT_BOLD);
                                            data_unit_1.setText(arrayChannels.get(i)[j].getCh_unit());

                                            if(count < 2) {
                                                if (data_unit_ko_1.getParent() != null)
                                                    ((ViewGroup) data_unit_ko_1.getParent()).removeView(data_unit_ko_1);
                                                include_data_layout.addView(data_unit_ko_1);
                                                if (data_1.getParent() != null)
                                                    ((ViewGroup) data_1.getParent()).removeView(data_1);
                                                include_data_layout.addView(data_1);
                                                if (data_unit_1.getParent() != null)
                                                    ((ViewGroup) data_unit_1.getParent()).removeView(data_unit_1);
                                                include_data_layout.addView(data_unit_1);
                                            }
                                            else
                                                count = 2;

                                            include_data_layout.setGravity(View.TEXT_ALIGNMENT_CENTER);
                                            include_data_layout.setOrientation(LinearLayout.HORIZONTAL);
                                        }
                                    }
                                    PreferenceManager.setString(activity, "ch" + j + "_name", arrayChannels.get(i)[j].getCh_name());
                                }
                            }
                        }
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<GetInfo> call, Throwable t) {
                        progressDialog.dismiss();
                        try {
                            System.out.println("통신 실패");
                            System.out.println("재시도 ...");
                            isNetwork.setVisibility(View.VISIBLE);
                            isNetwork.setTextSize(TypedValue.COMPLEX_UNIT_SP, 80);
                            Thread.sleep(15000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                int splrate = PreferenceManager.getInt(activity, "refresh_value");
                Thread.sleep(splrate * 1000); // splrate을 기준으로 센서값 표현하는 페이지 새로고침
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
