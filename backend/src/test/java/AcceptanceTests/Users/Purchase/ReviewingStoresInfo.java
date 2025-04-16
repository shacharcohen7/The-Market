package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import ServiceLayer.Response;
import Util.PaymentDTO;
import Util.UserDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ReviewingStoresInfo {

    @Autowired
    private BridgeToTests impl;

    private static String userID1;
    private static String userID2;
    private static String userID3;
    private static String storeID1;
    private static String storeID2;
    private static String storeID3;

    @BeforeEach
    public void setUp() {
        userID1 = impl.enterMarketSystem().getData();
        userID2 = impl.enterMarketSystem().getData();
        userID3 = impl.enterMarketSystem().getData();
        impl.register(userID1,"user1",  "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.register(userID2,"user2",  "12/12/99", "Israel", "Beer Sheva", "Mesada", "Nitzan", "fSijsd28cd1");

        impl.login(userID1, "user1", "fSijsd281");
        impl.login(userID2, "user2", "fSijsd28cd1");

        storeID1 = impl.openStore(userID1, "Bershka", "clothing store").getData();
        storeID2 = impl.openStore(userID1, "Zara", "clothing store").getData();
        storeID3 = impl.openStore(userID1, "PullAndBear", "clothing store").getData();
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void successfulStoreOwnerViewTest() {
        impl.closeStore(userID1, storeID3);

        List<String> allAvailableStores = new ArrayList<>();
        allAvailableStores.add(storeID1);
        allAvailableStores.add(storeID2);
        allAvailableStores.add(storeID3);

        Response<List<String>> res = impl.getInformationAboutStores(userID1);
        assertTrue(res.isSuccess());

        Set<String> expectedStoresSet = new HashSet<>(allAvailableStores);
        Set<String> actualStoresSet = new HashSet<>(res.getResult());

        assertEquals(expectedStoresSet, actualStoresSet);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void successfulMemberViewTest() {
        impl.closeStore(userID1, storeID3);

        List<String> allAvailableStores = new ArrayList<>();
        allAvailableStores.add(storeID1);
        allAvailableStores.add(storeID2);

        Response<List<String>> res = impl.getInformationAboutStores(userID2);
        assertTrue(res.isSuccess());

        Set<String> expectedStoresSet = new HashSet<>(allAvailableStores);
        Set<String> actualStoresSet = new HashSet<>(res.getResult());

        assertEquals(expectedStoresSet, actualStoresSet);    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void successfulUserViewTest() {
        impl.closeStore(userID1, storeID3);

        List<String> allAvailableStores = new ArrayList<>();
        allAvailableStores.add(storeID1);
        allAvailableStores.add(storeID2);

        Response<List<String>> res = impl.getInformationAboutStores(userID3);
        assertTrue(res.isSuccess());

        Set<String> expectedStoresSet = new HashSet<>(allAvailableStores);
        Set<String> actualStoresSet = new HashSet<>(res.getResult());

        assertEquals(expectedStoresSet, actualStoresSet);
    }
}
