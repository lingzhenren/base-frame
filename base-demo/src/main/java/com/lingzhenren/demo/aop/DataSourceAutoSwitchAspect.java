package com.lingzhenren.demo.aop;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.lingzhenren.demo.utils.DataSourceSwitcher;
import com.lingzhenren.demo.utils.NetworkUtil;
import com.lingzhenren.demo.utils.PingUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class DataSourceAutoSwitchAspect {

    @Pointcut("execution(* com.lingzhenren.*.service..*.*(..))")
    public void serviceMethods() {
    }

    @Around("serviceMethods()")
    public Object switchDataSourceIfNeeded(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            if (PingUtil.isReachable()) {
                DataSourceSwitcher.switchTo(DataSourceSwitcher.CLOUD);
            } else {
                DataSourceSwitcher.switchTo(DataSourceSwitcher.LOCAL);
            }
            return joinPoint.proceed();
        } finally {
            DataSourceSwitcher.clear();
        }
    }

}
