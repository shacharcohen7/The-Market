package DomainLayer.PaymentServices;


// this class is for external payment service itself

import DomainLayer.HttpRequestController;
import Util.ExceptionsEnum;
import Util.PaymentDTO;
import Util.PaymentServiceDTO;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "external_payment_service")
public  class ExternalPaymentService {

    @Id
    @Column(name = "url", nullable = false)
    private String url;

    //@Transient
    //private HttpRequestController httpReqCtrl;

    //@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    //@JoinColumn(name = "external_payment_service_url") // This will create a foreign key column in the Acquisition table
    //@MapKeyColumn(name = "acquisition_id") // Column in the Acquisition table for acquisition_id
    //private Map<String, Acquisition> idAndAcquisition = new HashMap<>();
    // private HttpClient httpClient=new SimpleHttpClient();
    @Transient
    private final Object acquisitionLock= new Object();

    public ExternalPaymentService() {
        // JPA requires a no-argument constructor
//        HttpRequestController httpReqCtrl;
//        try
//        {
//            httpReqCtrl = new HttpRequestController(url);
//        }
//        catch (Exception e)
//        {
//            httpReqCtrl = null;
//        }
    }

    public ExternalPaymentService(String url) throws Exception {
        this.url = url;
        try {
            HttpRequestController httpReqCtrl = new HttpRequestController(url);
        }
        catch (Exception e){
            throw new IllegalArgumentException("Payment Service cannot connect to the url");
        }
        //httpReqCtrl = null;

        }



    //    public ExternalPaymentService(PaymentServiceDTO paymentServiceDTO) {
//        this.licensedDealerNumber = paymentServiceDTO.getLicensedDealerNumber();
//        this.paymentServiceName = paymentServiceDTO.getPaymentServiceName();
//        this.url = paymentServiceDTO.getUrl();
//
//        //this.httpClient = httpClient;
//    }

//    public ExternalPaymentService(PaymentServiceDTO paymentServiceDTO, HttpClient httpClient) {
//        this.licensedDealerNumber = paymentServiceDTO.getLicensedDealerNumber();
//        this.paymentServiceName = paymentServiceDTO.getPaymentServiceName();
//        this.url = paymentServiceDTO.getUrl();
//
//        this.httpClient = httpClient;
//    }


//    public String getLicensedDealerNumber(){
//        return licensedDealerNumber;
//    }

//    public String getPaymentServiceName(){
//        return paymentServiceName;
//    }
    public String getUrl(){
        return url;
    }

    // Abstract method for paying with a card
    public int payWithCard(int price, PaymentDTO payment) throws Exception {
        try {
            //if (this.httpReqCtrl == null) {
            //    return -1;
            //}
            HttpRequestController httpReqCtrl = new HttpRequestController(url);
            if (!httpReqCtrl.checkHandShake()) {
                return -1;
            }
            Map<String,String> postContent = new HashMap<>();
            postContent.put("action_type", "pay");
            postContent.put("amount", String.valueOf(price));
            postContent.put("currency", payment.getCurrency());
            postContent.put("card_number", payment.getCreditCardNumber());
            postContent.put("month", String.valueOf(payment.getMonth()));
            postContent.put("year", String.valueOf(payment.getYear()));
            postContent.put("holder", payment.getHolderName());
            postContent.put("cvv", String.valueOf(payment.getCvv()));
            postContent.put("id", payment.getHolderId());
            httpReqCtrl = new HttpRequestController(url);
            String response  = httpReqCtrl.sendRequest(postContent);
            if (response == null) {
                System.out.println("Payment request failed or returned null response.");
                return -1;
            }
            try {
                int transactionId = Integer.parseInt(response);
                return transactionId;
            } catch (NumberFormatException e) {
                System.out.println("Response format error: " + e.getMessage());
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Exception during payment: " + e.getMessage());
            return -1;
        }

    }

//    public void addAcquisition(String acquisitionId, Acquisition acquisition){
//        synchronized (acquisitionLock) {
//            idAndAcquisition.put(acquisitionId, acquisition);
//        }
//    }



    public int cancelPayment(int transactionID)  throws Exception{

//        if(httpReqCtrl == null) //If constructor failed
//        {
//            throw new Exception("No connection established");
//        }

        if(transactionID == 0 || transactionID == -1)
        {
            throw new Exception(ExceptionsEnum.noPayment.toString());
        }

        HttpRequestController httpReqCtrl = new HttpRequestController(url);
        if(!httpReqCtrl.checkHandShake())
        {
            throw new Exception(ExceptionsEnum.checkHandShake.toString());
        }
        Map<String,String> postContent = new HashMap<>();
        postContent.put("action_type", "cancel_pay");
        postContent.put("transaction_id", String.valueOf(transactionID));
        httpReqCtrl = new HttpRequestController(url);
        String response = httpReqCtrl.sendRequest(postContent);
        if (response == null)
        {
            throw new Exception("Failed to send request to external payment system");
        }
        int cancelRes = Integer.parseInt(response);
        if(cancelRes != 1)
        {
            throw new Exception(ExceptionsEnum.cancelFailed.toString());
        }
        //removeAcquisition(String.valueOf(transactionID));
        return cancelRes;
    }

//    // Abstract method for checking service availability
//    public boolean checkServiceAvailability() {
//        return true;
//    }

//    public Map<String, Acquisition> getIdAndAcquisition() {
//        synchronized (acquisitionLock) {
//            return idAndAcquisition;
//        }
//    }

//    public void removeAcquisition(String acquisitionId){
//        synchronized (acquisitionLock){
//            idAndAcquisition.remove(acquisitionId);
//        }
//    }


    public boolean checkHandShake() throws Exception {

        HttpRequestController httpReqCtrl = new HttpRequestController(this.url);
        return httpReqCtrl.checkHandShake();
    }
}