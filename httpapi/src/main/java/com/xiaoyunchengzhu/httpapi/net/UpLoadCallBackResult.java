package com.xiaoyunchengzhu.httpapi.net;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public abstract class UpLoadCallBackResult implements CallBackResult<String> {
    @Override
    public void onDownloadProgress(long currentSize, long totalSize, double progress) {
    }


}
