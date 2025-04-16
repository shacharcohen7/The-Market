package DomainLayer.Store;

import Util.ProductDTO;
import jakarta.persistence.*;

import java.awt.*;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", unique = true)
    private String productName;

    @Column(name = "price")
    private int price;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "rating")
    private double rating;

    @Column(name = "num_of_ratings")
    private int numOfRatings;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;

    @Column(name = "description")
    private String description;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinColumn(name = "store_id")
//    private Store store;

    public Product() {
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(ProductDTO product, Category category) {
        this.productName = product.getName();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.rating = 0;
        this.numOfRatings = 0;
        this.description = product.getDescription();
        this.category = category;
        //this.store = store;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public int getPrice()
    {
        return this.price;
    }

    public String getProductName() {
        return this.productName;
    }

    public String getCategoryName() {
        return this.category.toString();
    }

    public double getRating() {
        return this.rating;
    }

    public int getNumOfRatings() {
        return this.numOfRatings;
    }

    public String getDescription() {
        return this.description;
    }

    public void addToStock(int amount){
        this.quantity = quantity+amount;
    }

    public ProductDTO getProductDTO(){
        return new ProductDTO(productName, price,quantity,description,category.name());
    }


}
