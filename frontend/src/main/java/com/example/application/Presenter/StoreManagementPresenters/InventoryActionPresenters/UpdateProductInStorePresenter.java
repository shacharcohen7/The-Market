package com.example.application.Presenter.StoreManagementPresenters.InventoryActionPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.Util.ProductDTO;
import com.example.application.View.StoreManagementViews.InventoryActionsViews.UpdateProductInStoreView;
import com.vaadin.flow.server.VaadinSession;

import java.util.LinkedList;
import java.util.List;

public class UpdateProductInStorePresenter {
    private UpdateProductInStoreView view;
    private String userID;
    private String storeID;

    public UpdateProductInStorePresenter(UpdateProductInStoreView view, String userID, String storeID){
        this.view = view;
        this.userID = userID;
        this.storeID = storeID;
    }

    public String getUserName(){
        return APIcalls.getMemberName(VaadinSession.getCurrent().getAttribute("memberID").toString());
    }

    public List<String> getAllProductNames(){
        List<ProductDTO> products = APIcalls.getStoreProducts(storeID);
        if(products != null){
            List<String> productNames = new LinkedList<String>();
            for(int i=0 ;i<products.size() ; i++){
                productNames.add(products.get(i).getName());
            }
            return productNames;
        }
        return null;
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){
            view.logout();
        }
    }

    public ProductDTO getProductByName(String productName){
        List<ProductDTO> products = APIcalls.getStoreProducts(storeID);
        if (products != null) {
            for(int i=0 ;i<products.size() ; i++){
                if(products.get(i).getName().equals(productName)) {
                    return products.get(i);
                }
            }
        }
        return null;
    }

    public List<String> getCategories(){
        return APIcalls.getCategories();
    }

    public void onUpdateButtonClicked(String productName, int price, int quantity, String description, String category) {
        if (APIcalls.updateProductInStore(userID, storeID, new ProductDTO(productName, price, quantity, description, category)).contains("success")) {
            view.updateSuccess("Product was updated");
        } else {
            view.updateFailure("Invalid input.");
        }
    }
}
