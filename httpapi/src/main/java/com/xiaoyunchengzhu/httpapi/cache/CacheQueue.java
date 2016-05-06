package com.xiaoyunchengzhu.httpapi.cache;

import com.xiaoyunchengzhu.httpapi.http.LogManger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public class CacheQueue{

    private int maxLength = 100;
    protected Map<String, CacheItem> innerDictionary = new HashMap<String, CacheItem>();
    public CacheQueue()
    {
       CacheQueueManager.add(this);
    }

    public void update(byte[] data,String key,long expired)
    {
        if (innerDictionary.containsKey(key))
        {
            CacheItem cacheItem = innerDictionary.get(key);
            if (data!=null) {
                cacheItem.setData(data);
            }
            if (expired>=0) {
                cacheItem.getDependency().updateeEpirationTime(expired);
            }
            innerDictionary.put(key, createCacheItem(data, key, expired));
        }
    }
    public void update(String key,long expired)
    {
      update(null,key,expired);
    }
    public void update(byte[] data,String key)
    {
        update(data,key,-1);
    }
    public void add(byte[] data,String key,long expired)
    {

        if (innerDictionary.size()>=maxLength) {
            deleteOutIndex();
        }
        deleteExpired();
        if (innerDictionary.containsKey(key))
        {
            innerDictionary.remove(key);
        }
        innerDictionary.put(key, createCacheItem(data, key, expired));
    }

    protected CacheItem createCacheItem(byte[] data,String key,long expired)
    {

        return new CacheItem(data,key,new Dependency(expired));
    }
    protected CacheItem createCacheItem(byte[] data,String key,long lastModifieldTime,long expired)
    {

        return new CacheItem(data,key,new Dependency(lastModifieldTime,expired));
    }

    public void deleteOutIndex()
    {
            Date minDate=new Date();
            String minKey=null;
            for (String key1:innerDictionary.keySet())
            {
                if (minDate.getTime()>innerDictionary.get(key1).getDependency().getNewTime().getTime())
                {
                    minDate=innerDictionary.get(key1).getDependency().getNewTime();
                    minKey=key1;
                }
            }
            remove(minKey);
    }

    public void deleteExpired() {
        Set<String> strings = innerDictionary.keySet();
        if (strings != null&&strings.size()>0) {
            for (String key : strings) {
                CacheItem vCacheItem = innerDictionary.get(key);
                if (vCacheItem.getDependency().isExpired()) {
                    remove(key);
                }
            }
        }
    }
    public void remove(String key)
    {
        if (key!=null)
        {
            if (innerDictionary.containsKey(key))
            {
                innerDictionary.remove(key);
            }
        }
    }
    public void clear()
    {
        innerDictionary.clear();
    }

    public void getOrAddNewValue(String key,GetOrNewValueCallBack getOrNewValueCallBack)
    {


        if (key!=null) {
            if (!innerDictionary.containsKey(key))
            {
                getOrNewValueCallBack.newVlue(key);

            }else {
                LogManger.e("过期时间：" + innerDictionary.get(key).getDependency().getExpirationTime() + "最后修改时间 ："+innerDictionary.get(key).getDependency().getLastModified() +"现在："+new Date());
                LogManger.e("时间差距 ："+String.valueOf(innerDictionary.get(key).getDependency().getLastModified().getTime()-(new Date().getTime()))+"是否过期："+innerDictionary.get(key).getDependency().isExpired());
                if (innerDictionary.get(key).getDependency().isExpired()) {
                    getOrNewValueCallBack.newVlue(key);
                }else {
                    getOrNewValueCallBack.getValue(innerDictionary.get(key).getData());
                }
            }
        }
    }
}
