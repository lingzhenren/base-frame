package com.lingzhenren.demo.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lingzhenren.websocket.WebSocketServerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

// 其他import保持不变

@Slf4j
@ServerEndpoint(value = "/chicken/socket", configurator = WebSocketServerConfig.class)
@Component
public class ChickenSocket {

    // 记录当前在线连接数
    private static final AtomicInteger onlineCount = new AtomicInteger(0);

    // 存放所有在线的客户端，key 为 erp 标识
    private static final Map<String, ChickenSocket> clients = new ConcurrentHashMap<String, ChickenSocket>();

    // 当前会话
    private Session session;

    // 当前用户的erp标识
    private String erp = "";

    // ObjectMapper用于JSON解析
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig conf) throws IOException {
        try {
            Map<String, Object> userProperties = conf.getUserProperties();
            String erp = (String) userProperties.get("erp");
            this.erp = erp;
            this.session = session;
            if (clients.containsKey(this.erp)) {
                // 如果已有连接，先关闭原连接
                clients.get(this.erp).session.close();
                clients.remove(this.erp);
                onlineCount.decrementAndGet();
            }
            clients.put(this.erp, this);
            onlineCount.incrementAndGet();
            log.info("有新连接加入：{}，当前在线人数为：{}", erp, onlineCount.get());
            sendMessage("连接成功", this.session);
        } catch (Exception e) {
            log.error("建立链接错误: {}", e.getMessage(), e);
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            if (clients.containsKey(erp)) {
                // 关闭当前会话并移除
                clients.get(erp).session.close();
                clients.remove(erp);
                onlineCount.decrementAndGet();
            }
            log.info("有一连接关闭：{}，当前在线人数为：{}", this.erp, onlineCount.get());
        } catch (Exception e) {
            log.error("连接关闭错误，错误原因: {}", e.getMessage(), e);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("服务端收到客户端[{}]的消息: {}", this.erp, message);
        try {
            // 处理心跳消息
            if ("ping".equals(message)) {
                sendMessage("pong", session);
                return;
            }

            // 解析消息，假设消息为JSON格式：{"toErp": "xxx", "msg": "消息内容"}
            Map msgMap = objectMapper.readValue(message, Map.class);
            String toErp = (String) msgMap.get("toErp");
            String msg = (String) msgMap.get("msg");

            if (toErp != null && !toErp.isEmpty()) {
                sendPrivateMessage(toErp, msg);
            } else {
                // 如果没有指定目标用户，将提示信息返回给发送者
                sendMessage("消息格式错误，请指定目标用户 toErp", session);
            }
        } catch (Exception e) {
            log.error("处理消息错误: {}", e.getMessage(), e);
            try {
                sendMessage("消息解析错误", session);
            } catch (Exception ex) {
                log.error("发送错误信息失败: {}", ex.getMessage(), ex);
            }
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        log.error("Socket: {} 发生错误, 错误原因: {}", erp, error.getMessage(), error);
        try {
            session.close();
        } catch (Exception e) {
            log.error("onError.Exception: {}", e.getMessage(), e);
        }
    }

    /**
     * 私聊消息，发送消息给指定的用户
     */
    public void sendPrivateMessage(String targetErp, String message) {
        ChickenSocket targetSocket = clients.get(targetErp);
        if (targetSocket != null && targetSocket.session.isOpen()) {
            log.info("服务端转发消息给客户端[{}]，消息内容: {}", targetErp, message);
            targetSocket.sendMessage("来自[" + this.erp + "]的消息: " + message, targetSocket.session);
        } else {
            // 目标用户不在线，反馈给发送者
            log.info("目标用户[{}]不在线", targetErp);
            sendMessage("用户[" + targetErp + "]不在线", this.session);
        }
    }

    /**
     * 指定发送消息
     */
    public void sendMessage(String message, Session session) {
        log.info("服务端给客户端[{}]发送消息: {}", this.erp, message);
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            log.error("客户端[{}]发送消息失败, 消息: {}, 错误原因: {}", this.erp, message, e.getMessage(), e);
        }
    }

    /**
     * 群发消息（如果需要实现群聊功能）
     */
    public void sendMessage(String message) {
        for (Map.Entry<String, ChickenSocket> sessionEntry : clients.entrySet()) {
            String erp = sessionEntry.getKey();
            ChickenSocket socket = sessionEntry.getValue();
            Session session = socket.session;
            log.info("服务端给客户端[{}]发送消息: {}", erp, message);
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("客户端[{}]发送消息失败, 消息: {}, 错误原因: {}", erp, message, e.getMessage(), e);
            }
        }
    }
}
