package DomainLayer.PaymentServices;

import jakarta.persistence.*;

@Entity
@Table(name = "product_detail_receipt")
public class ProductDetailReceipt {

    @EmbeddedId
    private ProductDetailReceiptId productDetailReceiptId;

    @Column(name = "amount")
    private int amount;

    @Column(name = "price")
    private int price;

    @ManyToOne
    @JoinColumn(name = "acquisition_id", nullable = false)
    private Acquisition acquisition;

    public ProductDetailReceipt(ProductDetailReceiptId id, int amount, int price , Acquisition acquisition) {
        this.productDetailReceiptId = id;
        this.amount = amount;
        this.price = price;
        this.acquisition = acquisition;
    }

    public ProductDetailReceipt() {
    }

    // Getters and Setters
    public ProductDetailReceiptId getId() {
        return productDetailReceiptId;
    }

    public void setId(ProductDetailReceiptId id) {
        this.productDetailReceiptId = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }

    public Acquisition getAcquisition() {
        return acquisition;
    }

    public void setAcquisition(Acquisition acquisition) {
        this.acquisition = acquisition;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
