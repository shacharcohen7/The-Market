package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import DomainLayer.Market.Market;
import PresentationLayer.Application;
import Util.PaymentServiceDTO;
import Util.ExceptionsEnum;
import Util.UserDTO;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AddingExternalPaymentServices {

    @Inject
    private Market market;



    @BeforeEach
    public void setUp() throws Exception {

    }

    @AfterEach
    public void tearDown() {
        market.resetAllTables();
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @Test
    public void testAddExternalPaymentServiceSuccess() throws Exception {
        // Arrange
        //String systemManagerId = "u1";
        //market.getSystemManagerIds().add(systemManagerId);
        String url = "https://damp-lynna-wsep-1984852e.koyeb.app/";
        String userId = market.enterMarketSystem();
        market.register(userId, new UserDTO("nadav", "nadavKt","10/10/2002","nad","vv "," vv","nasav " ), "nadavVV1");
        String memberId= market.Login(userId, "nadavKt", "nadavVV1");
        market.addSystemManagerForTest(memberId);
        // Act and Assert
        assertDoesNotThrow(() -> {
            market.addExternalPaymentService(url, userId);
        });
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @Test
    public void testAddExternalPaymentServiceFailureNotSystemManager() throws Exception {
        // Arrange
        market.init();
        String systemManagerId = "USER1";
        String nonManagerId = "user2";
        market.addSystemManagerForTest(systemManagerId);

        String url = "https://damp-lynna-wsep-1984852e.koy111eb.app/";
        String userId = market.enterMarketSystem();
        market.register(userId, new UserDTO("nadav", "nadavKt","10/10/2002","nad","vv "," vv","nasav " ), "nadavVV1");
        market.Login(userId,"nadavKt" ,"nadavVV1" );


        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalPaymentService(url, userId);
        });

        // Optionally check the exception message
        assertEquals(ExceptionsEnum.SystemManagerPaymentAuthorization.toString(), exception.getMessage());
    }


    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @Test
    public void testAddExternalPaymentServiceFailureInvalidDetails() throws Exception {
        // Arrange
        market.init();
        String systemManagerId = "user1";
        market.addSystemManagerForTest(systemManagerId);
      //  String licensedDealerNumber = "-1"; // Invalid dealer number
       // String paymentServiceName = null; // Invalid payment service name
        String url = "httpds://damp/"; // Invalid URL
        String userId = market.enterMarketSystem();
        market.Login(userId, "u1", "adminPassword1");
        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalPaymentService( url, userId);
        });

        // Optionally check the exception message
        assertEquals("Payment Service cannot connect to the url", exception.getMessage());
    }

}

