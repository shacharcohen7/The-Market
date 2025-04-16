package com.example.application.Presenter;

import com.example.application.Model.APIcalls;
import com.example.application.Util.ProductDTO;
import com.example.application.Util.StoreDTO;
import com.example.application.View.StoreView;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class StorePresenter {
    private StoreView view;
    private String userID;
    private String storeID;

    public StorePresenter(StoreView view, String userID, String storeID){
        this.view = view;
        this.userID = userID;
        this.storeID = storeID;
    }

    public String getUserName(){
        if(isMember()){
            return APIcalls.getMemberName(VaadinSession.getCurrent().getAttribute("memberID").toString());
        }
        return "Guest";
    }

    public List<String> getCategories(){
        return APIcalls.getCategories();
    }

    public boolean isStoreOwner(){
        return APIcalls.isStoreOwner(VaadinSession.getCurrent().getAttribute("memberID").toString(), storeID);
    }

    public boolean isStoreOpen(){
        return APIcalls.isStoreOpen(storeID);
    }

    public boolean isStoreManager(){
        return APIcalls.isStoreManager(VaadinSession.getCurrent().getAttribute("memberID").toString(), storeID);
    }

    public boolean hasInventoryPermissions(){
        return APIcalls.hasInventoryPermission(VaadinSession.getCurrent().getAttribute("memberID").toString(), storeID);
    }

    public boolean hasPurchasePermissions(){
        return APIcalls.hasPurchasePermission(VaadinSession.getCurrent().getAttribute("memberID").toString(), storeID);
    }

    public boolean isMember(){
        return APIcalls.isMember(userID);
    }

    public StoreDTO getStore(){
        return APIcalls.getStore(storeID);
    }

    public String getStoreName(){
        if(getStore() == null){
            return null;
        }
        return getStore().getStoreName();
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){
            view.logout();
        }
    }

    public List<ProductDTO> getAllProducts(){
        return APIcalls.getStoreProducts(storeID);
    }

    public void onSearchButtonClicked(String productName, String category,
                                      Set<String> keywords, int minPrice, int maxPrice) {
        view.showInStoreSearchResult(APIcalls.inStoreProductSearch(userID, productName, category, new ArrayList<>(keywords), storeID));

    }

    public void onAddToCartButtonClicked(String productName, int quantity){
        String result = APIcalls.addProductToBasket(productName, quantity, storeID, userID);
        if (result.contains("success")) {
            view.addProductCartResult(result);
        } else {
            view.addProductCartResult(result);
        }
    }

    public boolean onCloseButtonClicked(){
        return APIcalls.closeStore(userID, storeID).contains("success");
    }

    public boolean onReopenButtonClicked(){
        return APIcalls.reopenStore(userID, storeID).contains("success");
    }
}
