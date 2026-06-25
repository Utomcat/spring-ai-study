package com.ranyk.spring.ai.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * CLASS_NAME: VirtualThreadConfiguration.java
 *
 * @author ranyk
 * @version V1.0
 * @description: Java21 虚拟线程配置类
 * @date: 2026-06-24
 */
@Configuration
public class VirtualThreadConfiguration {

    /**
     * 创建一个虚拟线程调度对象
     *
     * @return 虚拟线程调度对象, {@link ScheduledExecutorService}
     */
    @Bean(name = "virtualThreadScheduler")
    public ScheduledExecutorService virtualThreadScheduler() {
        return Executors.newSingleThreadScheduledExecutor();
    }

    /**
     * 创建一个虚拟线程执行器
     *
     * @return 虚拟线程执行器对象, {@link ExecutorService}
     */
    @Bean(name = "virtualThreadExecutor")
    public ExecutorService virtualThreadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
