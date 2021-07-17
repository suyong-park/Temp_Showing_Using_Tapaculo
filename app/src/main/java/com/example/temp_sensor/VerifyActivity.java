package com.example.temp_sensor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class VerifyActivity extends AppCompatActivity {

    VerifyActivity verifyActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);

        verifyActivity = VerifyActivity.this;

        //PreferenceManager.clear(VerifyActivity.this); // 테스트 목적의 코드 라인

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        EditText api_key = (EditText) findViewById(R.id.api_key_enter);
        EditText api_secret = (EditText) findViewById(R.id.api_secret_enter);
        EditText MAC = (EditText) findViewById(R.id.MAC_enter);
        EditText admin = (EditText) findViewById(R.id.admin_enter);
        Button button = (Button) findViewById(R.id.start_btn);

        View view = (View) findViewById(R.id.linear_verify);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    downKeyboard();
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
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String api_key_str = api_key.getText().toString();
                String api_secret_str = api_secret.getText().toString();
                String mac_str = MAC.getText().toString();
                int admin_value = Integer.parseInt(admin.getText().toString());

                AlertDialog.Builder builder = new MaterialAlertDialogBuilder(verifyActivity);

                if(api_key_str.isEmpty() || api_secret_str.isEmpty() || mac_str.isEmpty()) {
                    builder.setTitle("Enter all of Information.")
                            .setMessage("Please enter your Information")
                            .setPositiveButton(getResources().getString(R.string.positive_alert), null)
                            .show();
                    return;
                }

                PreferenceManager.setString(verifyActivity, "api_key_str", api_key_str);
                PreferenceManager.setString(verifyActivity, "api_secret_str", api_secret_str);
                PreferenceManager.setString(verifyActivity, "mac_str", mac_str);
                PreferenceManager.setInt(verifyActivity, "admin_value", admin_value);
                PreferenceManager.setBoolean(verifyActivity, "is_first_connect", true);

                Intent intent = new Intent(verifyActivity, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void downKeyboard() {

        InputMethodManager inputManager = (InputMethodManager) verifyActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View focusedView = verifyActivity.getCurrentFocus();

        if (focusedView != null)
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
