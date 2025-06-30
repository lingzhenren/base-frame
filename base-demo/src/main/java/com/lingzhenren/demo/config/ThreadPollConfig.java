package com.lingzhenren.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/6/17
 */
@Configuration
public class ThreadPollConfig {

    @Bean(name = "testThreadPoll")
    public ThreadPoolExecutor getTestThreadPoll(){
        CustomNameThreadFactory testPoll = new CustomNameThreadFactory("test");
        return new ThreadPoolExecutor(20, 100,
                10, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(40),
                testPoll,
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
