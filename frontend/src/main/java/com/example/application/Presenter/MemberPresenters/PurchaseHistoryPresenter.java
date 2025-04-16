package com.example.application.Presenter.MemberPresenters;

import com.example.application.Model.APIcalls;
import com.example.application.Util.AcquisitionDTO;
import com.example.application.Util.ReceiptDTO;
import com.example.application.View.MemberViews.PurchaseHistoryView;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;
import java.util.Map;

public class PurchaseHistoryPresenter {
    private PurchaseHistoryView view;
    private String userID;

    public PurchaseHistoryPresenter(PurchaseHistoryView view, String userID) {
        this.view = view;
        this.userID = userID;
    }

    public List<AcquisitionDTO> loadAcquisitionHistory() {
        return APIcalls.getUserAcquisitionsHistory(userID);
    }

    public void logOut(){
        if(APIcalls.logout(userID).contains("success")){
            view.logout();
        }
    }

    public void onAcquisitionSelected(String acquisitionId) {
        try {
            Map<String, ReceiptDTO> receipts = APIcalls.getUserReceiptsByAcquisition(userID, acquisitionId);
            view.showReceipts(receipts);
        } catch (Exception e) {
            view.showError("Failed to load receipt details.");
        }
    }

    public String cancelAcquisition(String userId ,String acquisitionId){
        String val = APIcalls.cancelAcquisition(userId , acquisitionId);
        if (val!=null){
            if (val.equals("-1")){

                return "failed to cancel";
            }
            else {
                loadAcquisitionHistory();
                return "cancel successfully acquisitionId- "+acquisitionId;
            }
        }
        return "failed to cancel";
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

