package com.example.application.Presenter.MemberPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.MemberViews.NotificationsView;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

public class NotificationsPresenter {
    private NotificationsView view;
    private String userID;

    public NotificationsPresenter(NotificationsView view, String userID) {
        this.view = view;
        this.userID = userID;
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){

            view.logout();
        }
    }

    public String getUserName(){
        if(isMember()){
            return APIcalls.getMemberName(VaadinSession.getCurrent().getAttribute("memberID").toString());
        }
        return "Guest";
    }

    public boolean isMember(){
        return APIcalls.isMember(userID);
    }

    public List<String> getNotifications(){
        return APIcalls.getNotifications(VaadinSession.getCurrent().getAttribute("memberID").toString());
    }
}
