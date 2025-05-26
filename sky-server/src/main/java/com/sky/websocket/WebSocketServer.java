package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/ws/{sid}")
@Slf4j
public class WebSocketServer {

    private final Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        // 关联 session 和 sid
        sessions.put(sid, session);
        log.info("建立连接，sessionId=" + session.getId());
    }

    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.info("收到来自客户端：" + sid + "的信息:" + message);
    }

    @OnClose
    public void onClose(@PathParam("sid") String sid)  {
        log.info("连接断开:" + sid);
        sessions.remove(sid);
    }

    public void sendToAllClient(String message) {
        Collection<Session> session = sessions.values();
        for (Session s : session) {
            try {
                //服务器向客户端发送消息
                s.getBasicRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
