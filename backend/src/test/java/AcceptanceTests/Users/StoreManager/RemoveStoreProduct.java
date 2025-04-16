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
public class RemoveStoreProduct {

    @Autowired
    private BridgeToTests impl;

    static String saarUserID;
    static String tomUserID;
    static String storeID;

    @BeforeEach
    public void setUp() {
        saarUserID = impl.enterMarketSystem().getData();
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID, "saar",  "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        impl.register(tomUserID, "tom",  "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "Shlaifer2");
        impl.login(saarUserID, "saar", "Fadidaa1");
        impl.login(tomUserID, "tom", "Shlaifer2");
        storeID = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.appointStoreManager(saarUserID, "tom", storeID, true, false);
        impl.answerJobProposal(tomUserID,storeID,true, true);
        impl.addProductToStore(saarUserID, storeID, "weddingDress", 10, 5, "pink", "CLOTHING");
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulRemoveTest() {
        assertTrue(impl.removeProductFromStore(tomUserID,storeID,"weddingDress").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        Response<String> response = impl.removeProductFromStore(tomUserID,storeID,"skirt");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response.getDescription());
    }

    @Test
    public void noPermissionTest() {
        impl.updateStoreManagerPermissions(saarUserID,"tom",storeID,false,false);
        Response<String> response = impl.removeProductFromStore(tomUserID,storeID,"weddingDress");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.noInventoryPermissions.toString(), response.getDescription());
    }
}
