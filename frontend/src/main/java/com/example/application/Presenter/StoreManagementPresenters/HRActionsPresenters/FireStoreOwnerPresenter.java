package com.example.application.Presenter.StoreManagementPresenters.HRActionsPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.StoreManagementViews.HRActionsViews.FireStoreOwnerView;
import com.vaadin.flow.server.VaadinSession;

import java.util.LinkedList;
import java.util.List;

public class FireStoreOwnerPresenter {
    private FireStoreOwnerView view;
    private String userID;
    private String storeID;

    public FireStoreOwnerPresenter(FireStoreOwnerView view, String userID, String storeID){
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

    public List<String> getStoreOwners(){
        List<String> memberIDs = APIcalls.getStoreOwners(storeID);
        if (memberIDs != null) {
            List<String> names = new LinkedList<>();
            for(int i=0 ; i<memberIDs.size() ; i++){
                names.add(APIcalls.getMemberName(memberIDs.get(i)));
            }
            return names;
        }
        return null;
    }

    public void onFireButtonClicked(String appointedUserName) {
        if (APIcalls.fireStoreOwner(userID, appointedUserName, storeID).contains("success")) {
            view.fireSuccess("Store owner was fired successfully");
        } else {
            view.fireFailure("Invalid input.");
        }
    }
}
