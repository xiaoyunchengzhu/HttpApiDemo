package com.xiaoyunchengzhu.httpapi.http;

import com.xiaoyunchengzhu.httpapi.net.Api;
import com.xiaoyunchengzhu.httpapi.net.CallBackResult;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by zhangshiyu on 2016/4/18.
 */
public class HttpApi extends Api<HttpApi> {

    private OkHttpClient okHttpClient;
    private Call call;
    public HttpApi(String url)
    {
        super(url);
        okHttpClient=new OkHttpClient();
    }
    @Override
    public void execu(final CallBackResult callback){
                run(okHttpClient,new OkhttpCallbackAdapter(this,callback));
    }
    @Override
    public void cancel() {
             if (call!=null)
             {
                 call.cancel();
             }

    }
    void run(OkHttpClient client, OkhttpCallbackAdapter callback)  {

        Request.Builder buider= new Request.Builder();

        try {
            manageasamber(client,buider,callback);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        call=client.newCall(buider.build());
           call.enqueue(callback);
    }

    /**
     * 处理所有的装配
     * @param client
     * @param buider
     */
    private void manageasamber(OkHttpClient client,Request.Builder buider,OkhttpCallbackAdapter okhttpCallbackAdapter) throws MalformedURLException {

        if (systemApiBean.getSslSocketFactory()!=null&&new URL(systemApiBean.getUrl()).getProtocol().equals("https"))
        {
            client.newBuilder().sslSocketFactory(systemApiBean.getSslSocketFactory());

        }
        client.newBuilder().connectTimeout(systemApiBean.getConnectTimeout(), TimeUnit.SECONDS);
        switch (systemApiBean.getHttpMode())
        {
            case get:
                //map加到url串后缀
                buider.url(DataMeature.createUrlFromParams(systemApiBean.getUrl(), systemApiBean.getHttpParam().getMap()));
//                buider.put(requestBody);
                break;
            case post:
                buider.url(systemApiBean.getUrl());
                RequestBody requestBody=DataMeature.postRequstBody(systemApiBean,okhttpCallbackAdapter);
                buider.post(requestBody);
                break;

        }
         buider= DataMeature.getBuider(buider,systemApiBean.getHttpHeader());

    }



}
