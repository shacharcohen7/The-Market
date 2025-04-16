package com.example.application.WebSocketUtil;



import com.vaadin.flow.component.UI;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.WebSocketClient;


import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class WebSocketHandler {
    private static WebSocketHandler instance;
    private Map<String, ClientEndPoint> sessionEndpoints = new ConcurrentHashMap<>();
    //private UI ui;

    public synchronized static WebSocketHandler getInstance(){
        if (instance==null){
            instance = new WebSocketHandler() ;
        }
        return instance;

    }

    private WebSocketHandler() {

    }

    private ClientEndPoint getEndPoint(String memberId){
        return sessionEndpoints.get(memberId);
    }

    public void addUI(String memberId,UI ui ){
        if (getEndPoint(memberId)!=null) {
                getEndPoint(memberId).addUI(ui);
            }
        }



    public void openConnection(String memberId , UI ui) {
        try {
            WebSocketClient webSocketClient;
            HttpClient httpClient = new HttpClient();
            webSocketClient = new WebSocketClient(httpClient);
            webSocketClient.start();
            URI serverURI = URI.create("ws://localhost:8080/ws?memberID=" + memberId);
            ClientEndPoint clientEndPoint=getEndPoint(memberId) ;
            if (clientEndPoint==null) {
                clientEndPoint = new ClientEndPoint(memberId);
                clientEndPoint.addUI(ui);
                sessionEndpoints.put(memberId, clientEndPoint);
            }
            else {
                //clientEndPoint = getEndPoint(userId);
                clientEndPoint.addUI(ui);
            }
            webSocketClient.connect(clientEndPoint, serverURI);
        } catch (Exception e) {
            throw new RuntimeException("Failed to open WebSocket", e);
        }
    }

    public void closeConnection(String memberId) {
        try {
            ClientEndPoint clientEndPoint = getEndPoint(memberId);
            if (clientEndPoint != null) {
                clientEndPoint.close();
            }
        }
        finally {
            sessionEndpoints.remove(memberId);



    }
}

}
