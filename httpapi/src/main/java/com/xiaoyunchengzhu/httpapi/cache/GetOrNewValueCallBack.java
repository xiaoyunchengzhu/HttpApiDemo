package com.xiaoyunchengzhu.httpapi.cache;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public interface GetOrNewValueCallBack {

    /**
     * 有值
     * 回调得到值 data
     * @param data
     */
     void getValue(byte[] data);

    /**
     * 没有值，回调key，重新建立cache
     * @param key
     */

     void newVlue(String key);
}
