package com.xiaoyunchengzhu.httpapi.net;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshiyu on 2016/4/18.
 */
public class APIManager {


    private static HttpHeader header; //所有请求统一头部。
    private static HttpParam param;  //所有请求同意参数
    private static long connectTimeout=36000;             //所有请求统一连接超时
    private static APIManager apiManager;
    private static Context context1;
    private Handler handler=new Handler(Looper.getMainLooper());

    public Handler getHandler()
    {
        return handler;
    }
    /**
     * 初始化用
     * @return
     */
    public static APIManager getInstance()
    {
        if (apiManager == null) {
            synchronized (APIManager.class) {
                if (apiManager == null) {
                    apiManager = new APIManager();
                }
            }
        }
        return apiManager;
    }

    public Context getContext() {
        return context1;
    }

    private APIManager()
    {
        header=new HttpHeader();
        param=new HttpParam();
    }


    public APIManager addHeader(HttpHeader httpHeader)
    {
        header.put(httpHeader);
        return this;
    }
    public APIManager addParam(HttpParam httpParam)
    {
        param=httpParam;
        return this;
    }
    public APIManager addParam(String key,String value)
    {
        param.put(key,value);
        return this;
    }
    public APIManager setConnectTimeOut(long timeOut)
    {
        connectTimeout=timeOut;
        return this;
    }
    public APIManager init(Context context)
    {
        context1=context;
        return this;
    }


    public  HttpHeader getHeader() {
        return header;
    }

    public  long getConnectTimeout() {
        return connectTimeout;
    }

    public  HttpParam getParam() {
        return param;
    }

    private static List<Api> list=new ArrayList<>();
    public static Api createApi(Api api)
    {
        list.add(api);

        return api;
    }

    void cancel(String marks){

        for (Api api:list)
        {
                 if (api.getMarks().equals(marks))
                 {
                     api.cancel();
                 }
        }
    }


}
