package com.lingzhenren.demo.utils;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceSwitcher {

    // 数据源 key 常量
    public static final String CLOUD = "cloud";
    public static final String LOCAL = "local";

    /**
     * 安全切换数据源，避免重复 push
     */
    public static void switchTo(String ds) {
        String current = getCurrent();
        if (ds.equals(current)) {
            log.debug("🟢 数据源 [{}] 已在当前上下文中，跳过切换", ds);
            return;
        }

        log.info("🔄 切换数据源: {} -> {}", current, ds);
        DynamicDataSourceContextHolder.push(ds);
    }

    /**
     * 清理当前数据源上下文
     */
    public static void clear() {
        log.debug("🧹 清除当前数据源上下文");
        DynamicDataSourceContextHolder.clear();
    }

    /**
     * 获取当前线程使用的数据源
     */
    public static String getCurrent() {
        return DynamicDataSourceContextHolder.peek();
    }

    /**
     * 当前是否使用 cloud 数据源
     */
    public static boolean isUsingCloud() {
        return CLOUD.equals(getCurrent());
    }

    /**
     * 当前是否使用 local 数据源
     */
    public static boolean isUsingLocal() {
        return LOCAL.equals(getCurrent());
    }
}
