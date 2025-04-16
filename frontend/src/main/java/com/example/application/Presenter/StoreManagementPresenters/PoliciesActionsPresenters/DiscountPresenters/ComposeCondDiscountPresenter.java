package com.example.application.Presenter.StoreManagementPresenters.PoliciesActionsPresenters.DiscountPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.StoreManagementViews.PoliciesActionsViews.DiscountViews.ComposeCondDiscountView;
import com.example.application.View.StoreManagementViews.PoliciesActionsViews.DiscountViews.ComposeSimpleDiscountView;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComposeCondDiscountPresenter {
    private ComposeCondDiscountView view;
    private Map<String, Integer> descriptionToNum;
    private String userID;
    private String storeID;

    public ComposeCondDiscountPresenter(ComposeCondDiscountView view, String userID, String storeID){
        this.view = view;
        this.userID = userID;
        this.storeID = storeID;
        descriptionToNum = new HashMap<>();
    }

    public List<String> getStoreCurrentCondDiscountRules(){
        List<String> condDiscountRules = APIcalls.getStoreCurrentCondDiscountRules(userID, storeID);
        if (condDiscountRules != null) {
            for(int i=0 ; i<condDiscountRules.size() ; i++){
                descriptionToNum.put(condDiscountRules.get(i), i);
            }
            return condDiscountRules;
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

    public void onComposeButtonClicked(String rule1, String rule2, String logicalOperator, String numericalOperator) {
        String result = APIcalls.composeCurrentCondDiscountRules
                (descriptionToNum.get(rule1), descriptionToNum.get(rule2), logicalOperator, numericalOperator, userID, storeID);
        if (result.contains("success")) {
            view.composeSuccess("Discount rule was composed");
        } else {
            view.composeFailure(result);
        }
    }
}
