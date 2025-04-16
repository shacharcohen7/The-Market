package AcceptanceTests.Users.Guest;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class Login {

    @Autowired
    private BridgeToTests impl;
    private static String userID1;
    private static String userID2;
    private static String userID3;

    @BeforeEach
    public void setUp() {

        userID1 = impl.enterMarketSystem().getData();
        userID2 = impl.enterMarketSystem().getData();
        userID3 = impl.enterMarketSystem().getData();
        impl.register(userID1, "user1","12/12/00","Israel", "BeerSheva", "bialik","noa", "0VnDExW3T9");
        impl.register(userID2, "user2", "12/12/00", "Israel", "BeerSheva", "bialik","noa", "QtzxeceVM0");
        impl.register(userID3, "user3", "12/12/00", "Israel", "BeerSheva", "bialik","noa", "KjUJqvJBls1");
    }

    @AfterEach
    public void shutDown() {
        impl.logout(userID1);
        impl.logout(userID2);
        impl.logout(userID3);
        impl.resetAllTables();
    }

    @Test
    public void successfulLoginTest() {
        Response<String> response1 = impl.login(userID1, "user1", "0VnDExW3T9");
        assertTrue(response1.isSuccess());
        assertEquals("Login successful", response1.getResult());

        Response<String> response2 = impl.login(userID2, "user2", "QtzxeceVM0");
        assertTrue(response2.isSuccess());
        assertEquals("Login successful", response2.getResult());

        Response<String> response3 = impl.login(userID3, "user3", "KjUJqvJBls1");
        assertTrue(response3.isSuccess());
        assertEquals("Login successful", response1.getResult());
    }

    @Test
    public void incorrectUsernameTest() {
        // Try logging in with incorrect username
        Response<String> response0 = impl.login(userID1, "wrongUser0", "0VnDExW3T9");
        assertFalse(response0.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response0.getDescription());

        Response<String> response1 = impl.login(userID2, "wrongUser0", "QtzxeceVM0");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response1.getDescription());

        Response<String> response2 = impl.login(userID3, "wrongUser0", "KjUJqvJBls1");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response2.getDescription());
    }

    @Test
    public void incorrectPasswordTest() {
        // Try logging in with incorrect password
        Response<String> response0 = impl.login(userID1, "user1", "1");
        assertFalse(response0.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response0.getDescription());

        Response<String> response1 = impl.login(userID2, "user2", "1");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response1.getDescription());

        Response<String> response2 = impl.login(userID3, "user3", "1");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response2.getDescription());
    }

    @Test
    public void alreadyLoggedInTest() {
        impl.login(userID1, "user1", "0VnDExW3T9");
        impl.login(userID2, "user2", "QtzxeceVM0");
        impl.login(userID3, "user3", "KjUJqvJBls1");

        Response<String> response1 = impl.login(userID1, "user1", "0VnDExW3T9");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.userAlreadyLoggedIn.toString(), response1.getDescription());

        Response<String> response2 = impl.login(userID2, "user2", "QtzxeceVM0");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.userAlreadyLoggedIn.toString(), response2.getDescription());

        Response<String> response3 = impl.login(userID3, "user3", "KjUJqvJBls1");
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.userAlreadyLoggedIn.toString(), response3.getDescription());
    }
}
