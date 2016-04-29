package com.xiaoyunchengzhu.httpapi.http;

/**
 * Created by zhangshiyu on 2016/4/21.
 */
public interface ProgressBodyListenner {

     void onProgress(long currentSize, long totalSize, double progress);

}
