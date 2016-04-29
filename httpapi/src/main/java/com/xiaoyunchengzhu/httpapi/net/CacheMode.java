package com.xiaoyunchengzhu.httpapi.net;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public enum CacheMode {

    is_cache,//正常使用cache，内存->本地->网络
    memory_cache,//仅仅使用内存缓存  内存->网络
    requst_failed_cache,//请求网络失败请求缓存
    no_cache
}
