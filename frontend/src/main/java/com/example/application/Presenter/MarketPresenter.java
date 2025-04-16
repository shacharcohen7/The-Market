package com.example.application.Presenter;

import com.example.application.Model.APIcalls;
import com.example.application.Util.APIResponse;
import com.example.application.Util.ProductDTO;
import com.example.application.Util.StoreDTO;
import com.example.application.View.MarketView;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class MarketPresenter {
    private MarketView view;
    private String userID;

    public MarketPresenter(MarketView view, String userID){
        this.view = view;
        this.userID = userID;
    }

    public String getUserName(){
        if(isMember()){
            return APIcalls.getMemberName(VaadinSession.getCurrent().getAttribute("memberID").toString());
        }
        return "Guest";
    }

    public boolean isStoreOpen(String storeID){
        return APIcalls.isStoreOpen(storeID);
    }

    public boolean isMember(){
        return APIcalls.isMember(userID);
    }

    public void onSearchButtonClicked(String productName, String category,
                                      Set<String> keywords, int minPrice, int maxPrice, int minStoreRating) {
        List <String> keywordsList = new ArrayList<>(keywords);
        view.showGeneralSearchResult(APIcalls.generalProductSearch(userID, productName, category, keywordsList));
    }

    public List<StoreDTO> getAllStores(){
        return APIcalls.getAllStores();
    }

    public List<String> getCategories(){
        return APIcalls.getCategories();
    }

    public String getStoreName(String storeID){
        if(APIcalls.getStore(storeID).getStoreName() == null){
            return null;
        }
        return APIcalls.getStore(storeID).getStoreName();
    }

    public boolean isStoreOwner(String storeID){
        if(!isMember()){
            return false;
        }
        return APIcalls.isStoreOwner(VaadinSession.getCurrent().getAttribute("memberID").toString(), storeID);
    }

    public boolean isAdmin(){
        if(!isMember()){
            return false;
        }
        return APIcalls.isAdmin(VaadinSession.getCurrent().getAttribute("memberID").toString());
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){
            view.logout();
        }
    }

    public void onAddToCartButtonClicked(ProductDTO product, int quantity, String storeID){
        if (APIcalls.addProductToBasket(product.getName(), quantity, storeID, userID).contains("success")) {
            view.addProductCartResult("Product was added to cart");
        } else {
            view.addProductCartResult("Failed adding product to cart");
        }
    }
}
