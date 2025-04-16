package DomainLayer.User;
import jakarta.persistence.*;

import javax.persistence.Embeddable;

@Entity
@Table(name = "basket_products")
public class ProductDetails {

    @Column(name = "quantity")
    private int quantity;
    @Column(name = "product_total_price")
    private int totalPrice;

    @Id
    @Column(name = "product_name")
    private String product_name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "storeId")
    private Basket basket;

    // Constructors, getters, and setters
    public ProductDetails() {
    }

    public ProductDetails(int quantity, int totalPrice, String product_name){
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.product_name = product_name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setId(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_name() {
        return product_name;
    }
}
