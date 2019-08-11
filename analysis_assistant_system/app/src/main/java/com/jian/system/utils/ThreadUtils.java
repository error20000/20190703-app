package com.jian.system.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadUtils {

    static ExecutorService pool = Executors.newSingleThreadExecutor();

    public static void execute(Runnable runnable){
        pool.execute(runnable);
    }
}
