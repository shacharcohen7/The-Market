package com.example.application.Presenter;

import com.example.application.Model.APIcalls;
import com.example.application.Util.PaymentServiceDTO;
import com.example.application.Util.SupplyServiceDTO;
import com.example.application.Util.UserDTO;
import com.example.application.View.InitializeView;
import com.vaadin.flow.server.VaadinSession;

import java.util.HashSet;

public class InitializePresenter {
    InitializeView view;

    public InitializePresenter(InitializeView view){
        this.view = view;
        action();
    }

    public void action(){
        if(APIcalls.checkInitializedMarket()){
            view.startSession();
        }
        else{
            if (APIcalls.init()!= null) {
                view.startSession();
            }
            else {
                System.out.println("init error system cannot run");
                view.cannotRun();
            }
        }
    }

//    public void onInitializeButtonClicked(UserDTO userDTO, String password, PaymentServiceDTO paymentServiceDTO, String supplyDealerNumberField,
//                                          String supplyServiceName, String countriesSet, String citiesSet){
//        String data = APIcalls.init(userDTO, password, paymentServiceDTO, supplyDealerNumberField, supplyServiceName,
//                countriesSet, citiesSet);
//        if (data !=null && data.contains("user")){
//            VaadinSession.getCurrent().setAttribute("userID", data);;
//            view.initializeSuccess("Market was initialized successfully");
//        }
//        else{
//            view.initializeFailed("Initialized failed");
//        }
//    }
}
