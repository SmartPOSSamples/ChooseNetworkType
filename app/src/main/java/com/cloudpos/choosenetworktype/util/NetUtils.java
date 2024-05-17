package com.cloudpos.choosenetworktype.util;

import android.net.Network;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetUtils {

    public static String GetNetIp() {
        String IP = "";
        try {
            String address = "https://api.ip.sb/geoip";
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String tmpString = "";
                StringBuilder retJSON = new StringBuilder();
                while ((tmpString = reader.readLine()) != null) {
                    retJSON.append(tmpString + "\n");
                }
                JSONObject jsonObject = new JSONObject(retJSON.toString());
//                String code = jsonObject.getString("code");
                Log.e("", "Prompt：" + retJSON.toString());
                if (jsonObject.has("ip")) {
                    IP = jsonObject.getString("ip") + "(" + jsonObject.getString("country")
                            + jsonObject.getString("timezone") + "timezone"
                            + jsonObject.getString("region") + jsonObject.getString("city")
                            + jsonObject.getString("isp") + ")";

                    Log.e("Prompt", "IP is:" + IP);
                }
            } else {
                IP = "";
                Log.e("Prompt", "Can not get IP!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            IP = "";
            Log.e("Prompt", "Can not get IP!" + e.toString());
        }
        return IP;
    }


    public static String GetNetIpForNetwork(Network network) {
        String IP = "";
        try {
            String address = "https://api.ip.sb/geoip";
            HttpURLConnection connection = (HttpURLConnection) network.openConnection(new URL(address));
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.7 Safari/537.36");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String tmpString = "";
                StringBuilder retJSON = new StringBuilder();
                while ((tmpString = reader.readLine()) != null) {
                    retJSON.append(tmpString + "\n");
                }
                JSONObject jsonObject = new JSONObject(retJSON.toString());
                Log.e("", "Prompt：" + retJSON.toString());
                if (jsonObject.has("ip")) {
                    IP = jsonObject.getString("ip") + "(" + jsonObject.getString("country")
                            + jsonObject.getString("timezone") + "timezone"
                            + jsonObject.getString("region") + jsonObject.getString("city")
                            + jsonObject.getString("isp") + ")";

                    Log.e("Prompt", "IP is:" + IP);
                }
            } else {
                IP = "";
                Log.e("Prompt", "Can not get IP!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            IP = "";
            Log.e("Prompt", "Can not get IP!" + e.toString());
        }
        //Log.d("testtime", " network" + network.netId + System.currentTimeMillis());
        return IP;
    }
}