package com.lingzhenren.web.trace;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @description: 对该类作用进行描述
 * @author: yanghaitao
 * @date: 2025/6/19
 */
@Component
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String traceId = httpServletRequest.getHeader(TraceIdConstants.TRACE_ID);
        if (StringUtils.isEmpty(traceId)){
            traceId = TraceIdContext.generateTraceId();
        }
        TraceIdContext.setTraceId(traceId);
        filterChain.doFilter(servletRequest, servletResponse);
        TraceIdContext.clearTraceId();
    }
}
