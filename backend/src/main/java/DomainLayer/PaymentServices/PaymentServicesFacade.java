package DomainLayer.PaymentServices;


import DomainLayer.Repositories.AcquisitionMemoryRepository;
import DomainLayer.Repositories.AcquisitionRepository;
import DomainLayer.Repositories.ExternalPaymentMemoryRepository;
import DomainLayer.Repositories.ExternalPaymentRepository;
import DomainLayer.SupplyServices.ExternalSupplyService;
import Util.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class PaymentServicesFacade {
    private static PaymentServicesFacade paymentServicesFacadeInstance;
    private ExternalPaymentRepository externalPaymentRepository;
    private AcquisitionRepository acquisitionRepository;


    //db constructor
    @Autowired
    public PaymentServicesFacade(ExternalPaymentRepository externalPaymentRepository, AcquisitionRepository acquisitionRepository) throws Exception {
        this.externalPaymentRepository = externalPaymentRepository;
        this.acquisitionRepository = acquisitionRepository;
    }

    public void reset(){
        externalPaymentRepository.deleteAll();
        acquisitionRepository.deleteAll();
    }

    //memory constructor
    public PaymentServicesFacade(){
        this.externalPaymentRepository = new ExternalPaymentMemoryRepository();
        this.acquisitionRepository = new AcquisitionMemoryRepository();
    }

    //private int acquisitionIdCounter = 1;
    //private int receiptIdCounter = 1;

    public synchronized static PaymentServicesFacade getInstance() {
        if (paymentServicesFacadeInstance == null) {
            paymentServicesFacadeInstance = new PaymentServicesFacade();
        }
        return paymentServicesFacadeInstance;
    }

    public PaymentServicesFacade newForTest(){
        paymentServicesFacadeInstance= new PaymentServicesFacade();
        return paymentServicesFacadeInstance;
    }

    public void removeExternalService(String url) throws Exception {
        if (getAllPaymentServices().size() <= 1) {
            throw new Exception(ExceptionsEnum.OnlyPaymentService.toString());
        }
        if (externalPaymentRepository.findById(url).orElse(null)==null){
            throw new Exception("this url doesnt exist in the system");
        }
        externalPaymentRepository.deleteById(url);
    }

    public boolean checkHandShake(ExternalPaymentService externalPaymentService) throws Exception {
        return externalPaymentService.checkHandShake();
    }

//    public boolean addExternalService(String licensedDealerNumber, String paymentServiceName, String url){
//        synchronized (paymentServiceLock) {
//            int size_before = allPaymentServices.size();
//            ExternalPaymentService externalPaymentService = new ExternalPaymentService(licensedDealerNumber, paymentServiceName, url);
//            allPaymentServices.put(licensedDealerNumber, externalPaymentService);
//            return allPaymentServices.size() == size_before + 1;
//        }
//    }

    public boolean addExternalService(String paymentURL) throws Exception {
        List<ExternalPaymentService> externalPaymentServices = externalPaymentRepository.findAll();
        int size_before = externalPaymentServices.size();

        ExternalPaymentService externalPaymentService = new ExternalPaymentService(paymentURL);
        if (!checkHandShake(externalPaymentService)){
            return false;
        }
        externalPaymentRepository.save(externalPaymentService);
        if (externalPaymentRepository.findAll().size() == size_before ){
            throw new Exception("this url already exist");
        }
        return true;

    }


    public List<AcquisitionDTO> getAllAcquisitions(){
        List<Acquisition> acquisitions =acquisitionRepository.findAll();
        List<AcquisitionDTO> acquisitionDTOList = new ArrayList<>();
        for (Acquisition acquisition:acquisitions){
            acquisitionDTOList.add(new AcquisitionDTO(acquisition.getAcquisitionId(), acquisition.getMemberId(),acquisition.getTotalPrice(),acquisition.getDate()));
        }
        return acquisitionDTOList;
    }

//    public boolean addExternalService(PaymentServiceDTO paymentServiceDTO, HttpClient httpClient){
//        synchronized (paymentServiceLock) {
//            int size_before = allPaymentServices.size();
//            ExternalPaymentService externalPaymentService = new ExternalPaymentService(paymentServiceDTO, httpClient);
//            allPaymentServices.put(paymentServiceDTO.getLicensedDealerNumber(), externalPaymentService);
//            return allPaymentServices.size() == size_before + 1;
//        }
//    }

    public void clearPaymentServices() {
        externalPaymentRepository.deleteAll();
    }


    @Transactional
    public String pay(int price, PaymentDTO payment, String userId,String memberId, Map<String, Map<String, List<Integer>>> productList) throws Exception{
        String acquisitionId  = getNewAcquisitionId();
        ExternalPaymentService externalPaymentService = getAvailablePaymentService();
        if (externalPaymentService== null){
            throw new IllegalArgumentException("no available payment service right now , please try again later");
        }
        String url = externalPaymentService.getUrl();
        int transactionId  = externalPaymentService.payWithCard(price, payment );
        if (!isValidTransactionIdID(transactionId)){
            throw new IllegalArgumentException(ExceptionsEnum.ExternalPaymentFailed.toString());
        }
        Acquisition acquisition = new Acquisition(transactionId,acquisitionId, userId, memberId, price,url, payment, productList);
        acquisitionRepository.save(acquisition);
        //externalPaymentService.addAcquisition(acquisitionId, acquisition);
        return acquisition.getAcquisitionId();

    }

    public Acquisition getAcquisitionById(String acquisitionId){
        return acquisitionRepository.findById(acquisitionId).orElse(null);
    }


    public boolean cancelPayment(String acquisitionId) throws Exception {
        Acquisition acquisition = acquisitionRepository.findById(acquisitionId).orElse(null);
        if (acquisition!= null){
            throw new Exception("error cannot cancel the payment");
        }
        String url = acquisition.getUrl();
        if (url==null){
            throw new Exception("error while trying to find service to cancel payment");
        }
        ExternalPaymentService externalPaymentService = getPaymentServiceByURL(url);
        if (null== externalPaymentService){
            throw new Exception("error while trying to cancel payment");
        }
        int transactionId = externalPaymentService.cancelPayment(acquisition.getTransactionId());
        return isValidTransactionIdID(transactionId);
    }

    public List<String> getAllPaymentServicesUrl(){
        List<ExternalPaymentService> externalPaymentServicesList = externalPaymentRepository.findAll();
        List<String> urls = new ArrayList<>();
        for (ExternalPaymentService externalPaymentService: externalPaymentServicesList){
            urls.add(externalPaymentService.getUrl());
        }
        return urls;
    }


//    public int cancelPayment(int transactionID) throws Exception {
//        ExternalPaymentService externalPaymentService = getPaymentServiceByURL("https://damp-lynna-wsep-1984852e.koyeb.app/");
//         return externalPaymentService.cancelPayment(transactionID);
//    }

    public Map<String, String> getAcquisitionReceipts(String acquisitionId) {
        Optional<Acquisition> acquisition = acquisitionRepository.findById(acquisitionId);
        Acquisition acquisition1 = acquisition.orElse(null);
        if (acquisition1 != null) {
            return acquisition1.getReceiptIdAndStoreIdMap();
        }
        throw new IllegalArgumentException(ExceptionsEnum.AcquisitionNotExist.toString());
    }

    public String getNewAcquisitionId(){
        UUID uuid = UUID.randomUUID();
        String id = "acquisition-"+uuid.toString() ;
        return id;
    }


    public Map<String, ExternalPaymentService> getAllPaymentServices(){
        List<ExternalPaymentService> externalPaymentServices = externalPaymentRepository.findAll();
        Map<String,ExternalPaymentService> externalPaymentServiceMap = new HashMap<>();
        for(ExternalPaymentService externalPaymentService: externalPaymentServices){
            externalPaymentServiceMap.put(externalPaymentService.getUrl(), externalPaymentService);
        }
        return externalPaymentServiceMap;


    }


//    public PaymentServiceDTO getPaymentServiceDTOById(String paymentServiceId){
//        ExternalPaymentService externalPaymentService = getPaymentServiceById(paymentServiceId);
//        return new PaymentServiceDTO(externalPaymentService.getLicensedDealerNumber(), externalPaymentService.getPaymentServiceName(), externalPaymentService.getUrl());
//    }

//    public ExternalPaymentService getPaymentServiceById(String paymentServiceId){
//        if(allPaymentServices.containsKey(paymentServiceId)){
//            return allPaymentServices.get(paymentServiceId);
//        }
//        else {
//            return null;
//        }
//    }

    public ExternalPaymentService getPaymentServiceByURL(String paymentURL){
        List<ExternalPaymentService> externalPaymentServicesList = getAllPaymentServices().values().stream().toList();
        //Optional<ExternalPaymentService> externalPaymentService = externalPaymentRepository.findById(paymentURL);
        //ExternalPaymentService externalPaymentService1 = externalPaymentService.orElse(null);
        for (ExternalPaymentService externalPaymentService:externalPaymentServicesList){
            if (externalPaymentService.getUrl().equals(paymentURL)){
                return externalPaymentService;
            }
        }
        return null;
    }
//
//    public ExternalPaymentService getPaymentServiceByName(String name){
//        //Optional<ExternalPaymentService> externalPaymentService = externalPaymentRepository.findById(name);
//        //ExternalPaymentService externalPaymentService1 = externalPaymentService.orElse(null);
//        ExternalPaymentService externalPaymentService =externalPaymentRepository.findById(name).orElse(null);
//        if (externalPaymentService == null){
//            throw new IllegalArgumentException("no payment service with this name");
//        }
//        return externalPaymentService;

    //}


    public ExternalPaymentService getAvailablePaymentService() throws Exception {
        for (ExternalPaymentService externalPaymentService : externalPaymentRepository.findAll()){
            ExternalPaymentService externalPaymentService1 = new ExternalPaymentService(externalPaymentService.getUrl());
            if (externalPaymentService1.checkHandShake()){
                return  externalPaymentService1;
            }
        }
        return null;
    }

    public List<ReceiptDTO> getStoreAcquisitions(String storeId){
        List<ReceiptDTO> storeReceipts = new ArrayList<>();
        List<Acquisition> acquisitionList = acquisitionRepository.findAll();
        for (Acquisition acquisition : acquisitionList){
            for (Receipt receipt: acquisition.getReceiptMap().values()){
                if (receipt.getStoreId().equals(storeId)){
                    storeReceipts.add(new ReceiptDTO(receipt.getReceiptId(), receipt.getStoreId(),receipt.getMemberId(), receipt.getProductListToMap()));
                }
            }
        }
        return storeReceipts;
    }


    //return storeId and number of receipts
    @Transactional
    public Map<String, Integer> getStorePurchaseInfo() {
        Map<String, Integer> storePurchaseStats = new HashMap<>();
        List<Acquisition> acquisitions = acquisitionRepository.findAll();
        Map<String, List<String>> receiptIds = new HashMap<>(); //<storeId, List<receiptId>>
        for (Acquisition acquisition : acquisitions) {
            List<ProductDetailReceipt> productDetailReceipts = acquisition.getProductDetailReceipts();
            for (ProductDetailReceipt productDetailReceipt : productDetailReceipts) {
                String storeId = productDetailReceipt.getId().getStoreId();
                String receiptId = productDetailReceipt.getId().getReceiptId();
                List<String> receiptIdList = receiptIds.get(storeId);
                if (receiptIdList == null) {
                    receiptIdList = new ArrayList<>();
                }
                receiptIdList.add(receiptId);
                receiptIds.put(storeId, receiptIdList);
            }
        }
        for (String storeId : receiptIds.keySet()) {
            storePurchaseStats.put(storeId, receiptIds.get(storeId).size());
        }
        return storePurchaseStats;
    }


    public Map<String, Integer> getStoreReceiptsAndTotalAmount(String storeId)
    {
        Map<String, Integer> receiptAndTotalPrice = new HashMap<>();
        //List<Acquisition> acquisitions = acquisitionRepository.findAll();
        List<String> receipts = acquisitionRepository.getAllReceiptsByStoreId(storeId);
        for (String receipt : receipts){
            int receiptPrice = acquisitionRepository.findTotalPriceByReceipt(receipt);
            receiptAndTotalPrice.put(receipt, receiptPrice);
        }
//            for (Acquisition acquisition : acquisitions) {
//                acquisitionRepository.findByStoreId(storeId);
//                if (acquisition.getProductDetailReceipts().containsKey(storeId)) {
//                    receiptAndTotalPrice.put(acquisition.getReceiptIdByStoreId(storeId), acquisitionRepository.findTotalPriceByStoreAndReceiptAndAcquisition(storeId,acquisition.getReceiptIdByStoreId(storeId),acquisition.getAcquisitionId()));
//                }
//            }
        return receiptAndTotalPrice;
    }

    public Map<String, Acquisition> getIdAndAcquisition() {
        List<Acquisition> acquisitions = acquisitionRepository.findAll();
        Map<String, Acquisition> acquisitionMap = new HashMap<>();
        for (Acquisition acquisition: acquisitions){
            acquisitionMap.put(acquisition.getAcquisitionId(),acquisition);
        }
        return acquisitionMap;
    }

    public List<AcquisitionDTO> getAcquisitionsDTO(List<Acquisition> acquisitions) {
        List<AcquisitionDTO> acquisitionsDTO = new LinkedList<>();
        for (Acquisition acq : acquisitions) {
//            Optional<Acquisition> acq = acquisitionRepository.findById(acqId);
//            Acquisition acq1 = acq.orElse(null);
//            if (acq1 != null) {
                acquisitionsDTO.add(new AcquisitionDTO(acq.getAcquisitionId(), acq.getMemberId(), acq.getTotalPrice(), acq.getDate()));
//            }
        }

        return acquisitionsDTO;
    }

    public Map<String, ReceiptDTO> getReceiptsDTOByAcquisition(String acquisitionId) {

        Map<String, ReceiptDTO> receiptsDTO = new HashMap<>();
        Optional<Acquisition> acq = acquisitionRepository.findById(acquisitionId);
        Acquisition acq1 = acq.orElse(null);
        if (acq1 != null) {
            Map<String, String> storeReceipts = acq1.getStoreIdAndReceiptID();
            for (String storeId : storeReceipts.keySet()) {
                String receiptId = storeReceipts.get(storeId);
                receiptsDTO.put(receiptId,
                        new ReceiptDTO(storeReceipts.get(storeId),storeId, acq1.getMemberId(), convertToProductList(acquisitionRepository.findProductDetailReceiptsByReceiptAndAcquisition(receiptId,acquisitionId))));
            }
        }
        return receiptsDTO;
    }

    public boolean isValidTransactionIdID(int transactionId){
        int ID = transactionId;
        if(ID>=10000 && ID<=100000){
            return true;
        }
        return false;
    }

    //convert product list from List<ProductDetailReceipt> to Map<String, List<Integer>>
    public Map<String, List<Integer>> convertToProductList(List<ProductDetailReceipt> productDetailReceipts){
        Map<String, List<Integer>> productList = new HashMap<>();
        for (ProductDetailReceipt productDetailReceipt : productDetailReceipts) {
            List<Integer> priceAndQuantity = new ArrayList<>();
            priceAndQuantity.add(productDetailReceipt.getPrice());
            priceAndQuantity.add(productDetailReceipt.getAmount());
            productList.put(productDetailReceipt.getId().getProductName(), priceAndQuantity);
        }
        return productList;
    }

    public List<Acquisition> getMemberAcquisitionsHistory(String memberId) {
        return acquisitionRepository.findByMemberId(memberId);
    }


    public  List<Receipt> getUserReceiptsByAcquisition(String acquisitionId, String memberId) {
        List<Receipt> receipts = new LinkedList<>();
        List<String> receiptIds = acquisitionRepository.getReceiptIdsByAcquisitionId(acquisitionId);
        for (String receiptId : receiptIds) {
            List<ProductDetailReceipt> productDetailReceipts = acquisitionRepository.findProductDetailReceiptsByReceiptAndAcquisition(receiptId,acquisitionId);
            String storeId = productDetailReceipts.get(0).getId().getStoreId();
            receipts.add(new Receipt(receiptId,storeId,memberId,productDetailReceipts));
        }
        return receipts;
    }

    public ReceiptDTO getReceiptDTOFromReceipt(Receipt receipt) {
        return new ReceiptDTO(receipt.getReceiptId(), receipt.getStoreId(), receipt.getMemberId(), convertToProductList(receipt.getProductList()));
    }

    public void addPaymentForTests (String url) throws Exception {
        externalPaymentRepository.save(new ExternalPaymentService(url));
    }


}
