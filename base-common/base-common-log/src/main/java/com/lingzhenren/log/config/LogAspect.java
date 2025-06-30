package com.lingzhenren.log.config;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/6/19
 */
@Aspect
@Slf4j
@Component
public class LogAspect {

    @Pointcut("execution(* com.lingzhenren.*.controller.*Controller.*(..)) || execution(* com.lingzhenren.*.service.*Service.*(..))")
    public void pointCut(){}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object[] reqArgs = pjp.getArgs();
        String req = new Gson().toJson(reqArgs);
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        String methodName = methodSignature.getDeclaringType().getName() + "." + methodSignature.getName();
        log.info("{},req:{}", methodName, req);
        Long startTime = System.currentTimeMillis();
        Object responseObj = pjp.proceed();
        String resp = new Gson().toJson(responseObj);
        Long endTime = System.currentTimeMillis();
        log.info("{},response:{},costTime:{}", methodName, resp, endTime - startTime);
        return responseObj;
    }
}
