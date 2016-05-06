package com.xiaoyunchengzhu.httpapi.cache;

import java.util.Date;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public class Dependency {

    private long expirationTime = 3600000;
    private Date lastModified; //最后一次修改时间
    private Date newTime;//
    public Dependency()
    {
        lastModified=new Date();
        newTime=new Date();
    }
    /**
     *
     * @param expirationTime 单位毫秒
     */
    public Dependency(long expirationTime)
    {
        lastModified=new Date();
        newTime=new Date();
        this.expirationTime=expirationTime;
    }
    public Dependency(long lastModifieldTime,long expirationTime)
    {
        lastModified=new Date(lastModifieldTime);
        newTime=new Date();
        this.expirationTime=expirationTime;
    }
    public void updateeEpirationTime(long expirationTime)
    {
        this.expirationTime=expirationTime;
    }

    /**
     * 过期
     * @return
     */
    public boolean isExpired()
    {
        return (new Date().getTime()-lastModified.getTime())>=expirationTime;
    }

    public Date getNewTime() {
        return newTime;
    }

    public Date getLastModified()
    {
        return lastModified;
    }
    public long getExpirationTime()
    {
        return expirationTime;
    }
    public void afterUpdate()
    {
        lastModified=new Date();
    }

}
