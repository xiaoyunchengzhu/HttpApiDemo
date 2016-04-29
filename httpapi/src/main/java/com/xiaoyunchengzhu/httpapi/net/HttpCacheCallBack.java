package com.xiaoyunchengzhu.httpapi.net;

/**,
 * Created by zhangshiyu on 2016/4/26.
 */
public interface HttpCacheCallBack {

    void getData(String key, byte[] value);
    void newData(String key);
}
