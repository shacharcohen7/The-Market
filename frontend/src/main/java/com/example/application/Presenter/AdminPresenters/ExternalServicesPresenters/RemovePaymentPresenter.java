package com.example.application.Presenter.AdminPresenters.ExternalServicesPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.View.AdminViews.ExternalServicesViews.RemovePaymentView;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

public class RemovePaymentPresenter {
    private RemovePaymentView view;
    private String userID;
    private String storeID;

    public RemovePaymentPresenter(RemovePaymentView view, String userID){
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

    public List<String> getPaymentServices(){
        return APIcalls.getExternalPaymentServices();
    }

    public void onRemoveButtonClicked(String url) {
        String result = APIcalls.removeExternalPaymentService(url, userID);
        if (result.contains("success")) {
            view.removeSuccess("Payment service was removed");
        } else {
            view.removeFailure(result);
        }
    }
}
