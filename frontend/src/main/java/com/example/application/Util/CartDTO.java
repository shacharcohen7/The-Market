package com.example.application.Util;

import java.util.List;
import java.util.Map;

public class CartDTO {
    String userID;
    int cartPrice;
    Map<String, Map<String, List<Integer>>> storeToProducts;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(int cartPrice) {
        this.cartPrice = cartPrice;
    }

    public Map<String, Map<String, List<Integer>>> getStoreToProducts() {
        return storeToProducts;
    }

    public CartDTO (String userID, int cartPrice, Map<String, Map<String, List<Integer>>> products){
        this.userID = userID;
        this.cartPrice = cartPrice;
        this.storeToProducts = products;
    }

    public CartDTO(){
    }

}
