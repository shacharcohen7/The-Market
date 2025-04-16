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

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UpdateManagerPermissions {

    @Autowired
    private BridgeToTests impl;

    static String saarUserID;
    static String tomUserID;
    static String jalalUserID;
    static String ovadUserID;
    static String raniUserID;
    static String storeIDsaar;
    static String storeIDjalal;

    @BeforeEach
    public void setUp() {
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(tomUserID,"tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "Shlaifer2");
        jalalUserID = impl.enterMarketSystem().getData();
        impl.register(jalalUserID,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "Kasoomm3");
        ovadUserID = impl.enterMarketSystem().getData();
        impl.register(ovadUserID,"ovad", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad", "Haviaaa4");
        raniUserID = impl.enterMarketSystem().getData();
        impl.register(raniUserID,"rani", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 12", "rani", "Zeliggg5");
        impl.login(tomUserID, "tom", "Shlaifer2");
        impl.login(saarUserID, "saar", "Fadidaa1");
        storeIDsaar = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.appointStoreManager(saarUserID, "tom", storeIDsaar, true, false);
        impl.answerJobProposal(tomUserID, storeIDsaar, true , true);
        impl.login(jalalUserID, "jalal", "Kasoomm3");
        storeIDjalal = impl.openStore(jalalUserID, "alona2", "shopping2").getData();

        Response<String> response = impl.appointStoreManager(jalalUserID, "ovad", storeIDjalal, true, true);
        impl.login(ovadUserID,"ovad", "Haviaaa4");
        impl.answerJobProposal(ovadUserID, storeIDjalal, true,true);
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void successfulUpdateTest() {
        assertTrue(impl.updateStoreManagerPermissions(saarUserID, "tom",storeIDsaar,
                true, true).isSuccess());
        assertTrue(impl.updateStoreManagerPermissions(jalalUserID, "ovad",storeIDjalal,
                true, false).isSuccess());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void notManagerTest() {
        Response<String> response1 = impl.updateStoreManagerPermissions(saarUserID, "rani",storeIDsaar,
                true, false);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.notManager.toString(), response1.getDescription());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void notNominatorTest() {
        impl.appointStoreOwner(jalalUserID, "saar",storeIDjalal);
        impl.answerJobProposal(saarUserID, storeIDjalal, false, true);
        Response<String> response1 = impl.updateStoreManagerPermissions(saarUserID, "ovad",storeIDjalal,
                true, false);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.notNominatorOfThisEmployee.toString(), response1.getDescription());

        impl.appointStoreOwner(saarUserID, "jalal", storeIDsaar);
        impl.answerJobProposal(jalalUserID, storeIDsaar, false, true);
        Response<String> response2 = impl.updateStoreManagerPermissions(jalalUserID ,"tom",storeIDsaar,
                true, true);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.notNominatorOfThisEmployee.toString(), response2.getDescription());

    }
}
