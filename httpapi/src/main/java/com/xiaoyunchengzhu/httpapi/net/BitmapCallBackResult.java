package com.xiaoyunchengzhu.httpapi.net;

import android.graphics.Bitmap;

/**
 * Created by zhangshiyu on 2016/4/25.
 */
public abstract class BitmapCallBackResult implements CallBackResult<Bitmap> {

    @Override
    public void onDownloadProgress(long currentSize, long totalSize, double progress) {

    }

    @Override
    public void onUpLoadProgress(long currentSize, long totalSize, double progress) {

    }
}
