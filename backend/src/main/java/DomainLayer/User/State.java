package DomainLayer.User;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Map;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class State {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    protected Cart cart;

    protected State(){
        cart = new Cart();
    }

    //@Transactional
    public void addItemsToCart(String productName, int quantity, String storeId, int totalPrice)
    {
        cart.addItemsToCart(productName, quantity, storeId, totalPrice);
    }

    public void modifyProductInCart(String productName, int quantity, String storeId, int totalPrice)
    {
        cart.modifyProductInCart(productName, quantity, storeId, totalPrice);
    }

    public void calcCartTotal()
    {
        this.cart.calcCartTotal();
    }

    public boolean checkIfProductInUserCart(String productName, String storeId)
    {
        return cart.checkIfProductInCart(productName, storeId);
    }

    public void removeItemFromUserCart(String productName, String storeId)
    {
        cart.removeItemFromCart(productName, storeId);
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Cart getCart() {
        return cart;
    }

    public Map<String, List<Integer>> getCartProductsByStore(String storeId)
    {
        return cart.getProductsDetailsByStore(storeId);
    }

    public List<String> getCartStores()
    {
        return cart.getCartStores();
    }

    public boolean isCartEmpty()
    {
        return this.cart.isCartEmpty();
    }

    public int getCartTotalPriceBeforeDiscount()
    {
        return this.cart.getCartPrice();
    }

    public Long getCartID(){
        return cart.getCartId();
    }


    protected abstract String getMemberID();

    protected abstract void Logout();
    protected abstract void exitMarketSystem();
    //void Register(User user, String username, String password, String birthday, String address) throws Exception;
    protected abstract void Login() throws Exception;
    public abstract boolean isMember();
    public abstract String getUsername();
//    public abstract void addAcquisition(String acquisitionId);
//    public abstract List<String> getAcquisitionIds();
//    public abstract int removeAcquisition(String acquisitionId) ;
}
