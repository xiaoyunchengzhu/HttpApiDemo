package com.xiaoyunchengzhu.httpapi.net;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangshiyu on 2016/4/18.
 * 请求参数
 */
public class HttpParam implements Serializable {


    private ConcurrentHashMap<String, File> files;
    private ConcurrentHashMap<String ,String> map;

   public HttpParam()
   {
       inite();
   }
    public HttpParam(HttpParam httpParam)
    {
        inite();
        files.putAll(httpParam.getFiles());
        map.putAll(httpParam.getMap());
    }
    public HttpParam(String key,String value)
    {
        inite();
        put(key, value);
    }
    private  void  inite()
    {
        files=new ConcurrentHashMap<>();
        map=new ConcurrentHashMap<>();
    }
    public void put(HttpParam httpParam)
    {
        files.putAll(httpParam.getFiles());
        map.putAll(httpParam.getMap());
    }
    public void put(String key,String value)
    {
        map.put(key,value);
    }
    public void put(String key,File value)
    {
        files.put(key,value);
    }
    public void removeString(String key)
    {
        if (key!=null&&map.containsKey(key))
        map.remove(key);
    }
    public void removeBytes(String key)
    {
        if (key!=null&&files.containsKey(key))
            files.remove(key);
    }

    public ConcurrentHashMap<String, File> getFiles() {
        return files;
    }

    public void setFiles(ConcurrentHashMap<String,File> bytes) {
        this.files = bytes;
    }

    public ConcurrentHashMap<String, String> getMap() {
        return map;
    }

    public void setMap(ConcurrentHashMap<String, String> map) {
        this.map = map;
    }
}
