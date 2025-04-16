package AcceptanceTests.Users.StoreManager;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import ServiceLayer.Response;
import Util.ProductDTO;
import Util.UserDTO;
import Util.ExceptionsEnum;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AddStoreProduct {

    @Autowired
    private BridgeToTests impl;

    static String saarUserID;
    static String tomUserID;
    static String storeId;


    @BeforeEach
    public void setUp() {
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar",  "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(tomUserID,"tom",  "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "Shlaifer2");
        impl.login(saarUserID, "saar", "Fadidaa1");
        impl.login(tomUserID, "tom", "Shlaifer2");
        storeId  = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.appointStoreManager(saarUserID, "tom", storeId, true, false);
        impl.addProductToStore(saarUserID, storeId,"weddingDress", 10, 5, "pink", "CLOTHING");
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulAdditionTest() {
        assertTrue(impl.addProductToStore(saarUserID,storeId,"heels", 4, 2, "black", "CLOTHING").isSuccess());
    }

    @Test
    public void alreadyExistTest() {
        Response<String> response = impl.addProductToStore(saarUserID,storeId,"weddingDress", 3, 6, "pink", "CLOTHING");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productAlreadyExistInStore.toString(), response.getDescription());
    }

    @Test
    public void negQuantityTest() {
        Response<String> response = impl.addProductToStore(saarUserID,storeId,"shirt", 5, -4, "green", "CLOTHING");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response.getDescription());
    }

    @Test
    public void noPermissionTest() {
        impl.updateStoreManagerPermissions(saarUserID,"tom",storeId,false,false);
        Response<String> response = impl.addProductToStore(tomUserID,storeId,"heels", 3, 3, "black", "CLOTHING");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.noInventoryPermissions.toString(), response.getDescription());
    }
}
