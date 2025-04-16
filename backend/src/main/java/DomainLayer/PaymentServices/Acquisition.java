package DomainLayer.PaymentServices;

import Util.PaymentDTO;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "acquisition")
public class Acquisition {

    @Column(name = "transactionId")
    private int transactionId;

    @Column(name = "url")
    private String url;

    @Id
    @Column(name = "acquisition_id")
    private String acquisitionId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "memberId")
    private String memberId;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "holder_id")
    private String holderId;

//    @Column(name = "credit_card_number")
//    private String creditCardNumber;

//    @Column(name = "cvv")
//    private int cvv;
//
//    @Column(name = "month")
//    private int month;
//
//    @Column(name = "year")
//    private int year;

    @Column(name = "date")
    private Date date;

//    @ElementCollection
//    @CollectionTable(name = "receipt", joinColumns = @JoinColumn(name = "acquisition_id"))
//    @MapKeyColumn(name = "store_id")
//    @Column(name = "receipt_id")
    @Transient
    private Map<String, String> storeIdAndReceiptID = new HashMap<>();

    @Transient
    private Map<String, Receipt> receiptMap = new HashMap<>();

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "acquisition", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<ProductDetailReceipt> productDetailReceipts = new ArrayList<>();

    @Transient
    private final Object storeReceiptLock;

    public Acquisition(int transactionId , String acquisitionId, String userId, String memberId, int totalPrice,String url,  PaymentDTO payment, Map<String, Map<String, List<Integer>>> productList) {
        this.transactionId = transactionId;
        this.acquisitionId = acquisitionId;
        this.userId = userId;
        this.memberId = memberId;
        this.totalPrice = totalPrice;
        this.holderId = payment.getHolderId();
        this.url = url;

        //this.creditCardNumber = payment.getCreditCardNumber();
        //this.cvv = payment.getCvv();
        //this.month = payment.getMonth();
        //this.year = payment.getYear();
        this.date = new Date(); // Current date and time

        storeReceiptLock = new Object();

        for (String storeId : productList.keySet()) {
            String receiptID = getNewReceiptId();
            storeIdAndReceiptID.put(storeId, receiptID);
            receiptMap.put(storeId,createNewReceipt(receiptID,storeId,productList.get(storeId)));
            this.productDetailReceipts.addAll(convertToProductDetailReceipt(productList.get(storeId), storeId, receiptID));
        }
    }

    //convert product list from List<ProductDetailReceipt> to Map<String, List<Integer>>
    public Map<String, List<Integer>> convertToProductList(List<ProductDetailReceipt> productDetailReceipts){
        Map<String, List<Integer>> productList = new HashMap<>();
        for (ProductDetailReceipt productDetailReceipt : productDetailReceipts) {
            List<Integer> priceAndQuantity = new ArrayList<>();
            priceAndQuantity.add(productDetailReceipt.getAmount());
            priceAndQuantity.add(productDetailReceipt.getPrice());
            productList.put(productDetailReceipt.getId().getProductName(), priceAndQuantity);
        }
        return productList;
    }

    //convert product list from Map<String, List<Integer>> to List<ProductDetailReceipt>
    public List<ProductDetailReceipt> convertToProductDetailReceipt(Map<String, List<Integer>> productDetailReceipts, String storeId, String receiptId){
        List<ProductDetailReceipt> productList = new ArrayList<>();
        for (String productName : productDetailReceipts.keySet()) {
            List<Integer> priceAndQuantity = productDetailReceipts.get(productName);
            ProductDetailReceipt productDetailReceipt = new ProductDetailReceipt(new ProductDetailReceiptId(receiptId,storeId,productName), priceAndQuantity.get(0), priceAndQuantity.get(1),this);
            productList.add(productDetailReceipt);
        }
        return productList;
    }

    public Receipt createNewReceipt(String receiptID ,String storeID, Map<String, List<Integer>> products) {
        List<ProductDetailReceipt> productDetailReceipts = new ArrayList<>();
        for (String productName : products.keySet()) {
            int quantity = products.get(productName).get(0);
            int price = products.get(productName).get(1);
            ProductDetailReceiptId id = new ProductDetailReceiptId(receiptID,storeID,productName);
            ProductDetailReceipt productDetailReceipt = new ProductDetailReceipt(id,quantity,price, this);
            productDetailReceipts.add(productDetailReceipt);
        }
        return new Receipt(receiptID, storeID, memberId, productDetailReceipts);
    }

    public Acquisition() {
        this.storeReceiptLock = new Object();
    }

    public String getAcquisitionId() {
        return acquisitionId;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMemberId() {return memberId;}

    public void setMemberId(String memberId) {this.memberId = memberId;}

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public Map<String, Receipt> getReceiptMap() {
        return receiptMap;
    }

    public void setReceiptMap(Map<String, Receipt> receiptMap) {
        this.receiptMap = receiptMap;
    }

    public Map<String, String> getStoreIdAndReceiptID() {
        synchronized (storeReceiptLock) {
            return storeIdAndReceiptID;
        }
    }

//    public int getTotalPriceOfStoreInAcquisition(String storeId) {
//        synchronized (storeReceiptLock) {
//
//            Receipt receipt = createNewReceipt(storeIdAndReceiptID.get(storeId),storeId,)
//            return storeIdAndReceiptID.get(storeId).getTotalPriceOfStoreReceipt();
//        }
//    }

    public String getReceiptIdByStoreId(String storeId) {
        synchronized (storeReceiptLock) {
            return storeIdAndReceiptID.get(storeId);
        }
    }

    public int getTransactionId() {
        return transactionId;
    }

    public Map<String, String> getReceiptIdAndStoreIdMap() {
        Map<String, String> receiptIdAndStoreIdMap = new HashMap<>();
        synchronized (storeReceiptLock) {
            for (Map.Entry<String, String> entry : storeIdAndReceiptID.entrySet()) {
                receiptIdAndStoreIdMap.put(entry.getValue(), entry.getKey());
            }
        }
        return receiptIdAndStoreIdMap;
    }

    public List<ProductDetailReceipt> getProductDetailReceipts() {
        return productDetailReceipts;
    }

    public String getUrl() {
        return url;
    }


    public String getNewReceiptId() {
        UUID uuid = UUID.randomUUID();
        return "receipt-" + uuid.toString();
    }
}
