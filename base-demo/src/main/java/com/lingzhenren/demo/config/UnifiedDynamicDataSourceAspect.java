//package com.lingzhenren.demo.config;
//
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Aspect
//@Component
//public class UnifiedDynamicDataSourceAspect {
//
//    @Autowired
//    private DynamicDataSourceSelector selector;
//
//    @Pointcut("execution(* com.lingzhenren.*.service..*.*(..))")
//    public void serviceLayer() {
//    }
//
//    @Before("serviceLayer()")
//    public void beforeServiceExecution() {
//        String dataSourceKey = selector.determineDataSource();
//        DataSourceContextHolder.set(dataSourceKey);
//    }
//
//    @After("serviceLayer()")
//    public void clearDataSource() {
//        DataSourceContextHolder.clear();
//    }
//}