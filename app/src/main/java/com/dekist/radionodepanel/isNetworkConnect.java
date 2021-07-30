package com.dekist.radionodepanel;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class isNetworkConnect extends ConnectivityManager.NetworkCallback {

    private Context context;
    private NetworkRequest networkRequest;
    private ConnectivityManager connectivityManager;

    public isNetworkConnect(Context context) {
        this.context = context;

        networkRequest = new NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .build();

        this.connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void register() {
        this.connectivityManager.registerNetworkCallback(networkRequest, this);
    }

    public void unregister() {
        this.connectivityManager.unregisterNetworkCallback(this);
    }

    @Override
    public void onAvailable(@NonNull Network network) {
        super.onAvailable(network);
        // 네트워크 연결시 동작

        System.out.println("네트워크 연결됨");
        float volume = PreferenceManager.getFloat(this.context, "network_on_volume");

        ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, (int) volume);
        toneGen.startTone(ToneGenerator.TONE_PROP_BEEP2);

        Toast.makeText(this.context, "네트워크가 정상적으로 연결되었습니다.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLost(@NonNull Network network) {
        super.onLost(network);
        // 네트워크 끊김시 동작

        System.out.println("네트워크 끊김");
        float volume = PreferenceManager.getFloat(this.context, "network_off_volume");

        ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, (int) volume);
        toneGen.startTone(ToneGenerator.TONE_CDMA_ABBR_ALERT);

        Toast.makeText(this.context, "네트워크가 끊겼습니다.", Toast.LENGTH_LONG).show();
    }
}

