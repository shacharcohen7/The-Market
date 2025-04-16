package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CloseStore {

    @Autowired
    private BridgeToTests impl;

    private static String userID1;
    private static String storeID1;
    private static String storeID2;

    @BeforeEach
    public void setUp() {
        userID1 = impl.enterMarketSystem().getData();
        impl.register(userID1, "newUser1", "12/12/2000", "Israel", "BeerSheva", "bialik", "noa", "Password123");
        impl.login(userID1, "newUser1", "Password123");
        storeID1 = impl.openStore(userID1, "Bershka", "clothing store").getData();
        storeID2 = impl.openStore(userID1, "Zara", "clothing store").getData();

    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulCloseTest() {
        assertTrue(impl.closeStore(userID1, storeID1).isSuccess());
        assertTrue(impl.closeStore(userID1, storeID2).isSuccess());
    }

    @Test
    public void alreadyClosedTest() {
        impl.closeStore(userID1, storeID1);

        Response<String> response = impl.closeStore(userID1, storeID1);;
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.storeNotExist.toString(), response.getDescription());
    }

}
