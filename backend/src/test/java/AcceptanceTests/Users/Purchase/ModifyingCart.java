package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.ProductDTO;
import Util.TestRuleDTO;
import Util.UserDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ModifyingCart {

    @Autowired
    private BridgeToTests impl;

    private static String userID1;
    private static String storeID1;


    @BeforeEach
    public void setUp() {
        userID1 = impl.enterMarketSystem().getData();
        impl.register(userID1,"user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login(userID1, "user1", "fSijsd281");
        storeID1 = impl.openStore(userID1, "good store", "number 1").getData();
        impl.addProductToStore(userID1, storeID1,"Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(userID1, storeID1,"Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(userID1, storeID1,"Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(userID1, storeID1,"Shoes", 4, 12, "Nike Shoes", "clothing");

        impl.addProductToBasket("Milk", 2, storeID1, userID1);
        impl.addProductToBasket("Cheese", 4, storeID1, userID1);
        impl.addProductToBasket("Yogurt", 5, storeID1, userID1);
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulUpdateTest() {
        assertTrue(impl.modifyShoppingCart("Milk", 2,storeID1, userID1).isSuccess());
        assertTrue(impl.modifyShoppingCart("Cheese", 3, storeID1, userID1).isSuccess());
        assertTrue(impl.modifyShoppingCart("Yogurt", 4, storeID1, userID1).isSuccess());
    }

    @Test
    public void productNotExistTest() {

        Response<String> response1 = impl.modifyShoppingCart("Tomato", 2,storeID1, userID1);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInCart.toString(), response1.getDescription());

        Response<String> response2 = impl.modifyShoppingCart("awsdhnjlk", 3, storeID1, userID1);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInCart.toString(), response2.getDescription());

        Response<String> response3 = impl.modifyShoppingCart("njknlk", 4, storeID1, userID1);
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInCart.toString(), response3.getDescription());
    }


    @Test
    public void bigQuantityTest() {

        Response<String> response1 = impl.modifyShoppingCart("Milk", 100,storeID1, userID1);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response1.getDescription());

        Response<String> response2 = impl.modifyShoppingCart("Cheese", 200, storeID1, userID1);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response2.getDescription());

        Response<String> response3 = impl.modifyShoppingCart("Yogurt", 300, storeID1, userID1);
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response3.getDescription());

    }

    @Test
    public void negQuantityTest() {

        Response<String> response1 = impl.modifyShoppingCart("Milk", -1,storeID1, userID1);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response1.getDescription());

        Response<String> response2 = impl.modifyShoppingCart("Cheese", -2, storeID1, userID1);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response2.getDescription());

        Response<String> response3 = impl.modifyShoppingCart("Yogurt", -3, storeID1, userID1);
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response3.getDescription());

    }

    @Test
    public void purchasePolicyInvalidTest() {
        TestRuleDTO rule = new TestRuleDTO("Amount", "Above", null, "corn", "Basket must contain at least 2 corns", true, null, 2, null, null, null);
        impl.addPurchaseRuleToStore(List.of(rule), new ArrayList<>(), userID1, storeID1);
        Response<String> response = impl.addProductToBasket("Milk", 2, storeID1, userID1);
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.purchasePolicyIsNotMet.toString(), response.getDescription());
    }
}
