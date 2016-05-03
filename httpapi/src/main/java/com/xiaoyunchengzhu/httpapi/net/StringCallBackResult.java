package com.xiaoyunchengzhu.httpapi.net;

/**
 * Created by zhangshiyu on 2016/4/25.
 */
public abstract class StringCallBackResult extends CallBackResult<String> {

    public StringCallBackResult()
    {
        type=CallBackType.STRING_CALLBACKRESULT;
    }

    @Override
    public void onDownloadProgress(long currentSize, long totalSize, double progress) {

    }

    @Override
    public void onUpLoadProgress(long currentSize, long totalSize, double progress) {

    }
}
