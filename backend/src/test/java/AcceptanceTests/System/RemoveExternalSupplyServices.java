package AcceptanceTests.System;
import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import DomainLayer.Market.Market;
import DomainLayer.PaymentServices.ExternalPaymentService;
import PresentationLayer.Application;
import Util.ExceptionsEnum;
import Util.SupplyServiceDTO;
import Util.UserDTO;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RemoveExternalSupplyServices {

    @Inject
    private Market market;

    private String userId;



    @BeforeEach
    public void setUp() throws Exception {
        //this.market = Market.getInstance();
        userId = market.enterMarketSystem();
        market.register(userId, new UserDTO("nadav", "nadavKt","10/10/2002","nad","vv "," vv","nasav " ), "nadavVV1");
        String memberId= market.Login(userId, "nadavKt", "nadavVV1");
        market.addSystemManagerForTest(memberId);

    }

    @AfterEach
    public void tearDown() {
        market.resetAllTables();
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @Test
    public void testRemoveExternalPaymentServiceSuccess() throws Exception {
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");


        market.getSupplyServicesFacade().addSupplyForTests("https://supply1.com");
        market.getSupplyServicesFacade().addSupplyForTests("https://supply2.com");
        //market.addExternalSupplyService("supply1.com", "77");

        // Act and Assert
        assertDoesNotThrow(() -> {
            market.removeExternalSupplyService("https://supply1.com", userId);
        });
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testRemoveExternalSupplyServiceFailureNotSystemManager() throws Exception {
        // Arrange
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");
        market.addExternalSupplyService("https://damp-lynna-wsep-1984852e.koyeb.app/",userId);

        String userId2 = market.enterMarketSystem();
        market.register(userId2, new UserDTO(userId2, "nadavKtn","10/10/2002","nad","vv "," vv","nasav " ), "nadavVV1");
        String memberId= market.Login(userId2, "nadavKtn", "nadavVV1");

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.removeExternalSupplyService("supply.com", userId2); // 2 is not a system manager ID
        });

        // Optionally check the exception message
        assertEquals(ExceptionsEnum.SystemManagerSupplyAuthorizationRemove.toString(), exception.getMessage());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testRemoveExternalPaymentServiceFailureOnlyOneService() throws Exception {
        // Arrange
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");


        //market.getPaymentServiceFacade().getAllPaymentServices().put("supply1.com", new ExternalPaymentService("supply1.com"));
        market.addExternalPaymentService("https://damp-lynna-wsep-1984852e.koyeb.app/",userId);
        //market.addExternalSupplyService("supply.com","77");
//        market.removeExternalSupplyService("supply1.com", userId);
        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.removeExternalSupplyService("https://damp-lynna-wsep-1984852e.koyeb.app/", userId);
        });

        // Optionally check the exception message
        assertEquals(ExceptionsEnum.OnlySupplyService.toString(), exception.getMessage());
    }

}