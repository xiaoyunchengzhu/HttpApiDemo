package com.xiaoyunchengzhu.httpapi.net;

/**
 * Created by zhangshiyu on 2016/4/18.
 */
public interface CallBackResult<T> {

    void success(Api api, T result);
    void failure(Api api, String error);
    void onDownloadProgress(long currentSize, long totalSize, double progress);
    void onUpLoadProgress(long currentSize, long totalSize, double progress);
}
