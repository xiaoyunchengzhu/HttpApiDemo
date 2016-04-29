package com.xiaoyunchengzhu.httpapi.util;


import com.xiaoyunchengzhu.httpapi.net.DownLoadCallBackResult;
import com.xiaoyunchengzhu.httpapi.net.HttpDownload;
import com.xiaoyunchengzhu.httpapi.net.HttpResult;

/**
 * Created by zhangshiyu on 2016/4/29.
 */
public class HttpDownloadImpl extends HttpDownload {


    private DownloadUtil downloadUtil;
    @Override
    public void addtask(HttpResult result, final DownLoadCallBackResult downLoadCallBackResult) {
             downloadUtil=new DownloadUtil(downLoadCallBackResult);
             downloadUtil.post(result,downLoadCallBackResult.getDownloadPath());

    }
}
