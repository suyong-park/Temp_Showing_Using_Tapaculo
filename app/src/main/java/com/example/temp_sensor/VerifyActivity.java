package com.example.temp_sensor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);

        //PreferenceManager.clear(VerifyActivity.this); // 테스트 목적의 코드 라인

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        EditText api_key = (EditText) findViewById(R.id.api_key_enter);
        EditText api_secret = (EditText) findViewById(R.id.api_secret_enter);
        EditText MAC = (EditText) findViewById(R.id.MAC_enter);
        EditText admin = (EditText) findViewById(R.id.admin_enter);
        Button button = (Button) findViewById(R.id.start_btn);

        if(PreferenceManager.getBoolean(VerifyActivity.this, "is_first_connect")) { // 최초 접속이 아닌 경우
            api_key.setText(PreferenceManager.getString(VerifyActivity.this, "api_key_str"));
            api_secret.setText(PreferenceManager.getString(VerifyActivity.this, "api_secret_str"));
            MAC.setText(PreferenceManager.getString(VerifyActivity.this, "mac_str"));
            admin.setText(String.valueOf(PreferenceManager.getInt(VerifyActivity.this, "admin_value")));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String api_key_str = api_key.getText().toString();
                String api_secret_str = api_secret.getText().toString();
                String mac_str = MAC.getText().toString();
                int admin_value = Integer.parseInt(admin.getText().toString());

                AlertDialog.Builder builder = new MaterialAlertDialogBuilder(VerifyActivity.this);

                if(api_key_str.isEmpty() || api_secret_str.isEmpty() || mac_str.isEmpty()) {
                    builder.setTitle("Enter all of Information.")
                            .setMessage("Please enter your Information")
                            .setPositiveButton(getResources().getString(R.string.positive_alert), null)
                            .show();
                    return;
                }

                Connect_Tapaculo tapaculo = Request.getRetrofit().create(Connect_Tapaculo.class);
                Call<GetInfo> call = tapaculo.getInfo(api_key_str, api_secret_str, mac_str);
                call.enqueue(new Callback<GetInfo>() {
                    @Override
                    public void onResponse(Call<GetInfo> call, Response<GetInfo> response) {

                        GetInfo result = response.body();

                        if(result == null) {
                            builder.setTitle("Fail")
                                    .setMessage("Status Fail. Please Recheck your value.")
                                    .setPositiveButton(getResources().getString(R.string.positive_alert), null)
                                    .show();
                            return;
                        }

                        if(result.getStatus().equals("true")) {

                            Sensors[] sensors = result.getSensors();
                            Channels[] channels;

                            PreferenceManager.setString(VerifyActivity.this, "api_key_str", api_key_str);
                            PreferenceManager.setString(VerifyActivity.this, "api_secret_str", api_secret_str);
                            PreferenceManager.setString(VerifyActivity.this, "mac_str", mac_str);
                            PreferenceManager.setInt(VerifyActivity.this, "admin_value", admin_value);
                            PreferenceManager.setBoolean(VerifyActivity.this, "is_first_connect", true);

                            ArrayList<Channels[]> arrayChannels = new ArrayList<>();
                            ArrayList<Sensors> arraySensors = new ArrayList<>();

                            for(int i = 0; i < sensors.length; i++) {
                                channels = sensors[i].getChannels();
                                arraySensors.add(sensors[i]);
                                arrayChannels.add(channels);
                            }

                            Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                            intent.putExtra("Channels", arrayChannels);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<GetInfo> call, Throwable t) {
                        builder.setTitle("Fail")
                                .setMessage("Communication Fail. Check internet.")
                                .setPositiveButton(getResources().getString(R.string.positive_alert), null)
                                .show();
                        return;
                    }
                });
            }
        });
    }

}
