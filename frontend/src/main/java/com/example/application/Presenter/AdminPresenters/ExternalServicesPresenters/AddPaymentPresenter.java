package com.example.application.Presenter.AdminPresenters.ExternalServicesPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.Util.PaymentServiceDTO;
import com.example.application.View.AdminViews.ExternalServicesViews.AddPaymentView;
import com.vaadin.flow.server.VaadinSession;

public class AddPaymentPresenter {
    private AddPaymentView view;
    private String userID;
    private String storeID;

    public AddPaymentPresenter(AddPaymentView view, String userID){
        this.view = view;
        this.userID = userID;
    }

    public String getUserName(){
        return APIcalls.getMemberName(VaadinSession.getCurrent().getAttribute("memberID").toString());
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){
            view.logout();
        }
    }

    public void onAddButtonClicked(String paymentName, String licensedDealerNumber,String url) {
        String result = APIcalls.addExternalPaymentService(userID,
                new PaymentServiceDTO(licensedDealerNumber, paymentName, url));
        if (result.contains("success")) {
            view.addSuccess("Payment service was added");
        } else {
            view.addFailure(result);
        }
    }
}
