package com.xiaoyunchengzhu.httpapi.util;

/**
 * Created by zhangshiyu on 2016/4/26.
 */
public interface ConvertCallback {

    void success(byte[] data);
    void failure(String msg);
}
