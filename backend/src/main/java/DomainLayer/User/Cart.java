package DomainLayer.User;

import Util.CartDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.*;

@Entity
@Table(name = "cart", schema = "themarketdb")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    // One-to-many relationship with Basket
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    // @JoinColumn(name = "cart_id") // This will create a cart_id column in the Basket table
    @MapKey(name = "storeId") // Column in the Basket table for store_id
    private Map<String, Basket> baskets; //key = storeID

    @Column(name = "cart_price")
    private int cartPrice;

    @Transient
    private final Object basketsLock;
    @Transient
    private final Object priceLock;

    public Cart() {
        this.cartPrice = 0;
        basketsLock = new Object();
        baskets = new HashMap<>();
        priceLock = new Object();
    }

    //Constructor injection for testing
    public Cart(Map<String, Basket> baskets, int cartPrice) {
        this.baskets = baskets;
        this.cartPrice = cartPrice;
        basketsLock = new Object();
        priceLock = new Object();
    }

    public int getCartPrice()
    {
        calcCartTotal();
        return cartPrice;
    }

    private void setCartPrice(int price)
    {
        synchronized (priceLock) {
            this.cartPrice = price;
        }
    }

    @Transactional
    public void addItemsToCart(String productName, int quantity, String storeId, int totalPrice)
    {
        Basket basket;
        synchronized (basketsLock) {
            if (baskets.containsKey(storeId)) {
                basket = baskets.get(storeId);
            } else {
                basket = new Basket(storeId);
                basket.setCart(this);
                baskets.put(storeId, basket);
            }
            basket.addProduct(productName, quantity, totalPrice);
        }

    }


    public void emptyCart(){
        synchronized (basketsLock) {
            this.baskets = new HashMap<>();
        }
        setCartPrice(0);

    }

    public void modifyProductInCart(String productName, int quantity, String storeId, int totalPrice)
    {
        synchronized (basketsLock) {
            if (!baskets.containsKey(storeId)) {
                throw new IllegalArgumentException("You can only modify items in your cart from existing store's basket.");
            }
            else
            {
                baskets.get(storeId).modifyProduct(productName, quantity, totalPrice);
            }
        }

    }

    public void calcCartTotal()
    {
        int totalCartPrice = 0;
        synchronized (basketsLock) {
            for (String storeId : baskets.keySet()) {
                Basket basket = baskets.get(storeId);
                totalCartPrice = totalCartPrice + basket.getBasketPrice();
            }
        }
        setCartPrice(totalCartPrice);

    }

    public boolean checkIfProductInCart(String productName, String storeId)
    {
        synchronized (basketsLock) {
            if (baskets.containsKey(storeId)) {
                return baskets.get(storeId).checkIfProductInBasket(productName);
            }
        }
        //return false;
        throw new IllegalArgumentException("The store id" + storeId + "you entered is invalid");
    }

    public void removeItemFromCart(String productName, String storeId)
    {
        synchronized (basketsLock) {
            if (baskets.containsKey(storeId)) {
                baskets.get(storeId).removeItemFromBasket(productName);
                if (baskets.get(storeId).isBasketEmpty()){
                    baskets.remove(storeId);
                }
            }
            else{
                throw new IllegalArgumentException("The store id" + storeId + "you entered is invalid");
            }
        }
    }

    public boolean isCartEmpty()
    {
        synchronized (basketsLock) {
            for (String storeId : baskets.keySet()) {
                Basket basket = baskets.get(storeId);
                if (!basket.isBasketEmpty())
                    return false;
            }
        }
        return true;

    }

    public Map<String, List<Integer>> getProductsDetailsByStore(String store_ID)
    {
        synchronized (basketsLock) {
            if (!baskets.containsKey(store_ID)) {
                return new HashMap<>();
            }
            return baskets.get(store_ID).getProducts();
        }
    }

    public Map<String, Integer> getProductsQuantityByStore(String store_ID)
    {
        Map<String, List<Integer>> products = this.getProductsDetailsByStore(store_ID);
        //create new map, contains only the quantity of each product
        Map<String, Integer> productsQuantity = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : products.entrySet()) {
            String productName = entry.getKey();
            List<Integer> details = entry.getValue();

            if (details != null && !details.isEmpty()) {
                Integer quantity = details.get(0);
                productsQuantity.put(productName, quantity);
            }
        }

        return productsQuantity;
    }


    public List<String> getCartStores()
    {
        /*this function returns the stores from which the user added his products*/
        synchronized (basketsLock) {
            return new ArrayList<>(baskets.keySet());
        }
    }

    public CartDTO getDTO(String userId){
        return new CartDTO(userId, getCartPrice(),getBasketForDTO());
    }

    public Map<String,Map<String ,List<Integer>>> getBasketForDTO() {
        Map<String,Map<String ,List<Integer>>> basketsProd = new HashMap<>();
        synchronized (basketsLock) {
            for (String id : baskets.keySet()) {
                basketsProd.put(id, baskets.get(id).getProducts());
            }
        }
        return basketsProd;
    }

    public Map<String, Basket> getBaskets() {
        return baskets;
    }

    public Long getCartId() {
        return id;
    }
}

