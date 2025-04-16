package com.example.application.Presenter;

import com.example.application.Model.APIcalls;
import com.example.application.Util.CartDTO;
import com.example.application.Util.ProductDTO;
import com.example.application.Util.UserDTO;
import com.example.application.View.ShoppingCartView;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ShoppingCartPresenter {
    private ShoppingCartView view;
    private String userID;

    public ShoppingCartPresenter(ShoppingCartView view, String userID){
        this.view = view;
        this.userID = userID;
    }

    public String getUserName(){
        if(isMember()){
            return APIcalls.getMemberName(VaadinSession.getCurrent().getAttribute("memberID").toString());
        }
        return "Guest";
    }

    public UserDTO getUser(){
        return APIcalls.getUser(userID);
    }


    public boolean isMember(){
        return APIcalls.isMember(userID);
    }

    public String getStoreName(String storeID){
        if(APIcalls.getStore(storeID).getStoreName() == null){
            return null;
        }
        return APIcalls.getStore(storeID).getStoreName();
    }

    public CartDTO getCart(){
        return APIcalls.getCart(userID);
    }

    public void removeProductCart(String productName, String storeID, String userID){
        String result = APIcalls.removeProductFromBasket(productName , storeID,userID);
        if (result != null && result.contains("success")) {
            view.removeProductCartResult(result);
        } else {
            view.removeProductCartResult(result);
        }
    }

    public void modifyProductCart(String productName, int quantity,String storeID, String userID){
        String result = APIcalls.modifyShoppingCart(productName ,quantity, storeID,userID);
        if (result != null && result.contains("success")) {
            view.modifyProductCartResult(result);
        } else {
            view.modifyProductCartResult(result);
        }
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){
            Object memberIdObj = VaadinSession.getCurrent().getAttribute("memberID");
            if (memberIdObj!= null) {
                WebSocketHandler.getInstance().closeConnection(memberIdObj.toString());
            }
            view.logout();
        }
    }

    public ProductDTO getProduct(String productName, String storeID){
        List<ProductDTO> storeProducts = APIcalls.getStoreProducts(storeID);
        if (storeProducts != null) {
            for(int i=0 ; i<storeProducts.size() ; i++){
                if(storeProducts.get(i).getName().equals(productName)){
                    return storeProducts.get(i);
                }
            }
        }
        return null;
    }

    public int getTotalPrice(){
        int price = APIcalls.getCart(userID).getCartPrice();
        return price;
    }

    public void onSubmitButtonClicked(int price, String creditCard, int cvv, int month, int year, int holderID) {
        //call payWithExternalPaymentService()
        boolean success = true;
        if (success) {
            view.paymentSuccess("Payment performed successfully");
        } else {
            view.paymentFailure("Invalid payment details.");
        }
    }

    public void validationCart(String userID, UserDTO userDTO){
        try {
            CartDTO cartDTO = APIcalls.getCartAfterValidation(userID,userDTO);
            view.succssesCartValidation(cartDTO);
        } catch (Exception e) {
            view.failCartValidation("Cart validation failed");
        }
    }
}
