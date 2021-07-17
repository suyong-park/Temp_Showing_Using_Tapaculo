package com.example.temp_sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        Intent get_intent = getIntent();
        ArrayList<Channels[]> getChannelList = (ArrayList<Channels[]>) get_intent.getSerializableExtra("Channels");

        TextView data_1 = (TextView) findViewById(R.id.show_data_1);
        TextView data_2 = (TextView) findViewById(R.id.show_data_2);

        for(int i = 0; i < getChannelList.size(); i++) {
            for(int j = 0; j < getChannelList.get(i).length; j++) {
                if(j >= 2)
                    break;
                else if(j == 0)
                    data_1.setText(getChannelList.get(i)[j].getCh_value());
                else if(j == 1)
                    data_2.setText(getChannelList.get(i)[j].getCh_value());
            }
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
}