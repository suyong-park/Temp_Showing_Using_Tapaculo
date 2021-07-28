package com.example.temp_sensor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CloudCommunication {

    ArrayList<Rows_Values> arrayValues = null;

    String api_key_str;
    String api_secret_str;
    String search_str;
    int count = 0;

    CloudMainActivity activity;
    Connect_Tapaculo tapaculo;

    public CloudCommunication(CloudMainActivity activity, Connect_Tapaculo tapaculo, String api_key_str, String api_secret_str, String search_str) {
        this.activity = activity;
        this.tapaculo = tapaculo;
        this.api_key_str = api_key_str;
        this.api_secret_str = api_secret_str;
        this.search_str = search_str;
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

    public void requestHttp() {

        /*
        TODO : 센서값 표시할 것, 전체적인 알고리즘 손볼 것
         */

        activity.setVisibility(false, 7);
        activity.setVisibility(false, 8);
        activity.setVisibility(false, 9);
        activity.setVisibility(false, 10);
        activity.setVisibility(false, 11);
        activity.setVisibility(false, 12);
        activity.setProgress(activity);

        count += 1;
        if (count >= 2) count = 2;
        Call<GetValues> call = tapaculo.getValues(api_key_str, api_secret_str, search_str);
        call.enqueue(new Callback<GetValues>() {
            @Override
            public void onResponse(Call<GetValues> call, Response<GetValues> response) {
                GetValues result = response.body();
                System.out.println("통신 시도 ...");
                if(result == null) {
                    activity.setVisibility(true, 10);
                    activity.setVisibility(true, 11);
                    activity.setVisibility(true, 12);
                    activity.setTextSize(10, 25);
                    activity.setTextSize(11, 25);
                    System.out.println("통신 실패");
                }
                else if (result.getStatus().equals("true")) {
                    System.out.println("통신 성공");
                    activity.hideProgress();

                    String device_last_update = result.getRows()[0].getLast_update();
                    String device_interval = PreferenceManager.getString(activity, "device_interval");
                    try {
                        isDeviceConnect(device_last_update, device_interval);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Rows_Values[] rows_values = result.getRows();

                    arrayValues = new ArrayList<>();
                    for (int i = 0; i < rows_values.length; i++)
                        arrayValues.add(rows_values[i]);

                    PreferenceManager.setInt(activity, "device_sensor_num", arrayValues.size());
                    int showing_sensor_num = PreferenceManager.getInt(activity, "selected_total_sensor_num");

                    for(int i = 0; i < arrayValues.size(); i++) {
                        if (showing_sensor_num == -1 || showing_sensor_num == 2) { // 센서를 몇 개 보여줄지 아직 세팅하지 않은 경우 or 2개 보여주는 경우
                            activity.setVisibility(true, 1);
                            activity.setVisibility(true, 2);
                        } else if (showing_sensor_num == 0 || showing_sensor_num == 1) {
                            activity.setVisibility(false, 6);
                            activity.setVisibility(false, 4);
                            activity.setVisibility(false, 2);
                            activity.setVisibility(true, 1);
                        }

                        tempSetText(i);

                        /*
                        if (title_data == null || title_data.equals("")) // 최초 접속인 경우
                            setText(0, i, 0);
                        else if (title_data.contains(",")) { // 센서 2개를 선택한 경우
                            temp = new String[2];
                            temp = PreferenceManager.getString(activity, "selected_title_data").split(",");
                            for (int k = 0; k < temp.length; k++)
                                if (temp[k].equals(arrayValues.get(i)[j].getCh_name()))  // 받아온 제목과 동일한지 확인
                                    setText(2, i, k);
                        }
                        else // 센서 1개를 선택한 경우
                            setText(1, i, 0);


                        PreferenceManager.setString(activity, "ch" + j + "_name", arrayValues.get(i)[j].getCh_name());
                         */
                    }
                }
            }

            @Override
            public void onFailure(Call<GetValues> call, Throwable t) {
                System.out.println("통신 실패");
                activity.setVisibility(true, 7);
                activity.setVisibility(true, 11);
                activity.setVisibility(true, 12);
                activity.setTextSize(7, 25);
                activity.setTextSize(11, 25);
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
            activity.setVisibility(true, 11);
            activity.setText("마지막 데이터 전송 시간 : " + localTime, 9);
            activity.setTextSize(8, 25);
            activity.setTextSize(9, 25);
            activity.setTextSize(9, 20);
            //System.out.println("디바이스에서 데이터를 전송하지 못하고 있습니다.\n마지막 데이터 전송 시간 : " + localTime);
        }
        //else
            //System.out.println("디바이스에서 데이터를 제대로 전송 중입니다.\n마지막 데이터 전송 시간 : " + localTime);
    }

    public void tempSetText(int i) {
        switch (i) {
            case 0 :
                activity.setText(PreferenceManager.getString(activity, "ch" + i + "_name_verify"), 1);
                break;
            case 1 :
                activity.setText(PreferenceManager.getString(activity, "ch" + i + "_name_verify"), 2);
                break;
        }
    }

    /*
    public void setText(int num, int i, int j) {
        switch (num) {
            case 0 : // 최초 접속
                switch (j) {
                    case 0 :
                        activity.setText(arrayValues.get(i).getCh_name(), 5);
                        if (arrayValues.get(i)[j].getCh_value().length() == 0)
                            activity.setText(arrayValues.get(i)[j].getCh_value(), 1);
                        else
                            activity.setText(arrayValues.get(i)[j].getCh_value().substring(0, 4), 1);
                        activity.setText(arrayValues.get(i)[j].getCh_unit(), 3);
                        break;
                    case 1 :
                        activity.setText(arrayValues.get(i)[j].getCh_name(), 6);
                        if (arrayValues.get(i)[j].getCh_value().length() == 0)
                            activity.setText(arrayValues.get(i)[j].getCh_value(), 2);
                        else
                            activity.setText(arrayValues.get(i)[j].getCh_value().substring(0, 4), 2);
                        activity.setText(arrayValues.get(i)[j].getCh_unit(), 4);
                        break;
                }
                break;
            case 1 : // 센서 1개 선택
                temp = new String[2];
                temp[0] = PreferenceManager.getString(activity, "selected_title_data"); // 이 경우 1번째 값은 null
                if (temp[0].equals(arrayValues.get(i)[j].getCh_name())) {

                    activity.setVisibility(false, 0);
                    activity.setText(arrayValues.get(i)[j].getCh_name(), 5);
                    if (arrayValues.get(i)[j].getCh_value().length() == 0)
                        activity.setText(arrayValues.get(i)[j].getCh_value(), 1);
                    else
                        activity.setText(arrayValues.get(i)[j].getCh_value().substring(0, 4), 1);
                    activity.setTypeface();
                    activity.setText(arrayValues.get(i)[j].getCh_unit(), 3);

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
                        activity.setText(arrayValues.get(i)[j].getCh_name(), 5);
                        if (arrayValues.get(i)[j].getCh_value().length() == 0)
                            activity.setText(arrayValues.get(i)[j].getCh_value(), 1);
                        else
                            activity.setText(arrayValues.get(i)[j].getCh_value().substring(0, 4), 1);
                        activity.setText(arrayValues.get(i)[j].getCh_unit(), 3);
                        break;
                    case 1 :
                        activity.setText(arrayValues.get(i)[j].getCh_name(), 6);
                        if (arrayValues.get(i)[j].getCh_value().length() == 0)
                            activity.setText(arrayValues.get(i)[j].getCh_value(), 2);
                        else
                            activity.setText(arrayValues.get(i)[j].getCh_value().substring(0, 4), 2);
                        activity.setText(arrayValues.get(i)[j].getCh_unit(), 4);
                        break;
                }
                break;
        }
    }
     */
}
