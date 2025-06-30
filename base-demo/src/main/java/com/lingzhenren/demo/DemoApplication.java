package com.lingzhenren.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/6/9
 */
@SpringBootApplication
@ComponentScan(value = "com.lingzhenren")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
