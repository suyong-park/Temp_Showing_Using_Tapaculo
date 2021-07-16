package com.example.temp_sensor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        Intent intent = getIntent();
        ArrayList<Channels> getChannelList = (ArrayList<Channels>) intent.getSerializableExtra("Channels");

        for(int i = 0; i < getChannelList.size(); i++) {

            System.out.println(i + "번째 값 : " + getChannelList.get(i).getCh_value());


        }
    }
}