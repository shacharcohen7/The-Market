package AcceptanceTests.Users.Guest;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EnteringMarket {

    @Autowired
    private BridgeToTests impl;

    @BeforeEach
    public void setUp() {
        impl.resetAllTables();
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulEnterTest() {
        impl.enterMarketSystem();
        assertTrue(impl.enterMarketSystem().isSuccess());
        assertTrue(impl.enterMarketSystem().isSuccess());
        assertTrue(impl.enterMarketSystem().isSuccess());
    }
}
