package com.xiaoyunchengzhu.httpapi.http;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 * Created by zhangshiyu on 2016/4/21.
 */
public class ProgressRequestBody extends RequestBody {

    private RequestBody requestBody;
    private ProgressBodyListenner progressBodyListenner;
    long longth=0;
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {

        ForwardingSink forwardingSink=new ForwardingSink(sink) {
            long currentlength=0;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);

                if (longth==0)
                {
                    longth=contentLength();
                }
                currentlength+=byteCount;
                LogManger.i("currentlenth--------"+currentlength);
                progressBodyListenner.onProgress(currentlength,longth,(double)currentlength/longth);
            }
        };
          BufferedSink bufferedSink =Okio.buffer(forwardingSink);
        requestBody.writeTo(bufferedSink);
        forwardingSink.flush();
        bufferedSink.flush();
    }

    public ProgressRequestBody(RequestBody requestBody,ProgressBodyListenner progressBodyListenner) {
        super();
        this.requestBody=requestBody;
        this.progressBodyListenner=progressBodyListenner;
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }
}
