package com.example.application.Presenter.StoreManagementPresenters.HRActionsPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.StoreManagementViews.HRActionsViews.FireStoreManagerView;
import com.vaadin.flow.server.VaadinSession;

import java.util.LinkedList;
import java.util.List;

public class FireStoreManagerPresenter {
    private FireStoreManagerView view;
    private String userID;
    private String storeID;

    public FireStoreManagerPresenter(FireStoreManagerView view, String userID, String storeID){
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
        List<String> memberIDs = APIcalls.getStoreMangers(storeID);
        if (memberIDs != null) {
            List<String> names = new LinkedList<>();
            for(int i=0 ; i<memberIDs.size() ; i++){
                names.add(APIcalls.getMemberName(memberIDs.get(i)));
            }
            return names;
        }
        return null;
    }

    public void onFireButtonClicked(String appointedUsername) {
        if (APIcalls.fireStoreManager(userID, appointedUsername, storeID).contains("success")) {
            view.fireSuccess("Store manager was fired successfully");
        } else {
            view.fireFailure("Invalid input.");
        }
    }
}
