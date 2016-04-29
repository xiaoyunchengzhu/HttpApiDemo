package com.xiaoyunchengzhu.httpapi.cache;

import android.content.Context;

import com.xiaoyunchengzhu.httpapi.net.CacheMode;
import com.xiaoyunchengzhu.httpapi.net.HttpCache;
import com.xiaoyunchengzhu.httpapi.net.HttpCacheCallBack;

/**
 * Created by zhangshiyu on 2016/4/26.
 */
public class HttpCacheImp extends HttpCache {

    private CacheQueue cacheQueue=null;
    public HttpCacheImp(Context context, CacheMode cacheMode) {
        super(context, cacheMode);
        switch (cacheMode)
        {
            case memory_cache:
                cacheQueue=new CacheQueue();
                break;
            case is_cache:
                cacheQueue=new CacheQueueLocal(context);
                break;

        }

    }

    @Override
    public void insert(String key, byte[] value, long expiredTime) {
        cacheQueue.add(value,key,expiredTime);
    }

    @Override
    public void fetchData(final String key, final HttpCacheCallBack httpCacheCallBack) {
        cacheQueue.getOrAddNewValue(key, new GetOrNewValueCallBack() {
            @Override
            public void getValue(byte[] data) {
                httpCacheCallBack.getData(key,data);
            }

            @Override
            public void newVlue(String key) {
                httpCacheCallBack.newData(key);
            }

            });
    }

    @Override
    public void clearAllCache() {
        CacheQueueManager.clearAll();
    }
}
