package com.dekist.radionodepanel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 디바이스 시작시 바로 앱이 켜지도록 함
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent intent1 = new Intent(context, StartSelectActivity.class);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }
    }
}
