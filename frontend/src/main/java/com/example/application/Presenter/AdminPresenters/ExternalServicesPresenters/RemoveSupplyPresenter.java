package com.example.application.Presenter.AdminPresenters.ExternalServicesPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.AdminViews.ExternalServicesViews.RemoveSupplyView;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

public class RemoveSupplyPresenter {
    private RemoveSupplyView view;
    private String userID;
    private String storeID;

    public RemoveSupplyPresenter(RemoveSupplyView view, String userID){
        this.view = view;
        this.userID = userID;
        this.storeID = storeID;
    }

    public String getUserName(){
        return APIcalls.getMemberName(VaadinSession.getCurrent().getAttribute("memberID").toString());
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){
            view.logout();
        }
    }

    public List<String> getSupplyServices(){
        return APIcalls.getExternalSupplyServices();
    }

    public void onRemoveButtonClicked(String url) {
        String result = APIcalls.removeExternalSupplyService(url, userID);
        if (result.contains("success")) {
            view.removeSuccess("Supply service was removed");
        } else {
            view.removeFailure(result);
        }
    }
}
