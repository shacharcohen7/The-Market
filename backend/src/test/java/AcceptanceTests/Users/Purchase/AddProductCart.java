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

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AddProductCart {

    @Autowired
    private BridgeToTests impl;
    static String userId0;
    static String storeId0;
    static String userId1;


    @BeforeEach
    public void setUp() {
        userId0 = impl.enterMarketSystem().getData();
        userId1 = impl.enterMarketSystem().getData();
        impl.register(userId0, "user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login(userId0, "user1", "fSijsd281");
        impl.register(userId1, "user2", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login(userId1, "user2", "fSijsd281");


        storeId0 = impl.openStore(userId0, "Zara", "clothing store").getData();
        impl.addProductToStore(userId0, storeId0, "Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(userId0, storeId0, "Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(userId0, storeId0, "Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(userId0, storeId0, "Shoes", 4, 12, "Nike Shoes", "clothing");
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }


    @Test
    public void successfulAdditionTest()
    {
        assertTrue(impl.addProductToBasket("Milk", 2, storeId0, userId0).isSuccess());
        assertTrue(impl.addProductToBasket("Cheese", 4, storeId0, userId0).isSuccess());
        assertTrue(impl.addProductToBasket("Yogurt", 5, storeId0, userId0).isSuccess());
    }

    @Test
    public void invalidProductNameTest()
    {

        Response<String> response1 = impl.addProductToBasket("Computer", 2, storeId0, userId0);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response1.getDescription());


        Response<String> response2 = impl.addProductToBasket("Shirt", 4, storeId0, userId0);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response2.getDescription());


        Response<String> response3 = impl.addProductToBasket("TV", 5, storeId0, userId0);
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response3.getDescription());
    }

    @Test
    public void outOfStockProductTest()
    {
        impl.addProductToStore(userId0, storeId0, "Mouse", 10, 0, "HP Mouse", "electronics");
        impl.addProductToStore(userId0, storeId0, "Laptop", 15, 0, "HP Laptop ", "electronics");


        Response<String> response1 = impl.addProductToBasket("Mouse", 1, storeId0, userId0);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response1.getDescription());


        Response<String> response2 = impl.addProductToBasket("Laptop", 2, storeId0, userId0);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response2.getDescription());
    }

    @Test
    public void bigQuantityTest()
    {

        Response<String> response1 = impl.addProductToBasket("Milk", 10, storeId0, userId0);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response1.getDescription());


        Response<String> response2 = impl.addProductToBasket("Cheese", 9, storeId0, userId0);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response2.getDescription());


        Response<String> response3 = impl.addProductToBasket("Yogurt", 13, storeId0, userId0);
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response3.getDescription());
    }

    @Test
    public void negQuantityTest()
    {

        Response<String> response1 = impl.addProductToBasket("Milk", -4, storeId0, userId0);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response1.getDescription());


        Response<String> response2 = impl.addProductToBasket("Cheese", -1, storeId0, userId0);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response2.getDescription());


        Response<String> response3 = impl.addProductToBasket("Yogurt", -8, storeId0, userId0);
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response3.getDescription());
    }

    @Test
    public void purchasePolicyInvalidTest()
    {
        TestRuleDTO rule = new TestRuleDTO("Amount", "Above", null, "corn", "Basket must contain at least 2 corns", true, null, 2, null, null, null);
        impl.addPurchaseRuleToStore(List.of(rule), new ArrayList<>(), userId0, storeId0);
        Response<String> response = impl.addProductToBasket("Milk", 2, storeId0, userId0);
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.purchasePolicyIsNotMet.toString(), response.getDescription());
    }

    @Test
    public void concurrentAddProductToBasketTest() throws Exception {
        // Add a product with quantity 1
        setUp();
        for (int i=0 ; i<1000 ; i++) {

            impl.addProductToStore(userId0, storeId0, "Limited Edition Shirt", 50, 1, "Rare shirt", "clothing");

            // Set up the thread executor and count down latch
            ExecutorService executor = Executors.newFixedThreadPool(2);
            CountDownLatch latch = new CountDownLatch(1);

            Future<Response<String>> future1 = executor.submit(() -> {
                latch.await(); // Wait until both threads are ready to run
                return impl.addProductToBasket("Limited Edition Shirt", 1, storeId0, userId0);
            });

            Future<Response<String>> future2 = executor.submit(() -> {
                latch.await(); // Wait until both threads are ready to run
                return impl.addProductToBasket("Limited Edition Shirt", 1, storeId0, userId1);
            });

            // Start both threads at the same time
            latch.countDown();

            // Get the results of both threads
            Response<String> response1 = future1.get();
            Response<String> response2 = future2.get();

            // Shut down the executor
            executor.shutdown();

            // Check that only one thread succeeded
            assertTrue(response1.isSuccess() & response2.isSuccess(), "Only one thread should succeed in adding the last product to the basket");
            if (!response1.isSuccess()) {
                assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response1.getDescription());
            }
            if (!response2.isSuccess()) {
                assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response2.getDescription());
            }

        }
    }

}
