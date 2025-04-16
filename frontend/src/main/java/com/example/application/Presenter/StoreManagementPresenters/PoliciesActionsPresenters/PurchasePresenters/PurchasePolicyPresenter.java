package com.example.application.Presenter.StoreManagementPresenters.PoliciesActionsPresenters.PurchasePresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.StoreManagementViews.PoliciesActionsViews.PurchaseViews.PurchasePolicyView;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

public class PurchasePolicyPresenter {
    private PurchasePolicyView view;
    private String userID;
    private String storeID;

    public PurchasePolicyPresenter(PurchasePolicyView view, String userID, String storeID){
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

    public List<String> getStoreCurrentPurchaseRules(){
        return APIcalls.getStoreCurrentPurchaseRules(userID, storeID);
    }
}
