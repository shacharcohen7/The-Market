package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import DomainLayer.Market.Market;
import static org.junit.jupiter.api.Assertions.*;

import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import DomainLayer.User.UserFacade;
import PresentationLayer.Application;
import Util.ExceptionsEnum;
import Util.PaymentServiceDTO;
import Util.SupplyServiceDTO;
import Util.UserDTO;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;
import java.util.HashSet;

@Transactional
@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class  SystemStartup {

    @Inject
    private Market market;

    private UserFacade userFacade;


    @BeforeEach
    public void setUp() throws Exception {
        //this.userFacade = userFacade.getInstance();
        PaymentServicesFacade paymentServicesFacade = PaymentServicesFacade.getInstance();
        SupplyServicesFacade supplyServicesFacade = SupplyServicesFacade.getInstance();
        //this.market = new Market(userFacade, paymentServicesFacade, supplyServicesFacade);
       // market = new Market();
    }

    @AfterEach
    public void tearDown() {
        market.resetAllTables();
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @Test
    public void successfulInitTest() throws Exception {
        assertFalse(market.isInitialized());
        assertEquals(0, market.getSystemManagerIds().size());
        assertEquals(0, market.getPaymentServicesFacade().getAllPaymentServices().size());
        assertEquals(0, market.getSupplyServicesFacade().getAllSupplyServices().size());

//        String licensedDealerNumber = "12345";
//        String paymentServiceName = "PayPal";
//        String url = "http://example.com";
//        String licensedDealerNumber1 = "67890";
//        String supplyServiceName = "SupplyService";
//        HashSet<String> countries = new HashSet<>(Arrays.asList("USA", "Canada"));
//        HashSet<String> cities = new HashSet<>(Arrays.asList("New York", "Los Angeles"));

        // Act
        market.init();

        // Assuming there's a method isInitialized() that returns whether the system is initialized
        assertTrue(market.isInitialized());
        assertEquals(1, market.getSystemManagerIds().size());
        String memberID = market.getSystemManagerIds().iterator().next();
        userFacade = market.getUserFacade();
        // assert the system manager details match the details in the config file
        assertEquals("u1", userFacade.getMembers().getById(memberID).getUsername());
        assertEquals("19/09/1996", userFacade.getMembers().getById(memberID).getBirthday());
        assertEquals("Israel", userFacade.getMembers().getById(memberID).getCountry());
        assertEquals("Ashdod", userFacade.getMembers().getById(memberID).getCity());
        assertEquals("Elul", userFacade.getMembers().getById(memberID).getAddress());
        assertEquals("David Volodarsky", userFacade.getMembers().getById(memberID).getName());

    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @Test
    public void noPaymentServiceTest() {
        assertFalse(market.isInitialized());


//        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.init("src/main/resources/configurationBadPayment.yaml");
        });
////
////
////        // Optionally check the exception message
        assertEquals("Payment Service cannot connect to the url", exception.getMessage());
        assertFalse(market.isInitialized());
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @Test
    public void testInitFailureNoAdmin() {
        assertFalse(market.isInitialized());

        Exception exception = assertThrows(Exception.class, () -> {
            market.init("src/main/resources/configurationBadAdmin.yaml");
        });


        // Optionally check the exception message
//        assertEquals(ExceptionsEnum.passwordInvalid.toString(), exception.getMessage());
        assertFalse(market.isInitialized());

    }
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @Test
    public void noSupply() {
        assertFalse(market.isInitialized());


//        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.init("src/main/resources/configurationBadSupply.yaml");
        });
////
////
////        // Optionally check the exception message
        //assertEquals(ExceptionsEnum.InvalidPaymentServiceDetails.toString(), exception.getMessage());
        assertFalse(market.isInitialized());
    }


}
