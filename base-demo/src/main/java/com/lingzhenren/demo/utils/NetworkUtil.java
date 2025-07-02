package com.lingzhenren.demo.utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络工诊断具类
 */
public class NetworkUtil {

    /**
     * 检查网络是否可用（通过访问指定URL）
     *
     * @return true 表示网络可用，false 表示网络中断或连接失败
     */
    public static boolean isNetworkAvailable() {
//        try {
//            // 可替换为任意稳定的外部网址
//            URL url = new URL("http://www.baidu.com");
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(3000); // 设置连接超时
//            conn.connect();
//
//            int responseCode = conn.getResponseCode();
//            return (responseCode == 200);
//        } catch (IOException e) {
//            return false;
//        }
        return false;
    }

    // 示例：主函数调用
    public static void main(String[] args) {
        if (isNetworkAvailable()) {
            System.out.println("✅ 网络畅通");
        } else {
            System.out.println("❌ 网络中断");
        }
    }
}
