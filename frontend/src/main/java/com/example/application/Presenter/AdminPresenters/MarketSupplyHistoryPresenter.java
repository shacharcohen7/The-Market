package com.example.application.Presenter.AdminPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.Util.ShippingDTO;
import com.example.application.View.AdminViews.MarketPurchaseHistoryView;
import com.example.application.View.AdminViews.MarketSupplyHistoryView;
import com.vaadin.flow.server.VaadinSession;

import java.util.LinkedList;
import java.util.List;

public class MarketSupplyHistoryPresenter {
    private MarketSupplyHistoryView view;
    private String userID;

    public MarketSupplyHistoryPresenter(MarketSupplyHistoryView view, String userID) {
        this.view = view;
        this.userID = userID;
    }

    public List<ShippingDTO> loadMarketSupplyHistory() {
        return APIcalls.getSupplySystemHistory(userID);
//        List<ShippingDTO> list = new LinkedList<>();
//        ShippingDTO dto = new ShippingDTO("a", 1, "a", "a", "a", "a", "a");
//        list.add(dto);
//        return list;
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){
            view.logout();
        }
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

}
