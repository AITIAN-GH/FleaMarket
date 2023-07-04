package com.xu.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author AITIAN
 */
@Slf4j
@Component
@ServerEndpoint("/websocket/{username}")
public class WebSocket {
    private Session session;
    private static final CopyOnWriteArraySet<WebSocket> WEB_SOCKETS =new CopyOnWriteArraySet<>();
    private static final Map<String,Session> SESSION_POOL = new HashMap<String,Session>();

    @OnOpen
    public void onOpen(Session session, @PathParam(value="username") String username) {
        this.session = session;
        WEB_SOCKETS.add(this);
        SESSION_POOL.put(username, session);
        System.out.println(username+"【websocket消息】有新的连接，总数为:"+WEB_SOCKETS.size());
    }

    @OnClose
    public void onClose() {
        WEB_SOCKETS.remove(this);
        System.out.println("【websocket消息】连接断开，总数为:"+WEB_SOCKETS.size());
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("【websocket消息】收到客户端消息:"+message);
    }

    public void sendAllMessage(String message) {
        for(WebSocket webSocket : WEB_SOCKETS) {
            System.out.println("【websocket消息】广播消息:"+message);
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendOneMessage(String userName, String message) {
        System.out.println("【websocket消息】单点消息:"+message);
        Session session = SESSION_POOL.get(userName);
        if (session != null) {
            try {
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(session);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final WebSocket other = (WebSocket) obj;
        return Objects.equals(session, other.session);
    }

}
