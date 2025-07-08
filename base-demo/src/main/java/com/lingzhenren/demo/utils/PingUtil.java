package com.lingzhenren.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;

public class PingUtil {

    private static final Logger logger = LoggerFactory.getLogger(PingUtil.class);
    private static final int DEFAULT_TIMEOUT_MS = 100;
    private static final String DEFAULT_HOST = "172.22.85.69";
    /**
     * Ping 指定地址，判断是否可达
     *
     * @return 是否可达
     */
    public static boolean isReachable() {
        try {
            InetAddress address = InetAddress.getByName(DEFAULT_HOST);
            return address.isReachable(DEFAULT_TIMEOUT_MS);
        } catch (IOException e) {
            logger.error("PING失败 {}",e.getMessage());
            return false;
        }
    }
}
