package com.xiaoyunchengzhu.httpapi.net;



/**
 * Created by zhangshiyu on 2016/4/19.
 */
public abstract class DownLoadCallBackResult extends CallBackResult<String> {



    private String downloadPath;

    public DownLoadCallBackResult(String downloadPath)
    {
        type=CallBackType.DOWNLOAD_CALLBACKRESULT;
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
