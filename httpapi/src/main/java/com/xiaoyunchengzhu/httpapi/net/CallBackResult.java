package com.xiaoyunchengzhu.httpapi.net;

/**
 * Created by zhangshiyu on 2016/4/18.
 */
public abstract class CallBackResult<T> {

    protected CallBackType type=CallBackType.CALLBACKRESULT;
   abstract public void success(Api api, T result);
   abstract public void failure(Api api, String error);
   abstract public void onDownloadProgress(long currentSize, long totalSize, double progress);
   abstract public void onUpLoadProgress(long currentSize, long totalSize, double progress);
}
