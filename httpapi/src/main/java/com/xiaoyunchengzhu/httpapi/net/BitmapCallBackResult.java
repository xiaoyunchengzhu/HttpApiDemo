package com.xiaoyunchengzhu.httpapi.net;

import android.graphics.Bitmap;

/**
 * Created by zhangshiyu on 2016/4/25.
 */
public abstract class BitmapCallBackResult extends CallBackResult<Bitmap> {

    public BitmapCallBackResult()
    {
        type=CallBackType.BITMAP_CALLBACKRESULT;
    }
    @Override
    public void onDownloadProgress(long currentSize, long totalSize, double progress) {

    }

    @Override
    public void onUpLoadProgress(long currentSize, long totalSize, double progress) {

    }
}
