package com.example.application.Presenter.StoreManagementPresenters.PoliciesActionsPresenters.DiscountPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.Util.DiscountValueDTO;
import com.example.application.Util.ProductDTO;
import com.example.application.Util.TestRuleDTO;
import com.example.application.View.StoreManagementViews.PoliciesActionsViews.DiscountViews.AddCondDiscountView;
import com.vaadin.flow.server.VaadinSession;

import java.util.LinkedList;
import java.util.List;

public class AddCondDiscountPresenter {
    private AddCondDiscountView view;
    private String userID;
    private String storeID;

    public AddCondDiscountPresenter(AddCondDiscountView view, String userID, String storeID){
        this.view = view;
        this.userID = userID;
        this.storeID = storeID;
    }

    public List<String> getCategories(){
        return APIcalls.getCategories();
    }

    public List<String> getAllProductNames(){
        List<ProductDTO> products = APIcalls.getStoreProducts(storeID);
        if (products != null) {
            List<String> productNames = new LinkedList<String>();
            for(int i=0 ;i<products.size() ; i++){
                productNames.add(products.get(i).getName());
            }
            return productNames;
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

    public void onAddButtonClicked(List<TestRuleDTO> Rules, List<String> logicOperators, List<DiscountValueDTO> discs, List<String> numericOperators) {
        String result = APIcalls.addDiscountCondRuleToStore(Rules, logicOperators, discs, numericOperators, userID, storeID);
        if (result.contains("success")) {
            view.addSuccess("Discount rule was added");
        } else {
            view.addFailure(result);
        }
    }
}
