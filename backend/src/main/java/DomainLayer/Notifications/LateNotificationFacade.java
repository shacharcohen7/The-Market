package DomainLayer.Notifications;

import java.util.*;

public class LateNotificationFacade {

    private final int QUEUESIZE= 20;

    private Map<String, ArrayDeque<String>> lateNotifications ;
    //memberId--queue(last 20 massages)
    //private final Object lock = new Object();


    public LateNotificationFacade(){
        lateNotifications = new HashMap<>();
    }

    public synchronized void sendLateMessage(String memberIdToSend, String message){

        if (lateNotifications.get(memberIdToSend)==null){
            lateNotifications.put(memberIdToSend, new ArrayDeque<>(20));
        }
        ArrayDeque<String> arrayDeque = lateNotifications.get(memberIdToSend);
        if (arrayDeque.size()==QUEUESIZE){
            arrayDeque.removeLast();
        }
        arrayDeque.addFirst(message);
    }

    public synchronized List<String> getUserNotifications(String memberId){
        if (lateNotifications.get(memberId)==null){
            return new ArrayList<>();
        }
        List<String> notifications = lateNotifications.get(memberId).stream().toList();
        lateNotifications.put(memberId, new ArrayDeque<String>());
        return notifications;
    }

}
