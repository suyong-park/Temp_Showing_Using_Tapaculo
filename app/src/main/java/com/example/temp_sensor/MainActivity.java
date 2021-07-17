package com.example.temp_sensor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

        Intent get_intent = getIntent();
        ArrayList<Channels[]> getChannelList = (ArrayList<Channels[]>) get_intent.getSerializableExtra("Channels");

        TextView device_info = (TextView) findViewById(R.id.device_location);
        device_info.setText(PreferenceManager.getString(MainActivity.this, "device_info"));

        int showing_sensor_num = PreferenceManager.getInt(MainActivity.this, "sensor_num");

        if(showing_sensor_num == 0 || showing_sensor_num == -1) { // 센서를 몇 개 보여줄지 아직 세팅하지 않은 경우

        }
        else if(showing_sensor_num == 1) { // 센서를 1개 보여주기로 결정한 경우
            // TODO : 센서값을 몇 개를 볼지 그에 대한 액션을 정의. --> visual 속성을 정의
        }
        else if(showing_sensor_num == 2) { // 센서를 2개 보여주기로 결정한 경우

        }

        TextView data_1 = (TextView) findViewById(R.id.show_data_1);
        TextView data_2 = (TextView) findViewById(R.id.show_data_2);

        for(int i = 0; i < getChannelList.size(); i++)
            for(int j = 0; j < getChannelList.get(i).length; j++) {
                if(j >= 2)
                    break;
                else if(j == 0)
                    data_1.setText(getChannelList.get(i)[j].getCh_value() + getChannelList.get(i)[j].getCh_unit());
                else if(j == 1)
                    data_2.setText(getChannelList.get(i)[j].getCh_value() + getChannelList.get(i)[j].getCh_unit());
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

    @Override
    public void onBackPressed() {

        EditText admin_value = new EditText(MainActivity.this);
        LinearLayout container = new LinearLayout(MainActivity.this);
        container.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(60, 0, 60, 0);

        admin_value.setLayoutParams(lp);
        admin_value.setHint("관리자 인증번호 4자리");
        admin_value.setInputType(InputType.TYPE_CLASS_NUMBER);

        container.addView(admin_value);

        AlertDialog.Builder builder = new MaterialAlertDialogBuilder(MainActivity.this);
        builder.setTitle("경고")
                .setMessage("나가려면 관리자 인증번호 4자리를 입력하세요.")
                .setView(container)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int is_admin_value = PreferenceManager.getInt(MainActivity.this, "admin_value");

                        if(admin_value.length() != 4) {
                            Snackbar.make(findViewById(R.id.linear_main), "관리자 인증번호가 다릅니다.", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        else if(is_admin_value != Integer.parseInt(admin_value.getText().toString())) {
                            Snackbar.make(findViewById(R.id.linear_main), "관리자 인증번호가 다릅니다.", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        else
                            Snackbar.make(findViewById(R.id.linear_main), "정말로 나갈까요?", Snackbar.LENGTH_LONG).setAction("네", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }).show();
                    }
                })
                .setNegativeButton("취소", null)
                .show();

    }
}