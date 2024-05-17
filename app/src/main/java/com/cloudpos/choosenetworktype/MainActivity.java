package com.cloudpos.choosenetworktype;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudpos.choosenetworktype.util.NetUtils;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Log.d("handleMessage", "+" + msg);
            textView.setText((String) msg.obj);
        }
    };
    private RadioButton defaultRb;
    private RadioButton wifiRb;
    private RadioButton ethernetRb;
    private RadioButton mobileRb;

    //from ConnectivityManager.TYPE convert to NetworkCapabilities.TYPE
    private static int getTransportType(int networkType) {
        int[] from = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_ETHERNET};
        int[] to = {NetworkCapabilities.TRANSPORT_CELLULAR, NetworkCapabilities.TRANSPORT_WIFI, NetworkCapabilities.TRANSPORT_ETHERNET};
        for (int i = 0; i < from.length; ++i) {
            if (from[i] == networkType) {
                return to[i];
            }
        }
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

        defaultRb = (RadioButton) findViewById(R.id.default_rb);
        wifiRb = (RadioButton) findViewById(R.id.wifi_rb);
        ethernetRb = (RadioButton) findViewById(R.id.ethernet_rb);
        mobileRb = (RadioButton) findViewById(R.id.mobile_rb);
    }

    public void testType(View view) {
        if (defaultRb.isChecked()) {
            new Thread() {
                @Override
                public void run() {
                    handler.obtainMessage(1, "").sendToTarget();
                    handler.obtainMessage(1, "please wait ...").sendToTarget();
                    handler.obtainMessage(1, NetUtils.GetNetIp()).sendToTarget();
                }
            }.start();
        }
        if (wifiRb.isChecked()) {
            chooseType(ConnectivityManager.TYPE_WIFI, true);
        }
        if (ethernetRb.isChecked()) {
            chooseType(ConnectivityManager.TYPE_ETHERNET, true);
        }
        if (mobileRb.isChecked()) {
            chooseType(ConnectivityManager.TYPE_MOBILE, true);
        }
    }

    private void chooseType(int networkType, boolean isAllApply) {
        new ChooseNetThread(this, networkType, isAllApply).start();
    }

    public void onlyWifi(View view) {
        chooseType(ConnectivityManager.TYPE_WIFI, false);
    }

    public void onlyMobile(View view) {
        chooseType(ConnectivityManager.TYPE_MOBILE, false);
    }

    public void onlyEthernet(View view) {
        chooseType(ConnectivityManager.TYPE_ETHERNET, false);
    }

    private void sendToast(Context context, int type) {
        String netType = "";
        if (type == NetworkCapabilities.TRANSPORT_WIFI) {
            netType = "WIFI";
        } else if (type == NetworkCapabilities.TRANSPORT_CELLULAR) {
            netType = "MOBILE";
        } else if (type == NetworkCapabilities.TRANSPORT_ETHERNET) {
            netType = "ETHERNET";
        }
        Toast.makeText(context, "network type = " + netType, Toast.LENGTH_SHORT).show();
    }

    class ChooseNetThread extends Thread {
        Context context;
        private int testType;
        private boolean isAllApply;

        public ChooseNetThread(Context context, int testType, boolean isAllApply) {
            this.context = context;
            this.testType = testType;
            this.isAllApply = isAllApply;
        }

        @Override
        public void run() {
            handler.obtainMessage(1, "").sendToTarget();
            handler.obtainMessage(1, "please wait ...").sendToTarget();
            final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            final int type = getTransportType(testType);
            // Use addTransportType() method set Trans Type
            NetworkRequest.Builder builder = new NetworkRequest.Builder().addTransportType(type);
            NetworkRequest request = builder.build();
            final ConnectivityManager.NetworkCallback callback = new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    super.onAvailable(network);
                    if (isAllApply) {
                        // Use bindProcessToNetwork() methods to make the entire app use a network type
                        if (Build.VERSION.SDK_INT >= 23) {
                            connectivityManager.bindProcessToNetwork(network);
                            sendToast(context, type);
                            handler.obtainMessage(1, NetUtils.GetNetIp()).sendToTarget();
                        } else {
                            ConnectivityManager.setProcessDefaultNetwork(network);
                        }
                    } else {
                        //A network request uses the specified network type
                        try {
                            // use specified network type　do something
                            //HttpURLConnection urlConnection = (HttpURLConnection) network.openConnection(new URL("http://www.baidu.com/"));
                            String result = NetUtils.GetNetIpForNetwork(network);
                            handler.obtainMessage(1, result).sendToTarget();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    connectivityManager.unregisterNetworkCallback(this);
                }
            };
            connectivityManager.requestNetwork(request, callback);
        }
    }
}
