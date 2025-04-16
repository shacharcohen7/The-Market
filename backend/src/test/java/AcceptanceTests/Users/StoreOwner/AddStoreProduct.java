package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.ProductDTO;
import Util.UserDTO;
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
    static String storeId;

    @BeforeEach
    public void setUp() {
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        impl.login(saarUserID, "saar", "Fadidaa1");
        storeId = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.addProductToStore(saarUserID, storeId,"weddingDress", 10, 5, "pink", "CLOTHING");
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulAdditionTest() {
        assertTrue(impl.addProductToStore(saarUserID,storeId,"heels", 4, 2, "black", "CLOTHING").isSuccess());
        assertTrue(impl.addProductToStore(saarUserID,storeId,"skirt", 3, 6, "purple", "CLOTHING").isSuccess());
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
}
