package com.demo.com.demologin;

import android.content.Context;

/**
 * Created by 杨放 on 2015/12/13.
 */
public class DeviceInfo {

    public DeviceInfo(Context context,String name){
        imei = DeviceUtils.getImei(context);
        ip = DeviceUtils.getIp();
        osv = DeviceUtils.getOsv();
        packageName = DeviceUtils.getPackageName(context);
        time = DeviceUtils.getTime();
        this.username = name;
    }

    public String imei;
    public String ip;
    public String osv;
    public String packageName;
    public String username;
    public String time;
}
