package com.xiaoyunchengzhu.httpapi.http;

import android.util.Log;

/**
 * Created by zhangshiyu on 2016/4/18.
 */
public class LogManger {

    private static String tag="com.zhangshiy";
    private static boolean isDebug=true;

    public static void e(String msg)
    {
        if (isDebug) {
            if (msg!=null) {
                Log.e(tag, msg);
            }else {
                Log.e(tag, "null");
            }
        }else {

        }
    }
    public static void i(String msg)
    {
        if (isDebug) {
            if (msg!=null) {
                Log.i(tag, msg);
            }else {
                Log.i(tag, "null");
            }
        }else {

        }
    }
    public static void d(String msg)
    {
        if (isDebug) {
            if (msg!=null) {
                Log.d(tag, msg);
            }else {
                Log.d(tag, "null");
            }
        }else {

        }
    }
    public static void v(String msg)
    {
        if (isDebug) {
            if (msg!=null) {
                Log.v(tag, msg);
            }else {
                Log.v(tag, "null");
            }
        }else {

        }
    }
    public static void w(String msg)
    {
        if (isDebug) {
            if (msg!=null) {
                Log.w(tag, msg);
            }else {
                Log.w(tag, "null");
            }
        }else {

        }
    }

    
}
