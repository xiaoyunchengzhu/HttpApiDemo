package com.xiaoyunchengzhu.httpapi.util;

import com.xiaoyunchengzhu.httpapi.http.HttpApi;
import com.xiaoyunchengzhu.httpapi.http.LogManger;
import com.xiaoyunchengzhu.httpapi.net.APIManager;
import com.xiaoyunchengzhu.httpapi.net.Api;
import com.xiaoyunchengzhu.httpapi.net.CallBackResult;
import com.xiaoyunchengzhu.httpapi.net.DownLoadCallBackResult;
import com.xiaoyunchengzhu.httpapi.net.HttpResult;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

/**
 * Created by zhangshiyu on 2016/4/29.
 */
public class DownloadUtil {

    private  DownloadSqlliteHelper downloadSqlliteHelper;
    private  long download=1024*1024*8;//每段8M
    private DownLoadCallBackResult downLoadCallBackResult;
    public DownloadUtil(DownLoadCallBackResult downLoadCallBackResult)
    {
        this.downLoadCallBackResult=downLoadCallBackResult;
        downloadSqlliteHelper=new DownloadSqlliteHelper(APIManager.getInstance().getContext());
    }
    public void  post(HttpResult httpResult,String localPath)
    {
        List<DonloadInfo> query = downloadSqlliteHelper.query(httpResult.getUrl());
        if (query.size()>0)
        {
            //多线程记录有未下载完成的。继续下载；
            addtask(query);

        }else {
            //启动新的线程池，多线程
            //载；
           int threadCount=(int)(httpResult.getContentLength()/download);
            long currentSize=0;
            for (int i=0;i<threadCount;i++)
            {
                DonloadInfo donloadInfo=new DonloadInfo();
                donloadInfo.setUrl(httpResult.getUrl());
                donloadInfo.setFilPath(localPath);
                donloadInfo.setContentLength(httpResult.getContentLength());
                donloadInfo.setThreadId(i);
                donloadInfo.setStartIndex(currentSize);
                donloadInfo.setCurrentIndedx(currentSize);
                donloadInfo.setEndIndex(currentSize + download);
                currentSize+=download;
                downloadSqlliteHelper.insert(donloadInfo);
                query.add(donloadInfo);
            }
            addtask(query);

        }
    }
     long downloadcurentSize=0;
    int completcount=0;
    public void addtask( final List<DonloadInfo> query)
    {

        for (final DonloadInfo donloadInfo:query) {
            APIManager.createApi(new HttpApi(donloadInfo.getUrl())).head("Range", donloadInfo.getCurrentIndedx() + "-" + donloadInfo.getEndIndex()).execute(new CallBackResult<HttpResult>() {
                @Override
                public void success(Api api, HttpResult result) {
                           donwload(donloadInfo, result.getInputStream(), api, new DownLoadCallBackResult(donloadInfo.getFilPath()) {
                               @Override
                               public void success(Api api, String result) {
                                   completcount+=1;
                                   if (completcount==query.size())
                                   {
                                       downLoadCallBackResult.success(api,result);
                                   }
                               }

                               @Override
                               public void failure(Api api, String error) {
                                   downLoadCallBackResult.failure(api,error);
                               }

                               @Override
                               public void onDownloadProgress(long currentSize, long totalSize, double progress) {
                                   downloadcurentSize+=currentSize;
                                   downLoadCallBackResult.onDownloadProgress(downloadcurentSize,donloadInfo.getContentLength(),(double)downloadcurentSize/donloadInfo.getContentLength());
                                   LogManger.i("下载 ---" + downloadcurentSize);
                               }
                           });
                }

                @Override
                public void failure(Api api, String error) {
                    downLoadCallBackResult.failure(api,error);
                }

                @Override
                public void onDownloadProgress(long currentSize, long totalSize, double progress) {
                    downLoadCallBackResult.onDownloadProgress(downloadcurentSize,donloadInfo.getContentLength(),(double)downloadcurentSize/donloadInfo.getContentLength());
                }

                @Override
                public void onUpLoadProgress(long currentSize, long totalSize, double progress) {
                    downLoadCallBackResult.onUpLoadProgress(currentSize,totalSize,progress);
                }
            });
        }
    }

    public void donwload( final DonloadInfo donloadInfo,final InputStream inputStream, final Api api, final DownLoadCallBackResult downLoadCallBackResult)
    {
        ThreadPool.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                RandomAccessFile randomAccessFile=new RandomAccessFile(donloadInfo.getFilPath(),"rwd");
                    randomAccessFile.setLength(donloadInfo.getContentLength());
                    randomAccessFile.seek(donloadInfo.getCurrentIndedx());
                    long curreantSize=0;
                    byte[] buffer=new byte[1024];
                    int read=0;
                    while ((read=inputStream.read(buffer))>0)
                    {
                        randomAccessFile.write(buffer, 0, read);
                        curreantSize+=read;
                        final long finalCurreantSize = curreantSize;
                        //修改下载完成点
                       donloadInfo.setCurrentIndedx(finalCurreantSize);

                        APIManager.getInstance().getHandler().post(new Runnable() {
                            @Override
                            public void run() {


                                downLoadCallBackResult.onDownloadProgress(finalCurreantSize,donloadInfo.getContentLength(), (double)finalCurreantSize /donloadInfo
                                .getContentLength());
                            }
                        });
                    }

                    //下载完，删除记录
                    downloadSqlliteHelper.delete(donloadInfo);
                    APIManager.getInstance().getHandler().post(new Runnable() {
                        @Override
                        public void run() {


                            downLoadCallBackResult.success(api, donloadInfo.getFilPath());
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    downloadSqlliteHelper.update(donloadInfo);
                } catch (IOException e) {
                    e.printStackTrace();
                    //异常退出，则保存没下载完成的记录。
                    downloadSqlliteHelper.update(donloadInfo);
                }
            }
        });

    }

}
