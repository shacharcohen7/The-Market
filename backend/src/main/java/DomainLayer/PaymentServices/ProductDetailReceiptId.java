package DomainLayer.PaymentServices;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductDetailReceiptId implements Serializable {

    private String receiptId;

    private String storeId;

    private String productName;

    // Update constructor, getters, and setters accordingly
    public ProductDetailReceiptId(String receiptId, String storeId, String productName) {
        this.storeId = storeId;
        this.receiptId = receiptId;
        this.productName = productName;
    }

    public ProductDetailReceiptId() {

    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String store_id) {
        this.storeId = store_id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
