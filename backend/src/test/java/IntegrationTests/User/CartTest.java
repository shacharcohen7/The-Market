package IntegrationTests.User;

import DomainLayer.User.Basket;
import DomainLayer.User.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

    private Cart cart;

    @BeforeEach
    public void setUp() {
        cart = new Cart();
    }

    @Test
    public void testCartInitialization() {
        assertEquals(0, cart.getCartPrice());
        assertNotNull(cart.getBaskets());
        assertTrue(cart.getBaskets().isEmpty());
    }

    @Test
    public void testAddItemsToCart() {
        String productName = "Product1";
        int quantity = 2;
        String storeId = "1";
        int totalPrice = 100;

        cart.addItemsToCart(productName, quantity, storeId, totalPrice);

        // Check if product was added correctly
        Map<String, List<Integer>> products = cart.getProductsDetailsByStore(storeId);
        assertTrue(products.containsKey(productName));
        assertEquals(quantity, products.get(productName).get(0).intValue());
        assertEquals(totalPrice, products.get(productName).get(1).intValue());
    }

    @Test
    public void testModifyProductInCart() {
        String productName = "Product1";
        int initialQuantity = 2;
        int modifiedQuantity = 3;
        String storeId = "1";
        int initialTotalPrice = 100;
        int modifiedTotalPrice = 150;

        cart.addItemsToCart(productName, initialQuantity, storeId, initialTotalPrice);
        cart.modifyProductInCart(productName, modifiedQuantity, storeId, modifiedTotalPrice);

        // Check if product was modified correctly
        Map<String, List<Integer>> products = cart.getProductsDetailsByStore(storeId);
        assertTrue(products.containsKey(productName));
        assertEquals(modifiedQuantity, products.get(productName).get(0).intValue());
        assertEquals(modifiedTotalPrice, products.get(productName).get(1).intValue());
    }

    @Test
    public void testModifyProductInNonExistentCart() {
        String productName = "Product1";
        int quantity = 2;
        String storeId = "100";
        int totalPrice = 100;

        assertThrows(IllegalArgumentException.class, () -> {
            cart.modifyProductInCart(productName, quantity, storeId, totalPrice);
        });
    }

    @Test
    public void testCalcCartTotal() {
        String productName1 = "Product1";
        String productName2 = "Product2";
        int quantity1 = 2;
        int quantity2 = 3;
        String storeId1 = "1";
        String storeId2 = "2";
        int totalPrice1 = 100;
        int totalPrice2 = 200;

        cart.addItemsToCart(productName1, quantity1, storeId1, totalPrice1);
        cart.addItemsToCart(productName2, quantity2, storeId2, totalPrice2);

        cart.calcCartTotal();

        // Check if cart total price was calculated correctly
        assertEquals(totalPrice1 + totalPrice2, cart.getCartPrice());
    }

    @Test
    public void testCheckIfProductInCart() {
        String productName = "Product1";
        String storeId = "1";

        Basket basket = new Basket(storeId);
        basket.addProduct(productName, 1, 100);
        cart.getBaskets().put(storeId, basket);

        assertTrue(cart.checkIfProductInCart(productName, storeId));
    }

    @Test
    public void testCheckIfProductInNonExistentCart() {
        String productName = "Product1";
        String storeId = "100";

        assertThrows(IllegalArgumentException.class, () -> {
            cart.checkIfProductInCart(productName, storeId);
        });
    }

    @Test
    public void testRemoveItemFromCart() {
        String productName = "Product1";
        int quantity = 2;
        String storeId = "1";
        int totalPrice = 100;

        cart.addItemsToCart(productName, quantity, storeId, totalPrice);
        cart.removeItemFromCart(productName, storeId);

        // Check if product was removed correctly
        Map<String, List<Integer>> products = cart.getProductsDetailsByStore(storeId);
        assertFalse(products.containsKey(productName));
        assertThrows(IllegalArgumentException.class, () -> cart.removeItemFromCart(productName, "999"));
    }

    @Test
    public void testRemoveItemFromNonExistentCart() {
        String productName = "Product1";
        String storeId = "100";

        assertThrows(IllegalArgumentException.class, () -> {
            cart.removeItemFromCart(productName, storeId);
        });
    }
}
