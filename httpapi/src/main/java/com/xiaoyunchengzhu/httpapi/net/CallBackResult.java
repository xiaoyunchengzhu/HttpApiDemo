package com.xiaoyunchengzhu.httpapi.net;

/**
 * Created by zhangshiyu on 2016/4/18.
 */
public abstract class CallBackResult<T> {

    protected CallBackType type=CallBackType.CALLBACKRESULT;
   abstract void success(Api api, T result);
   abstract void failure(Api api, String error);
   abstract void onDownloadProgress(long currentSize, long totalSize, double progress);
   abstract void onUpLoadProgress(long currentSize, long totalSize, double progress);
}
