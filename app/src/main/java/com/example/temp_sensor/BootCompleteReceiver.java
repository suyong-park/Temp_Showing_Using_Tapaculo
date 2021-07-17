package com.example.temp_sensor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent intent1 = new Intent(context, VerifyActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
