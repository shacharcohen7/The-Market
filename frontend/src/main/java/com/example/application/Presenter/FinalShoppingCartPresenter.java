package com.example.application.Presenter;

import com.example.application.Model.APIcalls;
import com.example.application.Util.CartDTO;
import com.example.application.Util.PaymentDTO;
import com.example.application.Util.ProductDTO;
import com.example.application.Util.UserDTO;
import com.example.application.View.FinalShoppingCartView;
import com.example.application.WebSocketUtil.WebSocketHandler;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

public class FinalShoppingCartPresenter {

    private FinalShoppingCartView view;
    private String userID;
    private CartDTO finalCart;

    public FinalShoppingCartPresenter(FinalShoppingCartView view, String userID, CartDTO finalCart){
        this.view = view;
        this.userID = userID;
        this.finalCart = finalCart;
    }

    public String getUserName(){
        if(isMember()){
            return APIcalls.getMemberName(VaadinSession.getCurrent().getAttribute("memberID").toString());
        }
        return "Guest";
    }

    public boolean isMember(){
        return APIcalls.isMember(userID);
    }

    public UserDTO getUser(){
        return APIcalls.getUser(userID);
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

    public CartDTO getFinalCart(){
        return finalCart;
    }

    public String getStoreName(String storeID){
        if(APIcalls.getStore(storeID).getStoreName() == null){
            return null;
        }
        return APIcalls.getStore(storeID).getStoreName();
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
        return finalCart.getCartPrice();
    }

//    public void onSubmitButtonClicked(String creditCard, int cvv, int month, int year, String holderID) {
//        UserDTO userDTO = APIcalls.getUser(userID);
//        PaymentDTO paymentDTO = new PaymentDTO(holderID,creditCard,cvv,month,year);
//        String result = APIcalls.purchase(userDTO,paymentDTO,finalCart);
//        view.paymentResult(result);
//    }
}
