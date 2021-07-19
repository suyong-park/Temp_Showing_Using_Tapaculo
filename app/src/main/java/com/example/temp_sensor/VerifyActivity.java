package com.example.temp_sensor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActivity extends AppCompatActivity {

    VerifyActivity verifyActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        verifyActivity = VerifyActivity.this;

        //PreferenceManager.clear(VerifyActivity.this); // 테스트 목적의 코드 라인

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        EditText api_key = (EditText) findViewById(R.id.api_key_enter);
        EditText api_secret = (EditText) findViewById(R.id.api_secret_enter);
        EditText MAC = (EditText) findViewById(R.id.MAC_enter);
        EditText admin = (EditText) findViewById(R.id.admin_enter);
        EditText refresh = (EditText) findViewById(R.id.refresh_enter);
        Button button = (Button) findViewById(R.id.start_btn);
        Button tapaculo = (Button) findViewById(R.id.tapaculo_btn);

        tapaculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_tapaculo = new Intent(Intent.ACTION_VIEW);
                intent_tapaculo.setData(Uri.parse("https://s2.dev.tapaculo365.com/"));
                startActivity(intent_tapaculo);
            }
        });

        View view = (View) findViewById(R.id.linear_verify);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Request.downKeyboard(verifyActivity);
                    return true;
                }
                return false;
            }
        });

        if(PreferenceManager.getBoolean(verifyActivity, "is_first_connect")) { // 최초 접속이 아닌 경우
            api_key.setText(PreferenceManager.getString(verifyActivity, "api_key_str"));
            api_secret.setText(PreferenceManager.getString(verifyActivity, "api_secret_str"));
            MAC.setText(PreferenceManager.getString(verifyActivity, "mac_str"));
            admin.setText(String.valueOf(PreferenceManager.getInt(verifyActivity, "admin_value")));
            refresh.setText(String.valueOf(PreferenceManager.getInt(verifyActivity, "refresh_value")));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String api_key_str = api_key.getText().toString();
                String api_secret_str = api_secret.getText().toString();
                String mac_str = MAC.getText().toString();
                String refresh_value_str = refresh.getText().toString();
                String admin_value_str = admin.getText().toString();

                AlertDialog.Builder builder = new MaterialAlertDialogBuilder(verifyActivity);

                if(api_key_str.isEmpty() || api_secret_str.isEmpty() || mac_str.isEmpty() || admin_value_str.isEmpty() || refresh_value_str.isEmpty()) {
                    builder.setTitle("Enter all of Information.")
                            .setMessage("Please enter your Information")
                            .setPositiveButton(getResources().getString(R.string.positive_alert), null)
                            .setCancelable(false)
                            .show();
                    return;
                }
                if(admin_value_str.length() != 4) {
                    Snackbar.make(view, "관리자 인증번호는 4자리입니다.", Snackbar.LENGTH_LONG).show();
                    return;
                }

                Connect_Tapaculo tapaculo = Request.getRetrofit().create(Connect_Tapaculo.class);
                Call<GetInfo> call = tapaculo.getInfo(api_key_str, api_secret_str, mac_str);
                call.enqueue(new Callback<GetInfo>() {

                    @Override
                    public void onResponse(Call<GetInfo> call, Response<GetInfo> response) {

                        GetInfo result = response.body();
                        if (result == null) {
                            builder.setTitle("Fail")
                                    .setMessage("Status Fail. Please Recheck your value.")
                                    .setPositiveButton(getResources().getString(R.string.positive_alert), null)
                                    .setCancelable(false)
                                    .show();
                            return;
                        }

                        if (result.getStatus().equals("true")) {
                            int refresh_value = Integer.parseInt(refresh_value_str);
                            int admin_value = Integer.parseInt(admin_value_str);

                            PreferenceManager.setString(verifyActivity, "api_key_str", api_key_str);
                            PreferenceManager.setString(verifyActivity, "api_secret_str", api_secret_str);
                            PreferenceManager.setString(verifyActivity, "mac_str", mac_str);
                            PreferenceManager.setInt(verifyActivity, "admin_value", admin_value);
                            PreferenceManager.setInt(verifyActivity, "refresh_value", refresh_value);
                            PreferenceManager.setBoolean(verifyActivity, "is_first_connect", true);

                            Intent intent = new Intent(verifyActivity, MainActivity.class);
                            startActivity(intent);
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
                                .setCancelable(false)
                                .show();
                        return;
                    }
                });
            }
        });
    }
}
