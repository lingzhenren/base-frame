package com.lingzhenren.demo;

import org.mybatis.spring.annotation.MapperScan;
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
@MapperScan(value = "com.lingzhenren.demo.mapper")
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
