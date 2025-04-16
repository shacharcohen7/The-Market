package AcceptanceTests.Users.Member;

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
import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OpenStore {

    @Autowired
    private BridgeToTests impl;

    static  String userId0;


    @BeforeEach
    public void setUp() {
        userId0 = impl.enterMarketSystem().getData();
        impl.register(userId0, "user1",  "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login(userId0, "user1", "fSijsd281");

    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulOpenStoreTest() {
        assertTrue(impl.openStore(userId0, "Bershka", "clothing store").isSuccess());
        assertTrue(impl.openStore(userId0, "Zara", "clothing store").isSuccess());
        assertTrue(impl.openStore(userId0, "shufersal", "Food store").isSuccess());
    }

    @Test
    public void missingStoreNameTest() {
        Response<String> response = impl.openStore(userId0, null, "clothing store");
        assertFalse(response.isSuccess());

        assertEquals(ExceptionsEnum.illegalStoreName.toString(), response.getDescription());

        Response<String> response2 = impl.openStore(userId0, "", "Electronics store");
        assertFalse(response.isSuccess());

        assertEquals(ExceptionsEnum.illegalStoreName.toString(), response2.getDescription());
    }
}
