package DomainLayer.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BasketTest {

    private Basket basket;
    private final String storeId = "100";

    @BeforeEach
    public void setUp() {
        basket = new Basket(storeId);
    }

    @Test
    public void testBasketInitialization() {
        assertEquals(storeId, basket.getStoreId());
        assertEquals(0, basket.getBasketPrice());
        assertNotNull(basket.getProducts());
        assertTrue(basket.getProducts().isEmpty());
    }

    @Test
    public void testAddProduct() {
        String productName = "Product1";
        int quantity = 2;
        int totalPrice = 100;

        basket.addProduct(productName, quantity, totalPrice);

        // Check if product was added correctly
        Map<String, List<Integer>> products = basket.getProducts();
        assertTrue(products.containsKey(productName));
        assertEquals(quantity, products.get(productName).get(0).intValue());
        assertEquals(totalPrice, products.get(productName).get(1).intValue());
    }

    @Test
    public void testModifyProduct() {
        String productName = "Product1";
        int initialQuantity = 1;
        int initialTotalPrice = 50;
        int newQuantity = 2;
        int newTotalPrice = 100;

        basket.addProduct(productName, initialQuantity, initialTotalPrice);
        basket.modifyProduct(productName, newQuantity, newTotalPrice);

        List<Integer> quantityAndPrice = basket.getProducts().get(productName);
        assertEquals(newQuantity, quantityAndPrice.get(0));
        assertEquals(newTotalPrice, quantityAndPrice.get(1));
    }

    @Test
    public void testModifyNonExistentProduct() {
        String productName = "Product1";
        int quantity = 2;
        int totalPrice = 100;

        assertThrows(IllegalArgumentException.class, () -> {
            basket.modifyProduct(productName, quantity, totalPrice);
        });
    }

    @Test
    public void testCalcBasketPrice() {
        String productName1 = "Product1";
        String productName2 = "Product2";
        int quantity1 = 1;
        int quantity2 = 2;
        int totalPrice1 = 50;
        int totalPrice2 = 100;

        basket.addProduct(productName1, quantity1, totalPrice1);
        basket.addProduct(productName2, quantity2, totalPrice2);

        basket.calcBasketPrice();
        assertEquals(totalPrice1 + totalPrice2, basket.getBasketPrice());
    }

    @Test
    public void testCheckIfProductInBasket() {
        String productName = "Product1";
        int quantity = 1;
        int totalPrice = 50;

        basket.addProduct(productName, quantity, totalPrice);
        assertTrue(basket.checkIfProductInBasket(productName));
        assertFalse(basket.checkIfProductInBasket("NonExistentProduct"));
    }

    @Test
    public void testRemoveItemFromBasket() {
        String productName = "Product1";
        int quantity = 1;
        int totalPrice = 50;

        basket.addProduct(productName, quantity, totalPrice);
        assertTrue(basket.checkIfProductInBasket(productName));

        basket.removeItemFromBasket(productName);
        assertFalse(basket.checkIfProductInBasket(productName));
    }
}
