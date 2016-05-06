package com.xiaoyunchengzhu.httpapi.net;

import android.content.Context;

import java.util.Map;

/**
 * Created by zhangshiyu on 2016/4/26.
 */
public abstract class HttpCache {



    protected CacheMode cacheMode;
    protected Context context;
    protected HttpCache(Context context, CacheMode cacheMode){
           this.cacheMode=cacheMode;
           this.context=context;
    }

   public abstract   void insert(String key, byte[] value, long expiredTime);
    public abstract   void fetchData(String key,HttpCacheCallBack httpCacheCallBack);
    public abstract void clearAllCache();
    public abstract void update(String key,long expiredTime,byte[] value);
    public abstract void update(String key,byte[] value);
    public abstract void update(String key,long expiredTime);
}
