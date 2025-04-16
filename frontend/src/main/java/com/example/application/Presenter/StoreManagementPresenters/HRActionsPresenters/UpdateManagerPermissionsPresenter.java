package com.example.application.Presenter.StoreManagementPresenters.HRActionsPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.StoreManagementViews.HRActionsViews.UpdateManagerPermissionsView;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

public class UpdateManagerPermissionsPresenter {
    private UpdateManagerPermissionsView view;
    private String userID;
    private String storeID;

    public UpdateManagerPermissionsPresenter(UpdateManagerPermissionsView view, String userID, String storeID){
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

    public List<String> getStoreManagers(){
        return APIcalls.getStoreMangers(storeID);
    }

    public String getEmployeeUserName(String memberID){
        return APIcalls.getMemberName(memberID);
    }

    public boolean hasInventoryPermissions(String memberID){
        return APIcalls.hasInventoryPermission(memberID, storeID);
    }

    public boolean hasPurchasePermissions(String memberID){
        return APIcalls.hasPurchasePermission(memberID, storeID);
    }

    public void onUpdateButtonClicked(String usernameToUpdate, boolean inventoryPermissions, boolean purchasePermissions) {
        if (APIcalls.updateStoreManagerPermissions(userID, usernameToUpdate, storeID, inventoryPermissions, purchasePermissions).contains("success")) {
            view.updateSuccess("Permissions were updated successfully");
        } else {
            view.updateFailure("Appointment failed.");
        }
    }
}
