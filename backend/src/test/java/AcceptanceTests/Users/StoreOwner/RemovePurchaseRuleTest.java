package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import Util.ExceptionsEnum;
import Util.TestRuleDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RemovePurchaseRuleTest {

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
        impl.addProductToStore(saarUserID, storeId,"weddingDress", 10, 5, "pink", "clothes");

        TestRuleDTO rule1 = new TestRuleDTO("Age", "Below", "ALCOHOL", null, "Alcohol cannot be sold to users below the age of 18", false, 18, null, null, null, null);
        TestRuleDTO rule2 = new TestRuleDTO("Time", "Above", "ALCOHOL", null, "Alcohol cannot be sold after 23:00", false, null, null, null, null, LocalTime.of(23, 0));
        List<TestRuleDTO> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        impl.addPurchaseRuleToStore(rules, List.of("AND"), saarUserID, storeId);

        TestRuleDTO rule3 = new TestRuleDTO("Amount", "Below", null, "Tomato", "Basket must contain less than 5kg of tomatoes", true, null, 5, null, null, null);
        List<TestRuleDTO> singleRule = new ArrayList<>();
        singleRule.add(rule3);
        impl.addPurchaseRuleToStore(singleRule, new ArrayList<>(), saarUserID, storeId);
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulRemoveTest() {
        //remove rule 1
        assertTrue(impl.removePurchaseRuleFromStore(0, saarUserID, storeId).isSuccess());
        //remove rule 2
        assertTrue(impl.removePurchaseRuleFromStore(0, saarUserID, storeId).isSuccess());
    }

    @Test
    public void ruleNumDontExist() {
        assertFalse(impl.removePurchaseRuleFromStore(100, saarUserID, storeId).isSuccess());
        assertEquals(impl.removePurchaseRuleFromStore(100, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidRuleIndex.toString());
    }
}
