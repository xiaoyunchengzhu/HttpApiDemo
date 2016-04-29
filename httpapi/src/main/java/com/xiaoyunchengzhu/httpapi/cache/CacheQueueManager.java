package com.xiaoyunchengzhu.httpapi.cache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangshiyu on 2016/4/19.
 */
public class CacheQueueManager {


    private static List<CacheQueue> cacheQueueList=new ArrayList<>();


    public static void add(CacheQueue cacheQueue)
    {
        cacheQueueList.add(cacheQueue);
    }
    protected static void clearAll()
    {
        for (CacheQueue cacheQueue:cacheQueueList)
        {
            if (cacheQueue.getClass().equals(CacheQueueLocal.class))
            {

            }
            cacheQueue.clear();
        }
    }
    public static void destroy()
    {


    }


}
