package PresentationLayer.Vaadin;

import DomainLayer.Notifications.LateNotificationFacade;
//import DomainLayer.Notifications.Publisher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MyWebSocketHandler extends TextWebSocketHandler {
 /// todo add map
    LateNotificationFacade lateNotificationFacade = new LateNotificationFacade();
    private static final Map<String, WebSocketSession> sessionsByMember = new ConcurrentHashMap<>();
    private static MyWebSocketHandler instance;

    private MyWebSocketHandler(){
    }
    public static synchronized MyWebSocketHandler getInstance(){
        if(instance == null){
            instance = new MyWebSocketHandler();
        }
        return instance;
    }


    public List<String> getUserNotifications(String memberId){
        return lateNotificationFacade.getUserNotifications(memberId);
    }



    public void sendMassageToEveryOne(String message) throws Exception {
        // Handle incoming messages

        for (WebSocketSession session : sessionsByMember.values()){
            session.sendMessage(new TextMessage(message));
        }
       // session.sendMessage(new TextMessage(message));
    }


    private String formatTimestamp(LocalDateTime timestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return timestamp.format(formatter);
    }





    public synchronized void handleStringMessage(String memberID, String message) {
        // Handle incoming messages
        LocalDateTime timestamp = LocalDateTime.now();
        String formattedMessage = message + " at " + formatTimestamp(timestamp);


        WebSocketSession session = sessionsByMember.get(memberID);
        String currMemberId= null;
        try {
            currMemberId = (String) session.getAttributes().get("memberID");
        }
        catch (Exception e){
            lateNotificationFacade.sendLateMessage(memberID, formattedMessage);
            return;
        }
        if (session==null ){
            lateNotificationFacade.sendLateMessage(memberID, formattedMessage);
            return;
        }
        else if (currMemberId==null || !currMemberId.equals(memberID)){
            sessionsByMember.remove(memberID);
            lateNotificationFacade.sendLateMessage(memberID, formattedMessage);
            return;
        }
        else {
            try {
                session.sendMessage(new TextMessage(message));
            }
            catch (Exception e){
            }
            finally {
                lateNotificationFacade.sendLateMessage(memberID, formattedMessage);
            }
        }
    }

    @Override
    public synchronized void  afterConnectionEstablished(WebSocketSession session){
        // add session to map

        String sessionId = (String) session.getAttributes().get("memberID");
        if (sessionId != null) {
            sessionsByMember.put(sessionId, session);
        }
//        for (WebSocketSession socketSession: sessionsByMember.values()){
//            if (session.)
//        }
//        Event event = new Event(new Object(), "connect message from server!!!",new HashSet<>(Arrays.asList(u)));
//        Publisher publisher = (Publisher) SpringContext.getBean("Publisher");
//        publisher.publish(event);


    }



    public synchronized void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userName = (String) session.getAttributes().get("memberID");
        if (userName != null) {
            sessionsByMember.remove(userName);
        }
    }

    public void setLateNotificationFacadeForTest(LateNotificationFacade lateNotificationFacade) {
        this.lateNotificationFacade = lateNotificationFacade;
    }
}