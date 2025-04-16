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
public class RemoveStoreProduct {

    @Autowired
    private BridgeToTests impl;

    static String saarUserID;
    static String storeID;

    @BeforeEach
    public void setUp() {
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        impl.login(saarUserID, "saar", "Fadidaa1");
        storeID= impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.addProductToStore(saarUserID, storeID,"weddingDress", 10, 5, "pink", "CLOTHING");
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulRemoveTest() {
        assertTrue(impl.removeProductFromStore(saarUserID, storeID,"weddingDress").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        Response<String> response = impl.removeProductFromStore(saarUserID, storeID,"skirt");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response.getDescription());
    }
}
