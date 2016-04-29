package com.xiaoyunchengzhu.httpapi.util;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangshiyu on 2016/4/21.
 */
public class ThreadPool {


    // 线程池中默认线程的个数为5
    private static   int worker_num = 5;
   private ExecutorService executorService;
    private ConcurrentLinkedQueue<Runnable> taskQueue=new ConcurrentLinkedQueue<>();
    private static ThreadPool downloadThreadPool;

    private ThreadPool() {
        this(5);
    }

    // 创建线程池,worker_num为线程池中工作线程的个数
    private ThreadPool(int worker_num) {
             ThreadPool.worker_num=worker_num;
    }

    // 单态模式，获得一个默认线程个数的线程池
    public static ThreadPool getThreadPool() {
        return getThreadPool(worker_num);
    }

    // 单态模式，获得一个指定线程个数的线程池,worker_num(>0)为线程池中工作线程的个数
    // worker_num<=0创建默认的工作线程个数
    public static ThreadPool getThreadPool(int worker_num1) {
        if (worker_num1 <= 0)
            worker_num1 = ThreadPool.worker_num;
        if (downloadThreadPool == null)
            downloadThreadPool = new ThreadPool(worker_num1);
        downloadThreadPool.executorService= Executors.newFixedThreadPool(worker_num);
        return downloadThreadPool;
    }

    public void add(Runnable task) {
        synchronized (taskQueue) {
            taskQueue.add(task);
            taskQueue.notify();
        }
    }
    public void execute(Runnable task) {
       executorService.execute(task);

    }
    /**
     * execute all task
     */
    public void execute()
    {
        for (Runnable runnable:taskQueue)
        {
            executorService.execute(runnable);
            taskQueue.remove(runnable);
        }

    }

    /**
     * execute this
     * @param task
     */
    public void execute(List<Runnable> task)
    {
        for (Runnable runnable:task)
        {
            executorService.execute(runnable);
        }
    }

    public void add(List<Runnable> task) {
        synchronized (taskQueue) {
            for (Runnable t : task)
                taskQueue.add(t);
            taskQueue.notify();
        }
    }
    public void shutdown() {
        executorService.shutdown();
        taskQueue.clear();// 清空任务队列
    }
    // 返回工作线程的个数
    public int getWorkThreadNumber() {
        return worker_num;
    }
}
