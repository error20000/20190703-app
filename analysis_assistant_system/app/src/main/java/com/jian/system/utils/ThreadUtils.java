package com.jian.system.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {

    static ExecutorService pool = Executors.newSingleThreadExecutor();
    static ExecutorService pools = new ThreadPoolExecutor(20, 20, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1000));

    /**
     * 单线程池
     * @param runnable
     */
    public static void execute(Runnable runnable){
        pool.execute(runnable);
    }

    /**
     * 多线程池
     * @param runnable
     */
    public static void executes(Runnable runnable){
        pools.execute(runnable);
    }
}
