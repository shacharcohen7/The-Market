package com.example.application.WebSocketUtil;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class ClientEndPoint implements WebSocketListener {
    private Session session;
    Set<UI> uiList = ConcurrentHashMap.newKeySet();
    private final String memberID;

    public ClientEndPoint(String memberID) {
        //this.ui = ui;
        this.memberID = memberID;
    }

    public void addUI(UI ui){
        uiList.add(ui);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        //WebSocketListener.super.onWebSocketError(cause);
        System.out.println(cause.getCause().toString());
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        //WebSocketListener.super.onWebSocketClose(statusCode, reason);
        System.out.println(reason);
    }

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
        session.setIdleTimeout(Duration.ZERO);
        //

        //WebSocketListener.super.onWebSocketConnect(session);
    }

    @Override
    public void onWebSocketText(String message) {
        System.out.println("Received message: " + message);
        //notifyMessageListeners(message);
        for (UI ui: uiList) {
            ui.access(() -> Notification.show(message));
        }

        //WebSocketListener.super.onWebSocketText(message);
    }

    public void close() {
        if (session != null && session.isOpen()) {
            session.close();
        }

    }
}
