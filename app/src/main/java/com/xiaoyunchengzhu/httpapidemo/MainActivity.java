package com.xiaoyunchengzhu.httpapidemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiaoyunchengzhu.httpapi.http.HttpApi;
import com.xiaoyunchengzhu.httpapi.http.LogManger;
import com.xiaoyunchengzhu.httpapi.net.APIManager;
import com.xiaoyunchengzhu.httpapi.net.Api;
import com.xiaoyunchengzhu.httpapi.net.BitmapCallBackResult;
import com.xiaoyunchengzhu.httpapi.net.CacheMode;
import com.xiaoyunchengzhu.httpapi.net.DownLoadCallBackResult;
import com.xiaoyunchengzhu.httpapi.net.HttpMode;
import com.xiaoyunchengzhu.httpapi.net.StringCallBackResult;
import com.xiaoyunchengzhu.httpapi.net.UpLoadCallBackResult;
import java.io.File;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends Activity implements View.OnClickListener{


    @InjectView(R.id.show)
    TextView show;
    @InjectView(R.id.get)
    Button get;
    @InjectView(R.id.post)
    Button post;
    @InjectView(R.id.downloadfile)
    Button download;
    @InjectView(R.id.cache)
    Button cache;
    @InjectView(R.id.nochache)
    Button nocache;
    @InjectView(R.id.uploadFile)
    Button upload;
    @InjectView(R.id.https)
    Button https;
    @InjectView(R.id.image)
    Button image;
    @InjectView(R.id.contentUploadFile)
    Button  contentUpload;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        APIManager.getInstance().init(this);
        ButterKnife.inject(this);
        get.setOnClickListener(this);
        post.setOnClickListener(this);
        download.setOnClickListener(this);
        cache.setOnClickListener(this);
        nocache.setOnClickListener(this);
        upload.setOnClickListener(this);
        https.setOnClickListener(this);
        image.setOnClickListener(this);
        contentUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.get:
                //此处默认get方式，默认缓存无缓存。不设置就是默认。
                APIManager.createApi(new HttpApi(Test.URL_GET)).param("name","为什么乱码").execute(new StringCallBackResult() {
                    @Override
                    public void success(Api api, String result) {
                        LogManger.i(result);
                        show.setText(result);
                    }

                    @Override
                    public void failure(Api api, String error) {
                        show.setText(error);
                    }
                });
                break;
            case R.id.post:
                APIManager.createApi(new HttpApi(Test.URL_POST)).param("name", "我们的世界").httpMode(HttpMode.post).execute(new StringCallBackResult() {
                    @Override
                    public void success(Api api, String result) {
                        show.setText(result);
                    }

                    @Override
                    public void failure(Api api, String error) {
                        LogManger.e(error);
                        show.setText(error);
                    }
                });
                break;
            case R.id.cache:
                APIManager.createApi(new HttpApi(Test.URL_CACHE)).cacheMode(CacheMode.is_cache).execute(new StringCallBackResult() {
                    @Override
                    public void success(Api api, String result) {
                        show.setText(result);
                        LogManger.e(result);
                    }

                    @Override
                    public void failure(Api api, String error) {
                        LogManger.e(error);
                        show.setText(error);
                    }
                });
                break;
            case R.id.nochache:
                APIManager.createApi(new HttpApi(Test.URL_NO_CACHE)).param("name", "嗯，可以了").execute(new StringCallBackResult() {
                    @Override
                    public void success(Api api, String result) {
                        show.setText(result);
                        LogManger.e(result);
                    }

                    @Override
                    public void failure(Api api, String error) {
                        LogManger.e(error);
                        show.setText(error);
                    }
                });
                break;
            case R.id.downloadfile:
                //下载
                APIManager.createApi(new HttpApi(Test.URL_DOWNLOAD)).execute(new DownLoadCallBackResult(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test") {

                    @Override
                    public void success(Api api, String result) {
                        LogManger.i("下载成功，路径："+result);
                        show.setText(result);
                    }

                    @Override
                    public void failure(Api api, String error) {
                        LogManger.i("下载失败，："+error);
                        show.setText(error);
                    }

                    @Override
                    public void onDownloadProgress(long currentSize, long totalSize, double progress) {
                        LogManger.i("下载中，进度：currentSize:"+currentSize+"--totalSize:"+totalSize+"--progress:"+(int)(progress*100)+"%");
                        show.setText("进度：" + (int) (progress*100)+"%");
                    }
                });
                break;
            case R.id.uploadFile:
                APIManager.createApi(new HttpApi(Test.URL_FORM_UPLOAD)).param("file",new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/image1ps.jpg")).param("key1","value1").execute(new UpLoadCallBackResult() {
                    @Override
                    public void success(Api api, String result) {
                        LogManger.i("上传成功，：" + result);
                        show.setText("上传成功" + result);
                    }

                    @Override
                    public void failure(Api api, String error) {
                        LogManger.i(error);
                        show.setText("上传失败" + error);
                    }

                    @Override
                    public void onUpLoadProgress(long currentSize, long totalSize, double progress) {
                        LogManger.i("上传中，进度：currentSize:" + currentSize + "--totalSize:" + totalSize + "--progress:" + (int) (progress * 100) + "%");
                        show.setText("进度：" + (int) (progress * 100) + "%");
                    }
                });

                break;
            case R.id.https:

                break;
            case R.id.image:
                APIManager.createApi(new HttpApi(Test.URL_IMAGE)).execute(new BitmapCallBackResult() {
                    @Override
                    public void success(Api api, Bitmap result) {
                        show.setBackground(new BitmapDrawable(result));
                    }

                    @Override
                    public void failure(Api api, String error) {
                        show.setText(error);
                    }
                });
                break;
            case R.id.contentUploadFile:
                APIManager.createApi(new HttpApi(Test.URL_CONTENT_UPLOAD)).content(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/image1ps.jpg")).execute(new UpLoadCallBackResult() {
                    @Override
                    public void success(Api api, String result) {
                        LogManger.i("上传成功，：" + result);
                        show.setText("上传成功" + result);
                    }

                    @Override
                    public void failure(Api api, String error) {
                        LogManger.i(error);
                        show.setText("上传失败" + error);
                    }

                    @Override
                    public void onUpLoadProgress(long currentSize, long totalSize, double progress) {
                        LogManger.i("上传中，进度：currentSize:" + currentSize + "--totalSize:" + totalSize + "--progress:" + (int) (progress * 100) + "%");
                        show.setText("进度：" + (int) (progress * 100) + "%");
                    }
                });
                break;
        }

    }
}
