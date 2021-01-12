package com.cry.videotransform.config;

import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

public class ThreadConfig {
    private final int poolSize = 8;    //核心线程池大小
    private final int maxpoolSize = 16;    //最大线程数
    private final int queueCapacity = 32;    //队列容量
    private final int keepAliveSecond =60;    //活跃时间

    @Bean("mergeThreadPoll")
    public Executor mergeThreadPoll(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(maxpoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSecond);
        executor.setThreadNamePrefix("myThread-zgl-");//前缀名
        //当池中的线程使用完时，新任务由调用者所在的线程执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return  executor;
    }

}
