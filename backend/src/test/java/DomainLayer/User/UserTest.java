package DomainLayer.User;

import DomainLayer.Store.Store;
import DomainLayer.User.*;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTest {

    private User user;
    private Member mockMember;
    private Cart mockCart;
    private Store storeMock;
    private State mockState;
    private String userID = "1";

    @BeforeEach
    public void setUp() {
        user = new User("1");
        mockMember = mock(Member.class);

        // Create the mocks
        mockCart = Mockito.mock(Cart.class);
        storeMock = Mockito.mock(Store.class);
        mockState = Mockito.mock(State.class);
        // Initialize the user and set the user's cart
        user.setCart(mockCart);
    }

    @Test
    public void testUserInitialization() {
        assertEquals(userID, user.getUserID());
        assertFalse(user.getState().isMember());
        assertNotNull(user.getCart());
    }

    @Test
    public void testSetState() {
        user.setState(mockState);
        assertEquals(mockState, user.getState());
    }

    @Test
    public void testLogout() {
        user.setState(mockMember);
        user.Logout();
        doNothing().when(mockMember).Logout();

        //verify that the logout method was called on the mock member
        verify(mockMember).Logout();
        assertFalse(user.isMember());
    }

    @Test
    public void testAddToCart() {
        String productName = "Product1";
        int quantity = 2;
        String storeId = "1";
        int price = 100;

        user.addToCart(productName, quantity, storeId, price);

        verify(mockCart).addItemsToCart(productName, quantity, storeId, price);
    }

    @Test
    public void testModifyProductInCart() {
        String productName = "Product1";
        int quantity = 3;
        String storeId = "1";
        int price = 150;

        user.modifyProductInCart(productName, quantity, storeId, price);

        verify(mockCart).modifyProductInCart(productName, quantity, storeId, price);
    }


    @Test
    public void testLogin() throws Exception {
        user.Login(mockMember);
        when(mockMember.isMember()).thenReturn(true);

        assertTrue(user.getState().isMember());
    }

    @Test
    public void testRemoveItemFromUserCart() {
        String productName = "Product1";
        String storeId = "1";

        user.removeItemFromUserCart(productName, storeId);

        verify(mockCart).removeItemFromCart(productName, storeId);
    }

    @Test
    public void testGetCartProductsByStore() {
        String storeId = "1";
        Map<String, List<Integer>> mockProducts = new HashMap<>();
        when(mockCart.getProductsDetailsByStore(storeId)).thenReturn(mockProducts);

        Map<String, List<Integer>> products = user.getCartProductsByStore(storeId);

        assertEquals(mockProducts, products);
        verify(mockCart).getProductsDetailsByStore(storeId);
    }

    @Test
    public void testGetCartTotalPriceBeforeDiscount() {
        int totalPrice = 150;
        when(mockCart.getCartPrice()).thenReturn(totalPrice);

        assertEquals(totalPrice, user.getCartTotalPriceBeforeDiscount());
    }


    @Test
    public void testIsCartEmpty() {
        when(mockCart.isCartEmpty()).thenReturn(true, false);

        assertTrue(user.isCartEmpty());
        assertFalse(user.isCartEmpty());
    }

    @Test
    public void testCheckIfProductInUserCart() {
        String productName = "Product1";
        String storeId = "1";

        when(mockCart.checkIfProductInCart(productName, storeId)).thenReturn(true);

        assertTrue(user.checkIfProductInUserCart(productName, storeId));
        verify(mockCart).checkIfProductInCart(productName, storeId);
    }

}
