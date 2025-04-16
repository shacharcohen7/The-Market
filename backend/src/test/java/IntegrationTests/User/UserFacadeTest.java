package IntegrationTests.User;

import AcceptanceTests.RealToTest;
import DomainLayer.Repositories.ExternalSupplyRepository;
import DomainLayer.Repositories.MemberRepository;
import DomainLayer.Repositories.UserRepository;
import DomainLayer.User.Member;
import DomainLayer.User.User;
import DomainLayer.User.UserFacade;
import PresentationLayer.Application;
import Util.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;


@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserFacadeTest {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public MemberRepository memberRepository;

    private UserFacade userFacade;


    private final String userId = "1";
    private final String storeId = "1";
    private final String productName = "Product1";
    private final String productName2 = "Product2";
    private final int quantity = 2;
    private final int totalPrice = 100;

    @BeforeEach
    public void setUp() throws Exception {
        userFacade = new UserFacade(userRepository, memberRepository);

        // Reset the UserFacade singleton for each test
        userFacade.getUserRepository().deleteAll();

        // Create a new user and add to the UserFacade
        User user = new User(userId);
        userFacade.getUserRepository().save(user);
    }

    @AfterEach
    public void tearDown() {
        // Reset the UserFacade singleton for each test
        userFacade.getUserRepository().deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    public void testGetUserByID() {
        User user = userFacade.getUserByID(userId);
        assertNotNull(user);
        assertEquals(userId, user.getUserID());
    }

    @Test
    public void testAddUser() {
        String newUserId = userFacade.addUser();
        User newUser = userFacade.getUserByID(newUserId);
        assertNotNull(newUser);
        assertEquals(newUserId, newUser.getUserID());
    }

    @Test
    public void testRegister() throws Exception {
        String newUserId = userFacade.addUser();
        UserDTO userDTO = new UserDTO(newUserId, "testUser", "01/01/2000", "Test Country", "Test City", "123 Test St", "Test Name");
        userFacade.register(newUserId, userDTO, "testPass");

        Member member = userFacade.getMemberByUsername("testUser");
        assertNotNull(member);
        assertEquals("testUser", member.getUsername());
    }

    @Test
    public void testAddItemsToBasket() {
        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);

        User user = userFacade.getUserByID(userId);
        assertTrue(user.checkIfProductInUserCart(productName, storeId));
        assertEquals(1, user.getCartProductsByStore(storeId).size());
    }

    @Test
    public void testModifyBasketProduct() {
        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);
        userFacade.modifyBasketProduct(productName, quantity + 1, storeId, userId, totalPrice + 50);

        User user = userFacade.getUserByID(userId);
        assertEquals(3, user.getCartProductsByStore(storeId).get(productName).get(0));
        assertEquals(150, user.getCartProductsByStore(storeId).get(productName).get(1));
    }

    @Test
    public void testCheckIfCanRemove() {
        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);

        assertDoesNotThrow(() -> userFacade.checkIfCanRemove(productName, storeId, userId));
    }

    @Test
    public void testRemoveItemFromUserCart() {
        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);
        userFacade.addItemsToBasket(productName2, quantity, storeId, userId, totalPrice);

        userFacade.removeItemFromUserCart(productName, storeId, userId);

        User user = userFacade.getUserByID(userId);
        assertFalse(user.checkIfProductInUserCart(productName, storeId));
    }
}
