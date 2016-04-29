package com.xiaoyunchengzhu.httpapi.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by zhangshiyu on 2016/4/29.
 */
public class DownloadService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
