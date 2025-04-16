package IntegrationTests.User;

import DomainLayer.Store.Store;
import DomainLayer.User.Guest;
import DomainLayer.User.Member;
import DomainLayer.User.State;
import DomainLayer.User.User;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private User user;
    private String userID = "1";

    @BeforeEach
    public void setUp() {
        user = new User(userID);
    }

    @Test
    public void testUserInitialization() {
        assertEquals(userID, user.getUserID());
        assertFalse(user.getState().isMember());
        assertNotNull(user.getCart());
    }

    @Test
    public void testSetState() {
        State newState = new Guest();
        user.setState(newState);
        assertEquals(newState, user.getState());
    }

    @Test
    public void testLogout() {
        Member member = new Member("2", "1", "testUser", "123 Test St", "testName", "testPass", "01-01-2000", "testCountry", "testCity");
        user.setState(member);
        user.Logout();

        assertFalse(user.getState().isMember());
    }

    @Test
    public void testAddToCart() {
        user.addToCart("Product1", 2, "1", 100);
        assertFalse(user.getCart().isCartEmpty());
        assertTrue(user.checkIfProductInUserCart("Product1", "1"));
    }

    @Test
    public void testModifyProductInCart() {
        user.addToCart("Product1", 2, "1", 100);
        user.modifyProductInCart("Product1", 3, "1", 150);

        Map<String, List<Integer>> products = user.getCartProductsByStore("1");
        assertEquals(1, products.size());
        assertEquals(3, products.get("Product1").get(0));
    }

    @Test
    void testLogin() throws Exception {
        Member member = new Member("1", "1", "testUser", "123 Test St", "testName", "testPass", "01-01-2000", "testCountry", "testCity");
        user.Login(member);

        assertTrue(user.getState().isMember());
    }

    @Test
    public void testRemoveItemFromUserCart() {
        user.addToCart("Product1", 2, "1", 100);
        user.removeItemFromUserCart("Product1", "1");

        Map<String, List<Integer>> products = user.getCartProductsByStore("1");
        assertFalse(products.containsKey("Product1"));
    }

    @Test
    public void testGetCartProductsByStore() {
        user.addToCart("Product1", 2, "1", 100);
        user.addToCart("Product2", 1, "1", 50);

        Map<String, List<Integer>> products = user.getCartProductsByStore("1");
        assertEquals(2, products.size());
    }

    @Test
    public void testGetCartTotalPriceBeforeDiscount() {
        user.addToCart("Product1", 2, "1", 100);
        user.addToCart("Product2", 1, "1", 50);

        assertEquals(150, user.getCartTotalPriceBeforeDiscount());
    }

    @Test
    public void testIsCartEmpty() {
        assertTrue(user.isCartEmpty());
        user.addToCart("Product1", 2, "1", 100);
        assertFalse(user.isCartEmpty());
    }
}
