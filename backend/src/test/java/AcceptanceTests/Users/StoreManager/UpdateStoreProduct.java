package AcceptanceTests.Users.StoreManager;

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
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UpdateStoreProduct {

    @Autowired
    private BridgeToTests impl;

    static String saarUserID;
    static String tomUserID;
    static String storeId;

    @BeforeEach
    public void setUp() {
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(tomUserID,"tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "Shlaifer2");
        impl.login(saarUserID, "saar", "Fadidaa1");
        impl.login(tomUserID, "tom", "Shlaifer2");
        storeId = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.appointStoreManager(saarUserID, "tom", storeId, true, false);
        impl.answerJobProposal(tomUserID, storeId, true, true);
        impl.addProductToStore(saarUserID, storeId,"weddingDress", 10, 5, "pink", "CLOTHING");
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void successfulUpdateTest() {
        assertTrue(impl.updateProductInStore(tomUserID,storeId,"weddingDress", 11, 4,
                                                            "pink", "CLOTHING").isSuccess());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void productNotExistTest() {
        Response<String> response = impl.updateProductInStore(tomUserID,storeId,"heels", 1, 41,
                "black", "CLOTHING");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response.getDescription());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void negQuantityTest() {
        Response<String> response = impl.updateProductInStore(tomUserID,storeId,"weddingDress", 11, -4,
                "pink", "CLOTHING");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response.getDescription());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void noPermissionTest() {
        impl.updateStoreManagerPermissions(saarUserID,"tom",storeId,false,false);
        Response<String> response = impl.updateProductInStore(tomUserID,storeId,"heels", 14, 46,
                "black", "shoes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.noInventoryPermissions.toString(), response.getDescription());
    }
}
