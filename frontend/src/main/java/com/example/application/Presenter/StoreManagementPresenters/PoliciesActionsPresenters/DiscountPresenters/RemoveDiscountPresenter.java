package com.example.application.Presenter.StoreManagementPresenters.PoliciesActionsPresenters.DiscountPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.StoreManagementViews.PoliciesActionsViews.DiscountViews.RemoveDiscountView;
import com.example.application.View.StoreManagementViews.PoliciesActionsViews.PurchaseViews.RemovePurchaseRuleView;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoveDiscountPresenter {
    private RemoveDiscountView view;
    private Map<String, Integer> descriptionToNum;
    private String userID;
    private String storeID;

    public RemoveDiscountPresenter(RemoveDiscountView view, String userID, String storeID){
        this.view = view;
        this.userID = userID;
        this.storeID = storeID;
        descriptionToNum = new HashMap<>();
    }

    public List<String> getStoreCurrentDiscountRules(){
        List<String> discountPolicies = APIcalls.getStoreCurrentDiscountRules(userID, storeID);
        if (discountPolicies != null) {
            for(int i=0 ; i<discountPolicies.size() ; i++){
                descriptionToNum.put(discountPolicies.get(i), i);
            }
            return discountPolicies;
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

    public void onRemoveButtonClicked(String rule) {
        String result = APIcalls.removeDiscountRuleFromStore(descriptionToNum.get(rule), userID, storeID);
        if (result.contains("success")) {
            view.removeSuccess("Discount rule was removed");
        } else {
            view.removeFailure(result);
        }
    }
}
