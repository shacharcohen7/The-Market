package AcceptanceTests.Users.Member;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import Util.UserDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class Logout {

    @Autowired
    private BridgeToTests impl;
    static String  userId0;
    static String  userId1;
    static String  userId2;


    @BeforeEach
    public void setUp() {
        //Do what you need
        userId0 = impl.enterMarketSystem().getData();
        impl.register(userId0, "user1", "12/12/00","Israel", "BeerSheva", "bialik","noa", "0VnDExW3T9");
        userId1 = impl.enterMarketSystem().getData();

        impl.register(userId1, "user2", "12/12/00", "Israel", "BeerSheva", "bialik","noa", "QtzxeceVM0");
        userId2 = impl.enterMarketSystem().getData();
        impl.register(userId2, "user3",  "12/12/00", "Israel", "BeerSheva", "bialik","noa", "KjUJqvJBls");

        impl.login(userId0, "user1", "0VnDExW3T9");


    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }



    @Test
    public void successfulLogoutTest() {
        assertTrue(impl.logout(userId0).isSuccess());


    }

    @Test
    public void alreadyLoggedOutTest() {

        assertFalse(impl.logout(userId1).isSuccess());
    }

}
