package com.example.application.Presenter.StoreManagementPresenters.PoliciesActionsPresenters.DiscountPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.StoreManagementViews.PoliciesActionsViews.DiscountViews.DiscountPolicyView;
import com.vaadin.flow.server.VaadinSession;

import java.util.LinkedList;
import java.util.List;

public class DiscountPolicyPresenter {
    private DiscountPolicyView view;
    private String userID;
    private String storeID;

    public DiscountPolicyPresenter(DiscountPolicyView view, String userID, String storeID){
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

    public List<String> getStoreCurrentDiscountRules(){
        return APIcalls.getStoreCurrentDiscountRules(userID, storeID);
    }
}
