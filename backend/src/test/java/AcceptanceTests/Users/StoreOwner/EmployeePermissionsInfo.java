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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EmployeePermissionsInfo {


    @Autowired
    private BridgeToTests impl;

    private static String userID1;
    private static String userID2;
    private static String userID3;
    private static String userID4;
    private static String storeID;
    private static String memberID1;
    private static String memberID2;
    private static String memberID3;
    private static String memberID4;

    @BeforeEach
    public void setUp() {
        userID1 = impl.enterMarketSystem().getData();
        userID2 = impl.enterMarketSystem().getData();
        userID3 = impl.enterMarketSystem().getData();
        userID4 = impl.enterMarketSystem().getData();
        memberID1 = impl.register(userID1,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1").getData();
        memberID2 = impl.register(userID2,"tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "Shlaifer2").getData();
        memberID3 = impl.register(userID3,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "Kasoomm3").getData();
        memberID4 = impl.register(userID4,"ovad", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad", "Haviaaa4").getData();
        impl.login(userID1, "saar", "Fadidaa1");
        impl.login(userID2, "tom", "Shlaifer2");
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulRequestTest() {
        storeID = impl.openStore(userID1, "Zara", "clothing store").getData();
        impl.appointStoreOwner(userID1, "tom", storeID);
        impl.appointStoreManager(userID1, "jalal", storeID, true, false);
        impl.appointStoreManager(userID1, "ovad", storeID, true, false);

        assertTrue(impl.getAuthorizationsOfManagersInStore(userID1, storeID).isSuccess());
    }

    @Test
    public void storeNotExistTest() {
        String storeIdNotExist = "storeIdNotExist";
        Response<Map<String, List<Integer>>> response = impl.getAuthorizationsOfManagersInStore(userID1, storeIdNotExist);
        assertFalse(response.isSuccess());

        assertEquals(ExceptionsEnum.storeNotExist.toString(), response.getDescription());
    }

    @Test
    public void userIsNotStoreOwnerTest() {
        storeID = impl.openStore(userID1, "Zara", "clothing store").getData();
        impl.appointStoreManager(userID1, "jalal", storeID, true, false);

        Response<Map<String,String>> response = impl.getInformationAboutRolesInStore(userID2, storeID);
        assertFalse(response.isSuccess());

        assertEquals(ExceptionsEnum.userIsNotStoreOwner.toString(), response.getDescription());
    }
}
