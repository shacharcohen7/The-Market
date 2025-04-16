package com.example.application.Presenter.AdminPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.Util.StoreDTO;
import com.example.application.View.AdminViews.AdminCloseStoreView;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AdminCloseStorePresenter {
    private AdminCloseStoreView view;
    private String userID;
    private Map<String, String> nameToId;

    public AdminCloseStorePresenter(AdminCloseStoreView view, String userID){
        this.view = view;
        this.userID = userID;
        nameToId = new HashMap<>();
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

    public List<String> getAllStoreNames(){
        List<String> storeNames = new LinkedList<String>();
        List<StoreDTO> stores = APIcalls.getAllStores();
        if(stores != null) {
            for (int i = 0; i < stores.size(); i++) {
                StoreDTO store = stores.get(i);
                storeNames.add(store.getStoreName());
                nameToId.put(store.getStoreName(), store.getStore_ID());
            }
            return storeNames;
        }
        return null;
    }

    public void onCloseButtonClicked(String storeName) {
        if (APIcalls.closeStore(userID, nameToId.get(storeName)).contains("success")) {
            view.closeSuccess("Store was closed");
        } else {
            view.closeFailure("Invalid input.");
        }
    }
}
