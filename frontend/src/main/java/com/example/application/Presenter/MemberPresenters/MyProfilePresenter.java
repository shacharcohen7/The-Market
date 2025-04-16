package com.example.application.Presenter.MemberPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.Util.UserDTO;
import com.example.application.View.MemberViews.MyProfileView;
import com.vaadin.flow.server.VaadinSession;

import java.time.LocalDate;

public class MyProfilePresenter {
    private MyProfileView view;
    private String userID;

    public MyProfilePresenter(MyProfileView view, String userID){
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

    public UserDTO getUser(){
        return APIcalls.getUser(userID);
    }

//    public void onSaveButtonClicked(LocalDate birthdate, String country, String city,
//                                      String address, String name) {
//        //call updateUser()
//        boolean success = true;
//        if (success){
//            view.updateSuccess("User details were updated successfully");
//        } else {
//            view.updateFailure("Invalid input.");
//        }
//    }
}
