package com.xiaoyunchengzhu.httpapi.net;

import java.io.File;

import javax.net.ssl.SSLSocketFactory;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public class SystemApiBean {

    protected HttpMode httpMode;
    protected HttpParam httpParam;
    protected HttpHeader httpHeader;
    protected String url;
    protected long connectTimeout;
    private SSLSocketFactory sslSocketFactory;
    private String content;//post提交方式
    private byte[] bytesContent=new byte[0];//post提交方式
    private File fileContent;//内容提交为File，post方式提交

    public File getFileContent() {
        return fileContent;
    }

    public void setFileContent(File fileContent) {
        this.fileContent = fileContent;
    }

    public byte[] getBytesContent() {
        return bytesContent;
    }

    public void setBytesContent(byte[] bytesContent) {
        this.bytesContent = bytesContent;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return sslSocketFactory;
    }

    public void setSslSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.sslSocketFactory = sslSocketFactory;
    }

    public long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public HttpMode getHttpMode() {
        return httpMode;
    }

    public void setHttpMode(HttpMode httpMode) {
        this.httpMode = httpMode;
    }

    public HttpParam getHttpParam() {
        return httpParam;
    }

    public void setHttpParam(HttpParam httpParam) {
        this.httpParam = httpParam;
    }

    public HttpHeader getHttpHeader() {
        return httpHeader;
    }

    public void setHttpHeader(HttpHeader httpHeader) {
        this.httpHeader = httpHeader;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
