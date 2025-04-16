package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import DomainLayer.Market.Market;
import PresentationLayer.Application;
import Util.ExceptionsEnum;
import Util.PaymentServiceDTO;
import Util.UserDTO;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;


@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RemoveExternalPaymentServices {


    @Inject
    private Market market;



    @BeforeEach
    public void setUp() {

    }

    @AfterEach
    public void tearDown() {
        market.resetAllTables();
    }


    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testRemoveExternalPaymentServiceSuccess() throws Exception {
        market.getPaymentServiceFacade().addPaymentForTests( "https://damp-lynna-wsep-1984852e.koyeb.app/");
        market.getPaymentServiceFacade().addPaymentForTests( "http://stripe.com");
        String userId = market.enterMarketSystem();
        market.register(userId, new UserDTO("nadav", "nadavKt","10/10/2002","nad","vv "," vv","nasav " ), "nadavVV1");
        String memberId= market.Login(userId, "nadavKt", "nadavVV1");
        market.addSystemManagerForTest(memberId);

        // Act and Assert
        assertDoesNotThrow(() -> {
            market.removeExternalPaymentService("http://stripe.com", userId);
        });
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testRemoveExternalPaymentServiceFailureNotSystemManager() throws Exception {
        // Arrange
        String userId = market.enterMarketSystem();
        market.register(userId, new UserDTO("nadav", "nadavKt","10/10/2002","nad","vv "," vv","nasav " ), "nadavVV1");
        String memberId= market.Login(userId, "nadavKt", "nadavVV1");
        market.addSystemManagerForTest(memberId);
        market.addExternalPaymentService("https://damp-lynna-wsep-1984852e.koyeb.app/",userId);


        String userId2 = market.enterMarketSystem();
        market.register(userId2, new UserDTO("nadavkt", "nadavKt11","10/10/2002","nad","vv "," vv","nasav " ), "nadavVV1");
        String memberId2= market.Login(userId2, "nadavKt11", "nadavVV1");
        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.removeExternalPaymentService("https://damp-lynna-wsep-1984852e.koyeb.app/", userId2); // 2 is not a system manager ID
        });

        // Optionally check the exception message
        assertEquals(ExceptionsEnum.SystemManagerPaymentAuthorizationRemove.toString(), exception.getMessage());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testRemoveExternalPaymentServiceFailureOnlyOneService() throws Exception {
        String userId = market.enterMarketSystem();
        market.register(userId, new UserDTO("nadav", "nadavKt","10/10/2002","nad","vv "," vv","nasav " ), "nadavVV1");
        String memberId= market.Login(userId, "nadavKt", "nadavVV1");
        market.addSystemManagerForTest(memberId);
        market.addExternalPaymentService("https://damp-lynna-wsep-1984852e.koyeb.app/",userId);

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.removeExternalPaymentService("https://damp-lynna-wsep-1984852e.koyeb.app/", userId);
        });

        // Optionally check the exception message
        assertEquals(ExceptionsEnum.OnlyPaymentService.toString(), exception.getMessage());
    }

}
