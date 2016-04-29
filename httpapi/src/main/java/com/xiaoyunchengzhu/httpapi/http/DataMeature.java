package com.xiaoyunchengzhu.httpapi.http;

import com.xiaoyunchengzhu.httpapi.net.APIManager;
import com.xiaoyunchengzhu.httpapi.net.HttpHeader;
import com.xiaoyunchengzhu.httpapi.net.HttpParam;
import com.xiaoyunchengzhu.httpapi.net.SystemApiBean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by zhangshiyu on 2016/4/20.
 */
public class DataMeature {


    /** 将传递进来的参数拼接成 url get请求 */
    public static String createUrlFromParams(String url, Map<String, String> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0) sb.append("&");
            else sb.append("?");
            for (Map.Entry<String, String> urlParams : params.entrySet()) {
                String urlValue = URLEncoder.encode(urlParams.getValue(), "UTF-8");
                sb.append(urlParams.getKey()).append("=").append(urlValue).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 加头部配置
     * @param buider
     * @param httpHeader
     * @return
     */
    public static Request.Builder getBuider(Request.Builder buider,HttpHeader httpHeader) {
        Headers.Builder headers = new Headers.Builder();

        for (String headerKey : httpHeader.getAllKey()) {
            headers.add(headerKey,httpHeader.get(headerKey));
        }
        buider.headers(headers.build());
        return buider;
    }

    /**
     * 如果有文件上传则回调进度
     * @param systemApiBean
     * @param okhttpCallbackAdapter
     * @return
     */
    public static RequestBody postRequstBody(SystemApiBean systemApiBean, final OkhttpCallbackAdapter okhttpCallbackAdapter)
    {
        RequestBody requestBody=null;
        //内容为字符串
        if (systemApiBean.getContent()!=null)
        {
            requestBody=RequestBody.create(MediaType.parse("text/json; charset=utf-8"),systemApiBean.getContent() );
        }
        /**
         * 内容为byte[]
         */
        if (systemApiBean.getBytesContent()!=null||systemApiBean.getBytesContent().length>0)
        {
            requestBody=RequestBody.create(MediaType.parse("text/json; charset=utf-8"),systemApiBean.getBytesContent() );
        }
        //内容为文件
        if (systemApiBean.getFileContent()!=null)
        {
            requestBody=new ProgressRequestBody(RequestBody.create(MediaType.parse("text/x-markdown"), systemApiBean.getFileContent()), new ProgressBodyListenner() {
                @Override
                public void onProgress(final long currentSize, final long totalSize, final double progress) {
                    APIManager.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            okhttpCallbackAdapter.onProgress(currentSize, totalSize, progress);
                        }
                    });

                }
            });
        }
        /**
         * 表单提交
         */
        if (systemApiBean.getHttpParam().getFiles().size()>0||systemApiBean.getHttpParam().getMap().size()>0)
        {
            requestBody=new ProgressRequestBody(DataMeature.generateMultipartRequestBody(systemApiBean.getHttpParam()), new ProgressBodyListenner() {
                @Override
                public void onProgress(final long currentSize, final long totalSize, final double progress) {
                    APIManager.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            okhttpCallbackAdapter.onProgress(currentSize, totalSize, progress);
                        }
                    });
                }
            });
        }
        return requestBody;
    }

    /**
     * HttpParam  表单 处理
     * @param httpParam
     * @return
     */
    private static RequestBody generateMultipartRequestBody(HttpParam httpParam) {
        if (httpParam.getFiles().isEmpty()) {
            //表单提交，没有文件
            FormBody.Builder bodyBuilder = new FormBody.Builder();

            for (String key : httpParam.getMap().keySet()) {
                bodyBuilder.add(key, httpParam.getMap().get(key));
            }
            return bodyBuilder.build();
//            RequestUtfBody.Builder bodyBuilder = new RequestUtfBody.Builder();
//
//            for (String key : httpParam.getMap().keySet()) {
//                bodyBuilder.add(key, httpParam.getMap().get(key));
//            }
//            return bodyBuilder.build();
        } else {
            //表单提交，有文件
            MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            //拼接键值对
            if (!httpParam.getMap().isEmpty()) {
                for (Map.Entry<String, String> entry : httpParam.getMap().entrySet()) {
                    multipartBodybuilder.addFormDataPart(entry.getKey(), entry.getValue());
                }
            }
            //拼接文件
            for (String entry : httpParam.getFiles().keySet()) {
                MediaType mediaType= MediaType.parse("text/x-markdown; charset=utf-8");
                RequestBody fileBody = RequestBody.create(mediaType,httpParam.getFiles().get(entry));
                multipartBodybuilder.addFormDataPart(entry, entry, fileBody);
            }
            return multipartBodybuilder.build();
        }
    }


}
