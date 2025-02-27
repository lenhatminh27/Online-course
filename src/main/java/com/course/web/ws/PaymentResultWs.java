package com.course.web.ws;

import com.course.dto.base.WsResponse;
import com.google.gson.Gson;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static com.course.core.bean.BeanListener.BeanContext.getBean;

@Slf4j
@ServerEndpoint("/payment/result/{tranId}")
public class PaymentResultWs {
    private static final ConcurrentHashMap<Long, Session> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(@PathParam("tranId") Long tranId, Session session) {
        clients.put(tranId, session);
        log.info("Tran " + tranId + " connected for notifications.");
    }

    @OnMessage
    public void onMessage(String message, @PathParam("tranId") Long tranId) {
        Gson gson = getBean(Gson.class.getSimpleName());
        WsResponse wsResponse = gson.fromJson(message, WsResponse.class);
        sendToUser(tranId, wsResponse);
    }


    @OnClose
    public void onClose(@PathParam("tranId") Long tranId) {
        clients.remove(tranId);
        log.info("User " + tranId + " disconnected.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket error: ", throwable);
    }

    public static void sendToUser(Long tranId, WsResponse wsResponse) {
        Gson gson = getBean(Gson.class.getSimpleName());
        Session session = clients.get(tranId);
        if (session != null && session.isOpen()) {
            try {
                assert gson != null;
                session.getBasicRemote().sendText(gson.toJson(wsResponse));
            } catch (IOException e) {
                log.error("Error sending message to user " + tranId, e);
            }
        } else {
            log.warn("Session for user " + tranId + " is not available.");
        }
    }
}

