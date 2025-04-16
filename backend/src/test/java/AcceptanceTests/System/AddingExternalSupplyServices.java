package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import DomainLayer.Market.Market;
import PresentationLayer.Application;
import Util.SupplyServiceDTO;
import Util.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Util.ExceptionsEnum;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;


import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AddingExternalSupplyServices {

    @Autowired
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
    public void testAddExternalSupplyServiceSuccess() throws Exception {
        // Arrange
        String userId = market.enterMarketSystem();
        market.register(userId, new UserDTO("nadav", "nadavKt","10/10/2002","nad","vv "," vv","nasav " ), "nadavVV1");
        String memberId= market.Login(userId, "nadavKt", "nadavVV1");
        String url = "https://damp-lynna-wsep-1984852e.koyeb.app/";
        //String systemManagerId = ;
        market.addSystemManagerForTest(memberId);
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");
        // Act and Assert
        assertDoesNotThrow(() -> {
            market.addExternalSupplyService(url, userId);
        });
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testAddExternalSupplyServiceFailureNotSystemManager() throws Exception {
        // Arrange

        String userId = market.enterMarketSystem();
        market.register(userId, new UserDTO("nadav", "nadavKt","10/10/2002","nad","vv "," vv","nasav " ), "nadavVV1");
        String memberId= market.Login(userId, "nadavKt", "nadavVV1");
//        String url = ".com";
//        String systemManagerId = "user77";
//        String nonManagerId = "user2";
        //market.getSystemManagerIds().add(systemManagerId);
        //market.getSystemManagerIds().add(systemManagerId);
        String url = "https://damp-lynna-wsep-1984852e.koyeb.app/";
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalSupplyService(url, userId);
        });

        //  check the exception message
        assertEquals(ExceptionsEnum.SystemManagerSupplyAuthorization.toString(), exception.getMessage());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testAddExternalSupplyServiceFailureInvalidDetails() throws Exception {
        // Arrange
        String url = "https://nadav.com";
        String userId = market.enterMarketSystem();
        market.register(userId, new UserDTO("nadav", "nadavKt","10/10/2002","nad","vv "," vv","nasav " ), "nadavVV1");
        String memberId= market.Login(userId, "nadavKt", "nadavVV1");
        //market.getSystemManagerIds().add(systemManagerId);
        market.addSystemManagerForTest(memberId);
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalSupplyService(url, userId);
        });

        //  check the exception message
        assertEquals(ExceptionsEnum.InvalidSupplyServiceParameters.toString(), exception.getMessage());
    }


}
