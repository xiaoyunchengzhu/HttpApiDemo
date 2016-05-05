package com.xiaoyunchengzhu.httpapi.net;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangshiyu on 2016/4/18.
 * 请求头部
 */
public class HttpHeader implements Serializable {


    private ConcurrentHashMap<String,String> headerMap=null;
    private static final String httpDate = "EEE, dd MMM y HH:mm:ss 'GMT'";
    private static   final TimeZone timeZone = TimeZone.getTimeZone("GMT");
    public HttpHeader()
    {
        inite();
    }
    public HttpHeader(String key,String value)
    {
        inite();
        put(key, value);
    }

    private void inite()
    {
        headerMap=new ConcurrentHashMap<>();
    }
    public void setContentType(String contentType )
    {
        headerMap.put("content-type",contentType);
    }
    public void setCookie(Map<String,String> cookie)
    {
        StringBuffer cookies=new StringBuffer();
        for (String key:cookie.keySet()) {

            cookies.append(key+"="+cookie.get(key)+";");
        }

        headerMap.put("Set-Cookie",cookies.toString());
    }
    public void put(String key,String value)
    {
        if (key!=null&value!=null)
        headerMap.put(key,value);
    }
    public void put(HttpHeader httpHeader)
    {
        if (httpHeader!=null)
        headerMap.putAll(httpHeader.headerMap);
    }
    public String get(String key)
    {

        if (key!=null)
        return headerMap.get(key);
        return null;
    }
    public void remove(String key)
    {
        if (key!=null)
          headerMap.remove(key);
    }
    public Set<String> getAllKey()
    {
        return headerMap.keySet();
    }



    public final String toJSONString() {
        JSONObject jsonObject = new JSONObject();
        try {
            for (Map.Entry<String, String> entry : headerMap.entrySet()) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
    public static long parseGMTToMillis(String gmtTime) throws ParseException {
        if (TextUtils.isEmpty(gmtTime)) return 0;
        SimpleDateFormat formatter = new SimpleDateFormat(httpDate, Locale.US);
        formatter.setTimeZone(timeZone);
        Date date = formatter.parse(gmtTime);
        return date.getTime();
    }
    public static String formatMillisToGMT(long milliseconds) {
        Date date = new Date(milliseconds);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(httpDate, Locale.US);
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(date);
    }



}
