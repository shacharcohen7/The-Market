package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import ServiceLayer.Response;
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
public class UpdateStoreProduct {

    @Autowired
    private BridgeToTests impl;

    static String saarUserID;
    static String storeID;

    @BeforeEach
    public void setUp() {
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        impl.login(saarUserID, "saar", "Fadidaa1");
        storeID = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.addProductToStore(saarUserID, storeID,"weddingDress", 10, 5, "pink", "CLOTHING");
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulUpdateTest() {
        assertTrue(impl.updateProductInStore(saarUserID,storeID,"weddingDress", 11, 4,
                                                            "pink", "CLOTHING").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        Response<String> response = impl.updateProductInStore(saarUserID,storeID,"heels", 1, 41,
                "black", "shoes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response.getDescription());
    }

    @Test
    public void negQuantityTest() {
        Response<String> response = impl.updateProductInStore(saarUserID,storeID,"weddingDress", 11, -4,
                "pink", "CLOTHING");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response.getDescription());
    }
}
