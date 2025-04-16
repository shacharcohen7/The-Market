package com.example.application.Presenter.AdminPresenters.ExternalServicesPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.Util.SupplyServiceDTO;
import com.example.application.View.AdminViews.ExternalServicesViews.AddSupplyView;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashSet;

public class AddSupplyPresenter {
    private AddSupplyView view;
    private String userID;
    private String storeID;

    public AddSupplyPresenter(AddSupplyView view, String userID){
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

    public void onAddButtonClicked(String licensedDealerNumber, String supplyServiceName,
                                   String country, String city) {
        HashSet<String> countries = new HashSet<>();
        countries.add(country);
        HashSet<String> cities = new HashSet<>();
        countries.add(city);
        String result = APIcalls.addExternalSupplyService(userID,
                new SupplyServiceDTO(licensedDealerNumber, supplyServiceName, countries, cities));
        if (result.contains("success")) {
            view.addSuccess("Supply service was added");
        } else {
            view.addFailure(result);
        }
    }
}
