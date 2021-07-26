package com.example.temp_sensor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Communication {

    ArrayList<Channels[]> arrayChannels = null;
    ArrayList<Sensors> arraySensors = null;

    String[] temp = null;
    String[] new_temp = null;
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
    isDataReceive : 8
    isDataReceive_Time : 9
    first_data_layout : 1
    include_data_layout : 2
    */

    public void requestHttp() {

        activity.setVisibility(false, 7);
        activity.setVisibility(false, 8);
        activity.setVisibility(false, 9);
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
                    activity.setTextSize(7, 25);
                    System.out.println("통신 실패");
                }
                else if (result.getStatus().equals("true")) {
                    System.out.println("통신 성공"); // 여기 이전에 딜레이 발생시 progress dialog 넣어주기
                    activity.hideProgress();

                    String device_last_update = result.getDevice_lastupdate();
                    String device_interval = result.getDevice_interval();
                    try {
                        isDeviceConnect(device_last_update, device_interval);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Sensors[] sensors = result.getSensors();
                    Channels[] channels;

                    arrayChannels = new ArrayList<>();
                    arraySensors = new ArrayList<>();
                    for (int i = 0; i < sensors.length; i++) {
                        channels = sensors[i].getChannels();
                        arraySensors.add(sensors[i]);
                        arrayChannels.add(channels);
                    }

                    String title_data = PreferenceManager.getString(activity, "selected_title_data");
                    boolean is_from_setting = PreferenceManager.getBoolean(activity, "is_from_setting");
                    int showing_sensor_num = PreferenceManager.getInt(activity, "selected_total_sensor_num");
                    int tmp = 0;

                    if(is_from_setting) {
                        for (int i = 0; i < arrayChannels.size(); i++)
                            for (int j = 0; j < arrayChannels.get(i).length; j++) {
                                if (title_data.contains(",")) {
                                    temp = new String[2];
                                    temp = PreferenceManager.getString(activity, "selected_title_data").split(",");
                                    switch (j) {
                                        case 0 :
                                            arrayChannels.get(i)[j].setCh_name(temp[0]);
                                            break;
                                        case 1 :
                                            if(temp.length != 1)
                                                arrayChannels.get(i)[j].setCh_name(temp[1]);
                                            break;
                                    }
                                } else {
                                    temp = new String[1];
                                    temp[0] = PreferenceManager.getString(activity, "selected_title_data");
                                    while(true) {
                                        if(tmp == showing_sensor_num) {
                                            arrayChannels.get(i)[tmp].setCh_name(temp[0]);
                                            break;
                                        }
                                        tmp++;
                                    }
                                }
                            }
                        temp = null;
                    }

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

                            if (title_data == null || title_data.equals("")) // 최초 접속인 경우
                                setText(0, i, j, 0);
                            else if (title_data.contains(",")) { // 센서 2개를 선택한 경우
                                temp = new String[2];
                                temp = PreferenceManager.getString(activity, "selected_title_data").split(",");
                                for (int k = 0; k < temp.length; k++)
                                    if (temp[k].equals(arrayChannels.get(i)[j].getCh_name()))  // 받아온 제목과 동일한지 확인
                                        setText(2, i, j, k);
                            }
                            else // 센서 1개를 선택한 경우
                                setText(1, i, j, 0);
                            PreferenceManager.setString(activity, "ch" + j + "_name", arrayChannels.get(i)[j].getCh_name());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetInfo> call, Throwable t) {
                activity.setVisibility(true, 7);
                activity.setTextSize(7, 25);
                System.out.println(t.getMessage());
            }
        });
    }

    public void isDeviceConnect(String device_last_update, String device_interval) throws Exception {

        String localTime = null; // device_last_update는 UTC이므로 이를 Local Time으로 변경하기 위한 작업
        TimeZone tz = TimeZone.getTimeZone("GMT+09:00");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parseDate;
        try {
            parseDate = sdf.parse(device_last_update);
            long milliseconds = parseDate.getTime();
            int offset = tz.getOffset(milliseconds);
            localTime = sdf.format(milliseconds + offset);
            localTime = localTime.replace("+0000", "");
        } catch(Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }

        Date past = sdf.parse(localTime);
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(past);
        calendar.add(Calendar.MINUTE, (Integer.parseInt(device_interval) / 60) * 2); // 디바이스 전송 주기 분 단위로 변환 후

        long now = System.currentTimeMillis();
        Date current_date = sdf.parse(sdf.format(now));
        Date device_update_date = sdf.parse(sdf.format(calendar.getTime()));

        // device_last_update와 비교하여 너무 오래 update가 진행되지 않는 경우 경고문 팝업
        if(current_date.compareTo(device_update_date) > 0) {
            activity.setVisibility(true, 8);
            activity.setVisibility(true, 9);
            activity.setText("마지막 데이터 전송 시간 : " + localTime, 9);
            activity.setTextSize(8, 25);
            activity.setTextSize(9, 25);
            System.out.println("디바이스에서 데이터를 전송하지 못하고 있습니다.\n마지막 데이터 전송 시간 : " + localTime);
        }
        else
            System.out.println("디바이스에서 데이터를 제대로 전송 중입니다.\n마지막 데이터 전송 시간 : " + localTime);
    }

    public void setText(int num, int i, int j, int k) {
        switch (num) {
            case 0 : // 최초 접속
                switch (j) {
                    case 0 :
                        activity.setText(arrayChannels.get(i)[j].getCh_name(), 5);
                        if (arrayChannels.get(i)[j].getCh_value().length() == 0)
                            activity.setText(arrayChannels.get(i)[j].getCh_value(), 1);
                        else
                            activity.setText(arrayChannels.get(i)[j].getCh_value().substring(0, 4), 1);
                        activity.setText(arrayChannels.get(i)[j].getCh_unit(), 3);
                        break;
                    case 1 :
                        activity.setText(arrayChannels.get(i)[j].getCh_name(), 6);
                        if (arrayChannels.get(i)[j].getCh_value().length() == 0)
                            activity.setText(arrayChannels.get(i)[j].getCh_value(), 2);
                        else
                            activity.setText(arrayChannels.get(i)[j].getCh_value().substring(0, 4), 2);
                        activity.setText(arrayChannels.get(i)[j].getCh_unit(), 4);
                        break;
                }
                break;
            case 1 : // 센서 1개 선택
                temp = new String[2];
                temp[0] = PreferenceManager.getString(activity, "selected_title_data"); // 이 경우 1번째 값은 null
                if (temp[0].equals(arrayChannels.get(i)[j].getCh_name())) {

                    activity.setVisibility(false, 0);
                    activity.setText(arrayChannels.get(i)[j].getCh_name(), 5);
                    if (arrayChannels.get(i)[j].getCh_value().length() == 0)
                        activity.setText(arrayChannels.get(i)[j].getCh_value(), 1);
                    else
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
                break;
            case 2 : // 센서 2개 선택
                switch (k) {
                    case 0 :
                        activity.setText(arrayChannels.get(i)[j].getCh_name(), 5);
                        if (arrayChannels.get(i)[j].getCh_value().length() == 0)
                            activity.setText(arrayChannels.get(i)[j].getCh_value(), 1);
                        else
                            activity.setText(arrayChannels.get(i)[j].getCh_value().substring(0, 4), 1);
                        activity.setText(arrayChannels.get(i)[j].getCh_unit(), 3);
                        break;
                    case 1 :
                        activity.setText(arrayChannels.get(i)[j].getCh_name(), 6);
                        if (arrayChannels.get(i)[j].getCh_value().length() == 0)
                            activity.setText(arrayChannels.get(i)[j].getCh_value(), 2);
                        else
                            activity.setText(arrayChannels.get(i)[j].getCh_value().substring(0, 4), 2);
                        activity.setText(arrayChannels.get(i)[j].getCh_unit(), 4);
                        break;
                }
                break;
        }
    }
}
