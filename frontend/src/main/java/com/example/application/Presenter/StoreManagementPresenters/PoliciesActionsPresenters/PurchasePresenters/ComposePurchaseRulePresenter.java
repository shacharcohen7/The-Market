package com.example.application.Presenter.StoreManagementPresenters.PoliciesActionsPresenters.PurchasePresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.StoreManagementViews.PoliciesActionsViews.PurchaseViews.ComposePurchaseRuleView;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComposePurchaseRulePresenter {
    private ComposePurchaseRuleView view;
    private Map<String, Integer> descriptionToNum;
    private String userID;
    private String storeID;

    public ComposePurchaseRulePresenter(ComposePurchaseRuleView view, String userID, String storeID){
        this.view = view;
        this.userID = userID;
        this.storeID = storeID;
        descriptionToNum = new HashMap<>();
    }

    public List<String> getStoreCurrentPurchaseRules(){
        List<String> purchasePolicies = APIcalls.getStoreCurrentPurchaseRules(userID, storeID);
        if (purchasePolicies != null) {
            for(int i=0 ; i<purchasePolicies.size() ; i++){
                descriptionToNum.put(purchasePolicies.get(i), i);
            }
            return purchasePolicies;
        }
        return null;
    }

    public String getUserName(){
        return APIcalls.getMemberName(VaadinSession.getCurrent().getAttribute("memberID").toString());
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){
            view.logout();
        }
    }

    public void onComposeButtonClicked(String rule1, String rule2, String operator) {
        String result = APIcalls.composeCurrentPurchaseRules
                (descriptionToNum.get(rule1), descriptionToNum.get(rule2), operator, userID, storeID);
        if (result.contains("success")) {
            view.composeSuccess("Purchase rule was composed");
        } else {
            view.composeFailure(result);
        }
    }
}
