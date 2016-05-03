package com.xiaoyunchengzhu.httpapi.net;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public abstract class UpLoadCallBackResult extends CallBackResult<String> {

    public UpLoadCallBackResult()
    {
        type=CallBackType.UPLOADCALLBACKRESULT;
    }


    @Override
    public void onDownloadProgress(long currentSize, long totalSize, double progress) {
    }


}
