package com.daemon1993.dokhttp.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Daemon1993 on 16/4/11.
 */
public class DExecutorUtil {
    //创建一个使用单个 worker 线程的 Executor，以无界队列方式来运行该线程。
    private static ExecutorService pool = Executors.newSingleThreadExecutor();


    public static void addTask(Runnable runnable){
        pool.execute(runnable);
    }

}
