package com.example.application.Presenter.StoreManagementPresenters.InventoryActionPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.Util.ProductDTO;
import com.example.application.View.StoreManagementViews.InventoryActionsViews.AddProductToStoreView;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

public class AddProductToStorePresenter {
    private AddProductToStoreView view;
    private String userID;
    private String storeID;

    public AddProductToStorePresenter(AddProductToStoreView view, String userID, String storeID){
        this.view = view;
        this.userID = userID;
        this.storeID = storeID;
    }

    public String getUserName(){
        return APIcalls.getMemberName(VaadinSession.getCurrent().getAttribute("memberID").toString());
    }

    public List<String> getCategories(){
        return APIcalls.getCategories();
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){
            view.logout();
        }
    }

    public void onAddButtonClicked(String productName, int price, int quantity, String description, String category) {
        String result = APIcalls.addProductToStore(userID, storeID, new ProductDTO(productName, price, quantity, description, category));
        if (result.contains("success")) {
            view.addSuccess("Product was added");
        } else {
            view.addFailure(result);
        }
    }
}
