package com.example.application.Presenter.StoreManagementPresenters.HRActionsPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.StoreManagementViews.HRActionsViews.AppointStoreManagerView;
import com.vaadin.flow.server.VaadinSession;

public class AppointStoreManagerPresenter {
    private AppointStoreManagerView view;
    private String userID;
    private String storeID;

    public AppointStoreManagerPresenter(AppointStoreManagerView view, String userID, String storeID){
        this.view = view;
        this.userID = userID;
        this.storeID = storeID;
    }

    public String getUserName(){
        return APIcalls.getMemberName(VaadinSession.getCurrent().getAttribute("memberID").toString());
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){
            view.logout();
        }
    }

    public void onAppointButtonClicked(String usernameToAppoint, boolean inventoryPermissions, boolean purchasePermissions) {
        if (APIcalls.appointStoreManager(userID, usernameToAppoint, storeID, inventoryPermissions, purchasePermissions).contains("success")) {
            view.appointmentSuccess("Appointment performed successfully");
        } else {
            view.appointmentFailure("Invalid input.");
        }
    }
}
