package DomainLayer.PaymentServices;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Receipt {

    private String receiptId;

    private String storeId;

    private String memberId;

    private List<ProductDetailReceipt> productList = new ArrayList<>();

    private Object productListLock = new Object();

    public Receipt(String receiptId, String storeId, String memberId, List<ProductDetailReceipt> productList) {
        this.receiptId = receiptId;
        this.storeId = storeId;
        this.memberId = memberId;
        this.productList = productList;
    }

    public Receipt() {
        productListLock = new Object();
    }

    public int getTotalPriceOfStoreReceipt() {
        synchronized (productListLock) {
            int storePrice = 0;
            for (ProductDetailReceipt product : productList) {
                storePrice += product.getPrice();
            }
            return storePrice;
        }
    }

    public String getStoreId() {
        return storeId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public String getMemberId() {
        return memberId;
    }

    public List<ProductDetailReceipt> getProductList() {
        return productList;
    }

    public Map<String, List<Integer>> getProductListToMap() {
        Map<String, List<Integer>> productsList = new HashMap<>();
        for (ProductDetailReceipt product : productList) {
            List<Integer> quantityAndPrice = new ArrayList<>();
            quantityAndPrice.add(product.getAmount());
            quantityAndPrice.add(product.getPrice());
            productsList.put(product.getId().getProductName(), quantityAndPrice);
        }
        return productsList;
    }
}
