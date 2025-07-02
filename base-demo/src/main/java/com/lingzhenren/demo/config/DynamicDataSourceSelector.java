//package com.lingzhenren.demo.config;
//
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.Socket;
//
//@Component
//public class DynamicDataSourceSelector {
//
//    public String determineDataSource() {
//        // 自动判断当前应使用哪个数据源
//        if (isCloudAvailable()) {
//            return "CLOUD";
//        } else {
//            return "LOCAL";
//        }
//    }
//
//    private boolean isCloudAvailable() {
//        // 实现方式示例：Ping 云端数据库 IP 或调用 health 检查接口
//        try (Socket s = new Socket()) {
//            s.connect(new InetSocketAddress("cloud.mysql.host", 3306), 1000);
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//    }
//}