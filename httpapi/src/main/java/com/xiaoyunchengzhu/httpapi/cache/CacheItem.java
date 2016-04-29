package com.xiaoyunchengzhu.httpapi.cache;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public class CacheItem {

    private byte[] data;
    private String key;
    private Dependency dependency;

    public CacheItem(byte[] data,String key,Dependency dependency)
    {
        this.data=data;
        this.key=key;
        this.dependency=dependency;
    }
    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        dependency.afterUpdate();
        this.data = data;
    }

    public String getKey() {
        return key;
    }

    public Dependency getDependency() {
        return dependency;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
