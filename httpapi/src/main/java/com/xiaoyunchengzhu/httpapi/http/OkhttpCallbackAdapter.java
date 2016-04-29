package com.xiaoyunchengzhu.httpapi.http;

import com.xiaoyunchengzhu.httpapi.net.APIManager;
import com.xiaoyunchengzhu.httpapi.net.Api;
import com.xiaoyunchengzhu.httpapi.net.CallBackResult;
import com.xiaoyunchengzhu.httpapi.net.HttpHeader;
import com.xiaoyunchengzhu.httpapi.net.HttpResult;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by zhangshiyu on 2016/4/21.
 */
public class OkhttpCallbackAdapter implements Callback{

    private CallBackResult callBackResult;
    private Api api;
    public OkhttpCallbackAdapter(Api api, CallBackResult callBackResult)
    {
         this.callBackResult=callBackResult;
        this.api=api;
    }


    @Override
    public void onFailure(Call call, final IOException e) {
        APIManager.getInstance().getHandler().post(new Runnable() {
            @Override
            public void run() {
                callBackResult.failure(api,e.getMessage());
            }
        });

    }

    @Override
    public void onResponse(Call call, final Response response)  {
        HttpResult httpResult=new HttpResult();
        httpResult.setInputStream(response.body().byteStream());
        httpResult.setContentLength(response.body().contentLength());
        HttpHeader httpHeader=new HttpHeader();
        Headers headers = response.headers();
        for (String name:headers.names())
        {
            httpHeader.put(name,headers.get(name));
        }
        httpResult.setHttpHeader(httpHeader);
        httpResult.setProtocol(response.protocol().name());
        httpResult.setResponseCode(response.code());
        httpResult.setUrl(api.getUrl());
             callBackResult.success(api,httpResult);


    }
    public void onProgress(long currentSize, long totalSize, double progress)
    {
        callBackResult.onUpLoadProgress(currentSize,totalSize,progress);
    }
}
