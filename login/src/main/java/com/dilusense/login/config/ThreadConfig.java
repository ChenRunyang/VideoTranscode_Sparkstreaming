package com.dilusense.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class ThreadConfig implements AsyncConfigurer {
    private final int poolSize = 8;    //核心线程池大小
    private final int maxpoolSize = 16;    //最大线程数
    private final int queueCapacity = 32;    //队列容量
    private final int keepAliveSecond =6000;    //超过核心线程数的线程的活跃时间

    @Bean("modelTrain")
    public Executor mergeThreadPoll(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(maxpoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSecond);
        executor.setThreadNamePrefix("Thread-dilu-");//前缀名
        //当池中的线程使用完时，新任务由调用者所在的线程执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return  executor;
    }
}
