package com.lingzhenren.web.trace;

import org.slf4j.MDC;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/6/19
 */
public class TraceIdContext {


    public static final ThreadLocal<String> CURRENT_TRACE_ID = new InheritableThreadLocal<>();

    public static String generateTraceId() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }

    public static String getTraceId() {
        return MDC.get(TraceIdConstants.TRACE_ID);
    }

    public static void setTraceId(String traceId) {
        MDC.put(TraceIdConstants.TRACE_ID, traceId);
    }

    public static void clearTraceId() {
        CURRENT_TRACE_ID.set(null);
        CURRENT_TRACE_ID.remove();
    }
}
