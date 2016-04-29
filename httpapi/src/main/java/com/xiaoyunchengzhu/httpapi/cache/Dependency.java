package com.xiaoyunchengzhu.httpapi.cache;

import java.util.Date;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public class Dependency {

    private long expirationTime = 600 * 1000;
    private Date lastModified; //最后一次修改时间
    private Date newTime;//
    public Dependency()
    {
        lastModified=new Date();
        newTime=new Date();
    }
    /**
     *
     * @param expirationTime 单位秒
     */
    public Dependency(long expirationTime)
    {
        lastModified=new Date();
        newTime=new Date();
        this.expirationTime=expirationTime*1000;
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
    public void afterUpdate()
    {
        lastModified=new Date();
    }

}
