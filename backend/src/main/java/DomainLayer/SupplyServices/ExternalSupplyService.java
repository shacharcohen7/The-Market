package DomainLayer.SupplyServices;

import DomainLayer.HttpRequestController;
import Util.ExceptionsEnum;
import Util.ShippingDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "external_supply_service")
public class ExternalSupplyService {
    //private String licensedDealerNumber;
//    private String supplyServiceName;
//    private Set<String> countries = new HashSet<>();
//    private Set<String> cities = new HashSet<>();


    @Id
    @Column(name = "supply_url", nullable = false)
    private String supplyURL;
    @Transient
    private HttpRequestController httpReqCtrl;

    //@Transient
    //private Map<String, ShippingDTO> shippingAndDetails = new HashMap<>();
//    private final Object countriesLock;
//    private final Object citiesLock;
    @Transient
    private final Object shippingLock;

   // String ShiftID;

    public ExternalSupplyService() {
        // JPA requires a no-argument constructor
        this.shippingLock = new Object();
    }

    public ExternalSupplyService(String supplyURL){

        this.supplyURL=supplyURL;
        shippingLock =new Object();

        try
            {
                httpReqCtrl = new HttpRequestController(supplyURL);
            }
            catch (Exception e)
            {
                httpReqCtrl = null;
            }

        }

//    public ExternalSupplyService(String supplyURL){
//
//        this.licensedDealerNumber=supplyServiceDTO.getLicensedDealerNumber();
//        this.supplyServiceName = supplyServiceDTO.getSupplyServiceName();
//        this.countries  = supplyServiceDTO.getCountries();
//        this.cities = supplyServiceDTO.getCities();
//        countriesLock =new Object();
//        citiesLock = new Object();
//        shiftLock = new Object();
//    }

//    public String getLicensedDealerNumber(){
//        return this.licensedDealerNumber;
//    }

    public String getSupplyURL(){
        return this.supplyURL;
    }

    public boolean checkAreaAvailability(String country, String city){

//        synchronized (countriesLock) {
//            if (!countries.contains(country)) {
//                return false;
//            }
//        }
//        synchronized (citiesLock) {
//            if (!cities.contains((city))) {
//                return false;
//            }
//        }
        return true;
    }

    public void addCountries(HashSet<String> countriesToAdd){
//        synchronized (countriesLock) {
//            countries.addAll(countriesToAdd);
//        }

    }

    public void addCities(HashSet<String> citiesToAdd){
//        synchronized (citiesLock) {
//            cities.addAll(citiesToAdd);
//        }

    }

//    public Set<String> getCountries(){
////        synchronized (countriesLock) {
////            return this.countries;
////        }
//    }
//    public Set<String> getCities(){
//        synchronized (citiesLock) {
//            return this.cities;
//        }
//    }




//    public void createShiftingDetails(String userName, String country,String city,String address, String acquisitionId){
//        synchronized (shippingLock) {
//            int size = shiftIdAndDetails.size();
//            //getShiftIdAndDetails()
//            String shippingId=  getCurrentShippingID();
//            ShiftingDetails shiftingDetails = new ShiftingDetails(shippingId, userName, country, city, address ,acquisitionId);
//            shiftIdAndDetails.put(shippingId, shiftingDetails);
//        }
//    }
//    public Map<String, ShippingDTO> getShippingAndDetails(){
//        synchronized (shippingLock) {
//            return this.shippingAndDetails;
//        }
//    }

    public boolean checkHandShake() throws Exception {
        this.httpReqCtrl = new HttpRequestController(supplyURL);
        return this.httpReqCtrl.checkHandShake();
    }

    public int createSupply(String userName, String country,String city,String address , String acquisitionId)  {
        try {
            if (this.httpReqCtrl == null) {
                return -1;
            }
            this.httpReqCtrl = new HttpRequestController(supplyURL);
            if (!this.httpReqCtrl.checkHandShake()) {
                return -1;
            }
            Map<String, String> params = new HashMap<>();
            params.put("action_type", "supply");
            params.put("name", userName);
            params.put("address", address);
            params.put("city", city);
            params.put("country", country);
            params.put("zip", "777432");
            this.httpReqCtrl = new HttpRequestController(supplyURL);
            String response = this.httpReqCtrl.sendRequest(params);
            if (response == null) {
                System.out.println("Supply request failed or returned null response.");
                return -1;
            }
            try {
                int transactionId = Integer.parseInt(response);
                if (isValidTransactionID(transactionId)) {
                    return transactionId;
                }
                return  -1;

            } catch (NumberFormatException e) {
                System.out.println("Response format error: " + e.getMessage());
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Exception during Supply: " + e.getMessage());
            return -1;
        }
    }

        public int cancelSupply(int transactionID, String shippingId)  throws Exception{
            //int transactionID = getTransactionId(shippingId);
            if(httpReqCtrl == null) //If constructor failed
            {
                throw new Exception("No connection established");
            }

            if(transactionID == 0 || transactionID == -1)
            {
                throw new Exception(ExceptionsEnum.noPayment.toString());
            }

            this.httpReqCtrl = new HttpRequestController(supplyURL);
            //this.shippingAndDetails.remove(transactionID);
            if(!this.httpReqCtrl.checkHandShake())
            {
                throw new Exception(ExceptionsEnum.checkHandShake.toString());
            }
            Map<String,String> postContent = new HashMap<>();
            postContent.put("action_type", "cancel_supply");
            postContent.put("transaction_id", String.valueOf(transactionID));
            this.httpReqCtrl = new HttpRequestController(supplyURL);
            String response = this.httpReqCtrl.sendRequest(postContent);
            if (response == null)
            {
                throw new Exception("Failed to send request to external supply system");
            }
            int cancelRes = Integer.parseInt(response);
            if(cancelRes != 1)
            {
                throw new Exception(ExceptionsEnum.cancelFailed.toString());
            }
//            synchronized (shippingAndDetails) {
//                this.shippingAndDetails.remove(shippingId);
//            }
            return cancelRes;
        }

        public boolean isValidTransactionID(int transactionID){
            return transactionID >= 10000 && transactionID <= 100000;
        }

//        public boolean hasShipment(String shippingID){
//            return shippingAndDetails.containsKey(shippingID);
//        }
//
//        public String getShippingId(String acquisitionId){
//            for (ShippingDTO shippingDTO : shippingAndDetails.values()){
//                if (shippingDTO.getAcquisitionId().equals(acquisitionId)){
//                    return shippingDTO.getShipping_id();
//                }
//            }
//            return null;
//        }

//        public int getTransactionId(String shippingId){
//            synchronized (shippingLock){
//                ShippingDTO shippingDTO=  shippingAndDetails.get(shippingId);
//                if (shippingDTO!=null){
//                    return shippingDTO.getTransactionId();
//                }
//
//            }
//            return -1;
//        }






}
