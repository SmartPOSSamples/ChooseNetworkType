package com.cloudpos.choosenetworktype.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * create by rf.w 19-5-14下午12:48
 */
public class NetWorkStateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {

                    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {

                    } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {

                    }

                } else {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI) {

                    } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {

                    } else if (info.getType() == ConnectivityManager.TYPE_ETHERNET) {

                    }
                }
            }
        }

    }
}

