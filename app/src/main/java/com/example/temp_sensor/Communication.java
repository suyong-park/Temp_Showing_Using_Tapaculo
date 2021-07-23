package com.example.temp_sensor;

import androidx.appcompat.app.AlertDialog;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Communication {

    ArrayList<Channels[]> arrayChannels = new ArrayList<>();
    ArrayList<Sensors> arraySensors = new ArrayList<>();

    String api_key_str;
    String api_secret_str;
    String mac_str;
    int count = 0;

    MainActivity activity;
    Connect_Tapaculo tapaculo;

    public Communication(MainActivity activity, Connect_Tapaculo tapaculo, String api_key_str, String api_secret_str, String mac_str) {
        this.activity = activity;
        this.tapaculo = tapaculo;
        this.api_key_str = api_key_str;
        this.api_secret_str = api_secret_str;
        this.mac_str = mac_str;
    }

    /*
    data_1 : 1
    data_2 : 2
    data_unit_1 : 3
    data_unit_2 : 4
    data_unit_ko_1 : 5
    data_unit_ko_2 : 6
    isNetwork : 7
    indicator : 8
    first_data_layout : 1
    include_data_layout : 2
    */

    public void requestHttp() {

        activity.setVisibility(false, 7);
        activity.setProgress(activity);

        count += 1;
        if (count >= 2) count = 2;
        Call<GetInfo> call = tapaculo.getInfo(api_key_str, api_secret_str, mac_str);
        call.enqueue(new Callback<GetInfo>() {
            @Override
            public void onResponse(Call<GetInfo> call, Response<GetInfo> response) {
                GetInfo result = response.body();
                System.out.println("통신 시도 ...");
                if(result == null) {
                    activity.setVisibility(true, 7);
                    activity.setTextSize(80);
                    System.out.println("통신 실패");
                }
                else if (result.getStatus().equals("true")) {
                    System.out.println("통신 성공"); // 여기 이전에 딜레이 발생시 progress dialog 넣어주기
                    activity.hideProgress();

                    Sensors[] sensors = result.getSensors();
                    Channels[] channels;

                    for (int i = 0; i < sensors.length; i++) {
                        channels = sensors[i].getChannels();
                        arraySensors.add(sensors[i]);
                        arrayChannels.add(channels);
                    }

                    int showing_sensor_num = PreferenceManager.getInt(activity, "selected_total_sensor_num");
                    for (int i = 0; i < arrayChannels.size(); i++) {
                        PreferenceManager.setInt(activity, "device_sensor_num", arrayChannels.get(i).length);
                        for (int j = 0; j < arrayChannels.get(i).length; j++) {
                            if (showing_sensor_num == -1 || showing_sensor_num == 2) { // 센서를 몇 개 보여줄지 아직 세팅하지 않은 경우 or 2개 보여주는 경우
                                activity.setVisibility(true, 1);
                                activity.setVisibility(true, 2);
                            } else if (showing_sensor_num == 0 || showing_sensor_num == 1) {
                                activity.setVisibility(false, 6);
                                activity.setVisibility(false, 4);
                                activity.setVisibility(false, 2);
                                activity.setVisibility(true, 1);
                            }

                            String[] temp = new String[2];
                            if (PreferenceManager.getString(activity, "selected_title_data") == null || PreferenceManager.getString(activity, "selected_title_data").equals("")) {
                                // 최초 접속인 경우
                                if (j == 0) {
                                    activity.setText(arrayChannels.get(i)[j].getCh_name(), 5);
                                    activity.setText(arrayChannels.get(i)[j].getCh_value().substring(0, 4), 1);
                                    activity.setText(arrayChannels.get(i)[j].getCh_unit(), 3);
                                }
                                if (j == 1) {
                                    activity.setText(arrayChannels.get(i)[j].getCh_name(), 6);
                                    activity.setText(arrayChannels.get(i)[j].getCh_value().substring(0, 4), 2);
                                    activity.setText(arrayChannels.get(i)[j].getCh_unit(), 4);
                                }
                            } else if (PreferenceManager.getString(activity, "selected_title_data").contains(",")) {
                                // 센서 2개를 선택한 경우
                                temp = PreferenceManager.getString(activity, "selected_title_data").split(",");
                                for (int k = 0; k < temp.length; k++)
                                    if (temp[k].equals(arrayChannels.get(i)[j].getCh_name())) { // 받아온 제목과 동일한지 확인
                                        if (k == 0) {
                                            activity.setText(arrayChannels.get(i)[j].getCh_name(), 5);
                                            activity.setText(arrayChannels.get(i)[j].getCh_value().substring(0, 4), 1);
                                            activity.setText(arrayChannels.get(i)[j].getCh_unit(), 3);
                                        }
                                        if (k == 1) {
                                            activity.setText(arrayChannels.get(i)[j].getCh_name(), 6);
                                            activity.setText(arrayChannels.get(i)[j].getCh_value().substring(0, 4), 2);
                                            activity.setText(arrayChannels.get(i)[j].getCh_unit(), 4);
                                        }
                                    }
                            } else {
                                // 센서 1개를 선택한 경우
                                temp[0] = PreferenceManager.getString(activity, "selected_title_data"); // 이 경우 1번째 값은 null
                                if (temp[0].equals(arrayChannels.get(i)[j].getCh_name())) {

                                    activity.setVisibility(false, 0);
                                    activity.setText(arrayChannels.get(i)[j].getCh_name(), 5);
                                    activity.setText(arrayChannels.get(i)[j].getCh_value().substring(0, 4), 1);
                                    activity.setTypeface();
                                    activity.setText(arrayChannels.get(i)[j].getCh_unit(), 3);

                                    if (count < 2) {
                                        activity.addView(5);
                                        activity.addView(1);
                                        activity.addView(3);
                                    } else
                                        count = 2;

                                    activity.setLayoutGravity();
                                    activity.setLayoutOrientation();
                                }
                            }
                            PreferenceManager.setString(activity, "ch" + j + "_name", arrayChannels.get(i)[j].getCh_name());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetInfo> call, Throwable t) {
                activity.setVisibility(true, 7);
                activity.setTextSize(80);
                System.out.println(t.getMessage());
            }
        });
    }
}
