package com.example.temp_sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        EditText api_key = (EditText) findViewById(R.id.api_key_enter);
        EditText api_secret = (EditText) findViewById(R.id.api_secret_enter);
        EditText sensor = (EditText) findViewById(R.id.sensor_id_enter);
        Button button = (Button) findViewById(R.id.start_btn);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear_layout);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String api_key_str = api_key.getText().toString();
                String api_secret_str = api_secret.getText().toString();
                String sensor_str = sensor.getText().toString();

                if(api_key_str.isEmpty() || api_secret_str.isEmpty() || sensor_str.isEmpty()) {
                    Snackbar.make(layout, "Please Enter Information.", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Connect_Tapaculo tapaculo = Request.getRetrofit().create(Connect_Tapaculo.class);
                Call<List<GetValues>> call = tapaculo.getValues(api_key_str, api_secret_str, sensor_str);
                call.enqueue(new Callback<List<GetValues>>() {
                    @Override
                    public void onResponse(Call<List<GetValues>> call, Response<List<GetValues>> response) {

                        List<GetValues> result = response.body();
                        System.out.println(result);

                    }

                    @Override
                    public void onFailure(Call<List<GetValues>> call, Throwable t) {
                        System.out.println("Failure!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    }
                });

                Intent intent = new Intent(VerifyActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

}
