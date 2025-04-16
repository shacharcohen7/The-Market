package DomainLayer.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "basket")
public class Basket {
    @Id
    @Column(name = "storeId")
    private String storeId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "storeId")
    private Map<String, ProductDetails> products; //key = product name, value = [quantity, products total price]

    @Column(name = "basket_price")
    private int basketPrice;

    @Transient
    private final Object basketPriceLock;

    @Transient
    private final Object productsLock;


    public Basket(String storeId) {
        this.storeId = storeId;
        this.basketPrice = 0;
        basketPriceLock = new Object();
        products = new HashMap<>();
        productsLock =new Object();
    }


    public Basket() {
        this.storeId = "";
        this.basketPrice = 0;
        basketPriceLock = new Object();
        products = new HashMap<>();
        productsLock =new Object();
    }

    public String getStoreId()
    {
        return this.storeId;
    }



    public int getBasketPrice()
    {
        synchronized (basketPriceLock) {
            calcBasketPrice();
            return this.basketPrice;
        }
    }

    public void setBasketPrice(int price)
    {
        synchronized (basketPriceLock) {
            this.basketPrice = price;
        }
    }

    public synchronized Map<String, List<Integer>> getProducts() {
        synchronized (productsLock) {
            Map<String, List<Integer>> products = new HashMap<>();
            for (String productName : this.products.keySet()) {
                List<Integer> quantityAndPrice = new ArrayList<>();
                quantityAndPrice.add(this.products.get(productName).getQuantity());
                quantityAndPrice.add(this.products.get(productName).getTotalPrice());
                products.put(productName, quantityAndPrice);
            }
            return products;
        }
    }

    //@Transactional
    public synchronized void addProduct(String productName, int quantity, int totalPrice)
    {
        synchronized (productsLock) {
            ProductDetails productDetails = new ProductDetails(quantity, totalPrice, productName);
            products.put(productName, productDetails);
        }
    }

    public synchronized void modifyProduct(String productName, int quantity, int totalPrice)
    {
        synchronized (productsLock) {
            if (!products.containsKey(productName)) {
                throw new IllegalArgumentException("The item you try to edit is not in your basket. Please add the item before attempting to modify it.");
            }
        }
        synchronized (productsLock) {
            //todo: check if it is actually changing the values
            ProductDetails productDetails  = products.get(productName);
            productDetails.setQuantity(quantity);
            productDetails.setTotalPrice(totalPrice);
            products.put(productName, productDetails);
        }
    }

    public synchronized void calcBasketPrice()
    {
        int totalPrice = 0;
        synchronized (productsLock) {
            for (String productName : products.keySet()) {
                int totalItemPrice = products.get(productName).getTotalPrice();
                totalPrice += totalItemPrice;
            }
        }
        setBasketPrice(totalPrice);
    }

    public synchronized boolean checkIfProductInBasket(String productName)
    {
        synchronized (productsLock) {
            return products.containsKey(productName);
        }

    }

    public synchronized void removeItemFromBasket(String productName)
    {
        synchronized (productsLock) {
            products.remove(productName);
        }
    }

    public synchronized boolean isBasketEmpty()
    {
        synchronized (productsLock){
            return products.isEmpty();
        }

    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}
