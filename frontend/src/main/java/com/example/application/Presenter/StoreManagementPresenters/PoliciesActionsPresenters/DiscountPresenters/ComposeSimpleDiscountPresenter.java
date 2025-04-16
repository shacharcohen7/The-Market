package com.example.application.Presenter.StoreManagementPresenters.PoliciesActionsPresenters.DiscountPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.StoreManagementViews.PoliciesActionsViews.DiscountViews.ComposeSimpleDiscountView;
import com.example.application.View.StoreManagementViews.PoliciesActionsViews.PurchaseViews.ComposePurchaseRuleView;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComposeSimpleDiscountPresenter {
    private ComposeSimpleDiscountView view;
    private Map<String, Integer> descriptionToNum;
    private String userID;
    private String storeID;

    public ComposeSimpleDiscountPresenter(ComposeSimpleDiscountView view, String userID, String storeID){
        this.view = view;
        this.userID = userID;
        this.storeID = storeID;
        descriptionToNum = new HashMap<>();
    }

    public List<String> getStoreCurrentSimpleDiscountRules(){
        List<String> simpleDiscountRules = APIcalls.getStoreCurrentSimpleDiscountRules(userID, storeID);
        if (simpleDiscountRules != null) {
            for(int i=0 ; i<simpleDiscountRules.size() ; i++){
                descriptionToNum.put(simpleDiscountRules.get(i), i);
            }
            return simpleDiscountRules;
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
        String result = APIcalls.composeCurrentSimpleDiscountRules
                (descriptionToNum.get(rule1), descriptionToNum.get(rule2), operator, userID, storeID);
        if (result.contains("success")) {
            view.composeSuccess("Discount rule was composed");
        } else {
            view.composeFailure(result);
        }
    }
}
