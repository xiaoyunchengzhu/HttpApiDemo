package com.xiaoyunchengzhu.httpapi.net;

/**
 * Created by zhangshiyu on 2016/4/25.
 */
public abstract class StringCallBackResult implements CallBackResult<String> {


    @Override
    public void onDownloadProgress(long currentSize, long totalSize, double progress) {

    }

    @Override
    public void onUpLoadProgress(long currentSize, long totalSize, double progress) {

    }
}
