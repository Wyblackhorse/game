package com.oxo.ball.utils;

import java.util.concurrent.*;

public class ThreadPoolUtil {
    public static ExecutorService threadPool = newFixedThreadPool(Runtime.getRuntime().availableProcessors()*10);


    private static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>());
    }

    public static Future<?> exec(Runnable task){
        return threadPool.submit(task);
    }

}
