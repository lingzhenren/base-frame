package com.lingzhenren.web.config;

import com.lingzhenren.web.trace.TraceIdFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/6/19
 */
@Configuration
public class FilterConfig {

    @Resource
    private TraceIdFilter traceIdFilter;
    @Bean
    public FilterRegistrationBean registerTraceFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(traceIdFilter);
        registration.addUrlPatterns("/*");
        registration.setName("traceIdFilter");
        registration.setOrder(1);
        return registration;
    }
}
