package com.example.application.Util;

public class PaymentServiceDTO {
    private String licensedDealerNumber;
    private String paymentServiceName;
    private String url;

    public PaymentServiceDTO ( String licensedDealerNumber, String paymentServiceName,String url){
        this.licensedDealerNumber = licensedDealerNumber;
        this.paymentServiceName = paymentServiceName;
        this.url = url;

    }

    public String getLicensedDealerNumber() {
        return licensedDealerNumber;
    }

    public String getPaymentServiceName() {
        return paymentServiceName;
    }


    public String getUrl() {
        return url;
    }

    public PaymentServiceDTO(){

    }
}
