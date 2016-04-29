package com.xiaoyunchengzhu.httpapi.net;



/**
 * Created by zhangshiyu on 2016/4/19.
 */
public abstract class DownLoadCallBackResult implements CallBackResult<String> {


    private String downloadPath;

    public DownLoadCallBackResult(String downloadPath)
    {
        this.downloadPath=downloadPath;
    }

    public String getDownloadPath()
    {
        return downloadPath;
    }
    @Override
    public void onUpLoadProgress(long currentSize, long totalSize, double progress) {


    }
}
