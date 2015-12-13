package com.demo.com.demologin;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 杨放 on 2015/12/12.
 */
public class WorkService extends Service {

    private static final int SUCCESS = 1;
    private static final int FAIRLE = 2;
    public static final String TAG = MainActivity.class.getSimpleName();
    private ArrayList<Object> mArray = new ArrayList<>();
    private ArrayList<String> ip = new ArrayList<>();
    private ArrayList<String> username = new ArrayList<>();
    private ArrayList<String> time = new ArrayList<>();
    private String loginName;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(getApplicationContext(), "提交成功", 0).show();
                    break;
                case FAIRLE:
                    Toast.makeText(getApplicationContext(), "提交失败", 0).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        loginName = intent.getStringExtra("name");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        getData();
        MyTimerTask timerTask = new MyTimerTask();
        Timer timer = new Timer(true);
        timer.schedule(timerTask, 0, 5000);//定时每秒执行一次
        super.onCreate();
    }


    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            sendTimesMessages();
        }
    }

    /**
     * 生成这次请求所需要的集合
     */
    private void getData() {
        //生成这次的数据
        DeviceInfo info = new DeviceInfo(this,loginName);
        //设置一次就好
        if (mArray.isEmpty()) {
            mArray.add(info.imei);
            mArray.add(info.osv);
            mArray.add(info.packageName);
        }
        ip.add(info.ip);
        username.add(info.username);
        time.add(info.time);
        mArray.add(ip);
        mArray.add(username);
        mArray.add(time);

    }

    /**
     * 根据当前列表生成请求需要的包装类
     *
     * @return
     */
    private HttpRequestWrapper<DeviceInfo> getWrapper() {
        HttpRequestWrapper<DeviceInfo> httpRequestWrapper = new HttpRequestWrapper<DeviceInfo>() {
            @Override
            protected String convertToJson() {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("imei", mArray.get(0));
                    jsonObject.put("osv", mArray.get(1));
                    jsonObject.put("packageName", mArray.get(2));
                    JSONArray jsonIp = new JSONArray();
                    ArrayList<String> o1 = (ArrayList<String>) mArray.get(3);
                    for (String str : o1) {
                        jsonIp.put(str);
                    }
                    jsonObject.put("ip", jsonIp);
                    JSONArray jsonUsername = new JSONArray();
                    ArrayList<String> o2 = (ArrayList<String>) mArray.get(4);
                    for (String str : o2) {
                        jsonUsername.put(str);
                    }
                    jsonObject.put("username", jsonUsername);
                    JSONArray jsonTime = new JSONArray();
                    ArrayList<String> o3 = (ArrayList<String>) mArray.get(5);
                    for (String str : o3) {
                        jsonTime.put(str);
                    }
                    jsonObject.put("time", jsonTime);
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate :", e);
                }
                return jsonObject.toString();
            }
        };
        return httpRequestWrapper;
    }

    /**
     * 联网发送数据
     */
    private void sendTimesMessages() {
        URL urlPath;
        try {
            urlPath = new URL(Contants.url);
            HttpRequestWrapper<DeviceInfo> wrapper = getWrapper();
            String jsonParam = wrapper.getjson();
            HttpURLConnection connection = (HttpURLConnection) urlPath.openConnection();
            connection.setConnectTimeout(5000);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            OutputStream os = connection.getOutputStream();
            if(jsonParam != null) {
                os.write(jsonParam.getBytes());
            }
            os.close();
            int code = connection.getResponseCode();
            if (code == 200) {
                handler.sendEmptyMessage(SUCCESS);
                Log.e("TAG", "CODE-----" + code);
            }
        } catch (Exception e) {
            e.printStackTrace();
            handler.sendEmptyMessage(FAIRLE);
            Log.e("TAG", "联网失败————-");
        }
    }

}
