package DomainLayer.User;

import DomainLayer.User.Basket;
import DomainLayer.User.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartTest {

    private Cart cart;
    private Basket mockBasket;
    private Map<String, Basket> mockMap;

    @BeforeEach
    public void setUp() {
        mockBasket = mock(Basket.class);
        mockMap = mock(Map.class);
        when(mockMap.containsKey("1")).thenReturn(true);
        when(mockMap.get("1")).thenReturn(mockBasket);
        cart = new Cart(mockMap, 0);
    }

    @Test
    public void testCartInitialization() {
        assertEquals(0, cart.getCartPrice());
        assertNotNull(cart.getBaskets());
    }

    @Test
    public void testAddItemsToCart() {
        String productName = "Product1";
        int quantity = 2;
        String storeId = "1";
        int totalPrice = 100;

        cart.addItemsToCart(productName, quantity, storeId, totalPrice);
        verify(mockBasket).addProduct(productName, quantity, totalPrice);
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

        verify(mockBasket).modifyProduct(productName, modifiedQuantity, modifiedTotalPrice);
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

        when(mockBasket.getBasketPrice()).thenReturn(100);
        when(mockMap.keySet()).thenReturn(Set.of("1"));
        cart.calcCartTotal();

        // Since the second basket is not mocked, we assume the cart's total price includes only the first basket's price
        assertEquals(totalPrice1, cart.getCartPrice());
    }

    @Test
    public void testCheckIfProductInCart() {
        String productName = "Product1";
        String storeId = "1";

        when(mockBasket.checkIfProductInBasket(productName)).thenReturn(true);

        assertTrue(cart.checkIfProductInCart(productName, storeId));
        verify(mockBasket).checkIfProductInBasket(productName);
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

        verify(mockBasket).removeItemFromBasket(productName);
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
