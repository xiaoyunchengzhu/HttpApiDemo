package com.xiaoyunchengzhu.httpapi.net;

import com.google.gson.Gson;
import com.xiaoyunchengzhu.httpapi.cache.HttpCacheImp;
import com.xiaoyunchengzhu.httpapi.http.LogManger;
import com.xiaoyunchengzhu.httpapi.util.ConvertCallback;
import com.xiaoyunchengzhu.httpapi.util.Util;

import java.io.File;
import java.util.Map;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by zhangshiyu on 2016/4/18.
 * Api请求类，抽象类
 */
public abstract class Api<T extends Api> {



    private HttpMode httpMode=HttpMode.get;
    private HttpParam httpParam=new HttpParam();
    private HttpHeader httpHeader=new HttpHeader();
    private String url;
    private Object marks;//标记 Api，用于获取Api
    private long connectTimeout;//默认10s
    protected SystemApiBean systemApiBean;
    protected CacheMode cacheMode=CacheMode.no_cache;
    private HttpCache httpCache;
    private HttpDownload httpDownload;
    private SSLSocketFactory sslSocketFactory;
    private long expired=60000;//缓存统一过期时效一分钟
    private String content=null;
    private byte[] bytesContent=new byte[0];
    private File fileContent=null;
    private Gson gson=new Gson();

    public Api(String url)
    {
        this.url=url;
        connectTimeout=APIManager.getInstance().getConnectTimeout();
        httpParam.put(APIManager.getInstance().getParam());
        httpHeader.put(APIManager.getInstance().getHeader());
        systemApiBean=new SystemApiBean();
    }



    public long getConnectTimeout() {
        return connectTimeout;
    }

    public Object getMarks() {
        return marks;
    }


    public String getUrl() {
        return url;
    }

    public HttpMode getHttpMode() {
        return httpMode;
    }
    public T param(HttpParam param)
    {
        httpParam.put(param);
        return (T) this;
    }

    public T updateExpired(long expired)
    {
        this.expired=expired;
        if (httpCache!=null)
        {
            httpCache.update(gson.toJson(systemApiBean),expired);
        }
        return (T) this;

    }
    public T contentType(String contentType)
    {
        httpHeader.setContentType(contentType);
        return (T) this;
    }
    public T cookie(Map cookie)
    {
        httpHeader.setCookie(cookie);
        return (T) this;
    }
    public T param(String key,String value)
    {
        httpParam.put(key,value);
        return (T) this;
    }
    public T head(String key,String value){
        httpHeader.put(key,value);
        return (T) this;
    }
    public T head(HttpHeader httpHeader){
        httpHeader.put(httpHeader);
        return (T) this;
    }
    public T param(String key,File file)
    {
        httpParam.put(key,file);
        return (T) this;
    }
    public T content(String content)
    {
        this.content=content;
        return (T)this;
    }
    public T  content(byte[] content)
    {
        this.bytesContent=content;
        return (T)this;
    }
    public T  content(File content)
    {
        this.fileContent=content;
        return (T)this;
    }


    public  T sSLSocketFactory(SSLSocketFactory sslSocketFactory)
    {
        this.sslSocketFactory=sslSocketFactory;
        return (T)this;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public    T httpMode(HttpMode httpMode)
    {
        this.httpMode=httpMode;
        return (T)this;
    }
    public   T url(String url){
        this.url=url;
        return (T)this;
    }
    public   T setConnectTimeout(long connectTimeout){
        this.connectTimeout=connectTimeout;
        return (T)this;
    }
    public   T header(HttpHeader headers){
      this.httpHeader.put(headers);
        return (T)this;
    }
    public   T header(String key,String value){
           this.httpHeader.put(key, value);
        return (T)this;
    }
    public   T removeHeader(String key){
              this.httpHeader.remove(key);
        return (T)this;
    }

    public   T marks(java.lang.Object marks){
             this. marks= marks;
        return (T)this;
    }


    public   HttpParam getParam(){

        return httpParam;
    }
    public    HttpHeader getHeader(){

        return httpHeader;
    }

    /**
     * 装到systemApibean里面
     */
    protected    void  assemble(){

        if (bytesContent.length>0||fileContent!=null||content!=null||!httpParam.getFiles().isEmpty())
        {
            httpMode=HttpMode.post;
        }
        systemApiBean.setUrl(url);
        systemApiBean.setConnectTimeout(connectTimeout);
        systemApiBean.setHttpMode(httpMode);
        systemApiBean.setHttpParam(httpParam);
        systemApiBean.setHttpHeader(httpHeader);
        systemApiBean.setSslSocketFactory(sslSocketFactory);
        systemApiBean.setContent(content);
        systemApiBean.setBytesContent(bytesContent);
        systemApiBean.setFileContent(fileContent);
    }
    /**
     *
     * @param callBack  回调方法或者接口
     */
    protected abstract   void execu(CallBackResult callBack);

    public SystemApiBean getSystemApiBean() {
        return systemApiBean;
    }

    /**
     * 取消本次请求
     */
    public  abstract  void cancel();


    public T cacheMode(CacheMode cacheMode)
    {
        this.cacheMode=cacheMode;
        return (T)this;
    }


    public  void assembleExecu(CallBackResult callBackResult){
        assemble();
        execu(callBackResult);
    }
    public  T execute( final CallBackResult callBack){
        //执行前完成装配 便于子类获取装配进行配置
         assemble();
        //下载
        if (callBack.type==CallBackType.DOWNLOAD_CALLBACKRESULT) {

//             httpDownload=new HttpDownloadImpl();
            execu(new CallBackResult<HttpResult>() {
                @Override
                public void success(Api api, HttpResult result) {
//                    httpDownload.addtask(result, (DownLoadCallBackResult) callBack);
                    Util.inputStreamToFile(result.getInputStream(), result.getContentLength(), api, (DownLoadCallBackResult) callBack);
                }

                @Override
                public void failure(Api api, String error) {
                    callBack.failure(api,error);
                }

                @Override
                public void onDownloadProgress(long currentSize, long totalSize, double progress) {
                 callBack.onDownloadProgress(currentSize,totalSize,progress);
                }

                @Override
                public void onUpLoadProgress(long currentSize, long totalSize, double progress) {
                    callBack.onDownloadProgress(currentSize,totalSize,progress);
                }
            });

        }else
        {
                FilterCallbackResult filterCallbackResult = new FilterCallbackResult(callBack);
                Cacheput cacheput = new Cacheput(filterCallbackResult);
                switch (cacheMode) {
                    case no_cache:
                        execu(filterCallbackResult);
                        break;
                    //先请求网络，失败后访问缓存
                    case requst_failed_cache:
                        final Cacheput finalCacheput = cacheput;
                        execu(new CallBackResult<HttpResult>() {
                            @Override
                            public void success(Api api, HttpResult result) {
                                finalCacheput.success(api, result);
                            }

                            @Override
                            public void failure(Api api, String error) {
                                httpCache = new HttpCacheImp(APIManager.getInstance().getContext(), CacheMode.is_cache);
                                exeCache(httpCache, finalCacheput, callBack);
                            }

                            @Override
                            public void onDownloadProgress(long currentSize, long totalSize, double progress) {
                                finalCacheput.onDownloadProgress(currentSize, totalSize, progress);
                            }

                            @Override
                            public void onUpLoadProgress(long currentSize, long totalSize, double progress) {
                                finalCacheput.onUpLoadProgress(currentSize, totalSize, progress);
                            }
                        });
                        break;
                    case is_cache:
                        httpCache = new HttpCacheImp(APIManager.getInstance().getContext(), CacheMode.is_cache);
                        exeCache(httpCache, cacheput, callBack);
                        break;
                    case memory_cache:
                        httpCache = new HttpCacheImp(APIManager.getInstance().getContext(), CacheMode.memory_cache);
                        exeCache(httpCache, cacheput, callBack);
                        break;
                }
            }
     return (T) this;

    }

    //加入缓存 cache 不为空，请求缓存，如果缓存没数据，请求网络，为空，则不使用缓存，直接请求网络。
    private void exeCache(HttpCache httpCache,final CallBackResult cacheput,final CallBackResult callBackResult)
    {

            if (httpCache != null) {
                httpCache.fetchData(gson.toJson(getSystemApiBean()), new HttpCacheCallBack() {
                            @Override
                            public void getData(String key, byte[] value) {
                                   switch (callBackResult.type)
                                   {
                                       case STRING_CALLBACKRESULT:
                                           callBackResult.success(Api.this, new String(value));
                                           break;
                                       case BITMAP_CALLBACKRESULT:
                                           callBackResult.success(Api.this, Util.getBitmapFromBytes(value, 100, 100));
                                           break;
                                       case UPLOADCALLBACKRESULT:

                                           break;
                                       case  CALLBACKRESULT:

                                           break;
                                       default:

                                           break;
                                   }
                            }
                            @Override
                            public void newData(String key) {
                                execu(cacheput);
                            }
                        }
                );

            } else {
                execu(new FilterCallbackResult(callBackResult));
        }
    }

    private class Cacheput extends CallBackResult<HttpResult>
    {

        private CallBackResult callBackResult;

        public Cacheput(CallBackResult callBackResult)
        {
            this.callBackResult=callBackResult;
        }

        @Override
        public void success(final Api api, HttpResult result)  {

            httpCache=new HttpCacheImp(APIManager.getInstance().getContext(),CacheMode.is_cache);

            Util.convertBytes(result.getInputStream(), new ConvertCallback() {
                @Override
                public void success(byte[] data) {
                    httpCache.insert(gson.toJson(api.getSystemApiBean()),data,expired);
                }

                @Override
                public void failure(String msg) {
                    callBackResult.failure(api,msg);
                }
            });

            callBackResult.success(api, result);
        }

        @Override
        public void failure(Api api, String error) {
             callBackResult.failure(api,error);
        }

        @Override
        public void onDownloadProgress(long currentSize, long totalSize, double progress) {
                callBackResult.onDownloadProgress(currentSize,totalSize,progress);
        }

        @Override
        public void onUpLoadProgress(long currentSize, long totalSize, double progress) {
              callBackResult.onUpLoadProgress(currentSize,totalSize,progress);
        }
    }


    private class FilterCallbackResult extends CallBackResult<HttpResult>{

        private CallBackResult callBackResult;
        public FilterCallbackResult( CallBackResult callBackResult)
        {
            this.callBackResult=callBackResult;
        }
        @Override
        public void success(final Api api,  final HttpResult result) {



                Util.convertBytes(result.getInputStream(), new ConvertCallback() {
                    @Override
                    public void success(byte[] data) {
                        switch (callBackResult.type) {
                            case STRING_CALLBACKRESULT:
                                callBackResult.success(Api.this, new String(data));
                                break;
                            case BITMAP_CALLBACKRESULT:
                                callBackResult.success(Api.this, Util.getBitmapFromBytes(data, 100, 100));
                                break;
                            case UPLOADCALLBACKRESULT:

                                break;
                            case CALLBACKRESULT:
                                callBackResult.success(api, result);
                                break;
                            default:

                                break;
                        }

                    }

                    @Override
                    public void failure(String msg) {
                        callBackResult.failure(api, msg);
                    }
                });
            }



        @Override
        public void failure(Api api, String error) {
                callBackResult.failure(api,error);
        }

        @Override
        public void onDownloadProgress(long currentSize, long totalSize, double progress) {
                callBackResult.onDownloadProgress(currentSize,totalSize,progress);
        }

        @Override
        public void onUpLoadProgress(long currentSize, long totalSize, double progress) {
              callBackResult.onUpLoadProgress(currentSize,totalSize,progress);
        }
    }
}
