package com.iot_station_management.iot_station_management.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
// reference: https://medium.com/javarevisited/using-async-schedulers-in-spring-boot-78c15f9df466
public class AsyncConfiguration {
    @Bean
    public ThreadPoolExecutor asyncTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Set the initial number of threads in the pool
        executor.setMaxPoolSize(10); // Set the maximum number of threads in the pool
        executor.setQueueCapacity(25); // Set the queue capacity for holding pending tasks
        executor.setThreadNamePrefix("AsyncTask-"); // Set a prefix for thread names
        executor.initialize();
        return executor.getThreadPoolExecutor();
    }
}
