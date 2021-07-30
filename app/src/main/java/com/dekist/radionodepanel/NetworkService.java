package com.dekist.radionodepanel;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

public class NetworkService extends Service {

    isNetworkConnect isNetworkConnect;

    public NetworkService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            if(isNetworkConnect == null){
                isNetworkConnect = new isNetworkConnect(getApplicationContext());
                isNetworkConnect.register();
            }

        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            if(isNetworkConnect != null)
                isNetworkConnect.unregister();
    }
}