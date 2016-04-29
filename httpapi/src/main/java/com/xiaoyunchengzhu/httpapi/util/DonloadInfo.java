package com.xiaoyunchengzhu.httpapi.util;

/**
 * Created by zhangshiyu on 2016/4/29.
 */
public class DonloadInfo {


    private int threadId;
    private String url;
    private String filPath;
    private long startIndex;
    private long currentIndedx;
    private long endIndex;
    private long contentLength;

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFilPath() {
        return filPath;
    }

    public void setFilPath(String filPath) {
        this.filPath = filPath;
    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public long getCurrentIndedx() {
        return currentIndedx;
    }

    public void setCurrentIndedx(long currentIndedx) {
        this.currentIndedx = currentIndedx;
    }

    public long getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
    }
}
