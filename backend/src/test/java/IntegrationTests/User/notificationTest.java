package IntegrationTests.User;


import AcceptanceTests.RealToTest;
import DomainLayer.Market.Market;
import DomainLayer.Notifications.LateNotificationFacade;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Role.StoreManager;
import DomainLayer.Role.StoreOwner;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import PresentationLayer.Application;
import PresentationLayer.Vaadin.MyWebSocketHandler;
import Util.CartDTO;
import Util.UserDTO;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ExtendWith(MockitoExtension.class)
public class notificationTest {
    @Inject
    Market market;

        //private NotificationService notificationService;
        private MyWebSocketHandler myWebSocketHandler;
        private RoleFacade roleFacade;
        private StoreFacade storeFacade;
        private UserFacade userFacade;
        private LateNotificationFacade lateNotificationFacade;

        @BeforeEach
        public void setUp() throws Exception {
            myWebSocketHandler = market.getMyWebSocketHandler();
            roleFacade = market.getRoleFacade();
            storeFacade = market.getStoreFacade();
            userFacade = market.getUserFacade();
            lateNotificationFacade = new LateNotificationFacade();

            // Injecting real instances
            //notificationService = new NotificationService(vaadinUserService, myWebSocketHandler, roleFacade, storeFacade, userFacade, lateNotificationFacade);

            // Set up the roles for the test

            roleFacade.addNewStoreOwnerToTheMarketForTests(new StoreOwner( "owner1","store1", true," " ,false));
            roleFacade.addNewStoreOwnerToTheMarketForTests(new StoreOwner("owner2","store1",  true," " ,false));
            roleFacade.addNewStoreManagerToTheMarketForTests(new StoreManager( "manager1","store1", false, true," " ,false));
        }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testSendMessagesToOwnersAndManagers() throws Exception {
        String storeId = "store1";
        String message = "Test Message";

        // Call the method under test
        market.sendMessagesToOwnersAndManagers(storeId, message);

        // Verify WebSocketHandler messages
        List<String> owner1Messages = myWebSocketHandler.getUserNotifications("owner1");
        List<String> owner2Messages = myWebSocketHandler.getUserNotifications("owner2");
        List<String> manager1Messages = myWebSocketHandler.getUserNotifications("manager1");

        assertTrue(owner1Messages.get(0).contains(message));
        assertTrue(owner2Messages.get(0).contains(message));
        assertTrue(manager1Messages.get(0).contains(message));

    }


    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testSendMessagesOnPurchaseToStoreOwners() throws Exception {
//        CartDTO cartDTO = new CartDTO();
//        // Populate cartDTO with relevant data
        String userId = market.enterMarketSystem();
        market.register(userId, new UserDTO(userId, "dsfs", "12/12/2022", "dsf", "dfsf", "dsfsdf","dsfsf"), "Pdsf32424ff2");
        market.Login(userId,"dsfs","Pdsf32424ff2" );
        String storeId = market.openStore(userId, "store1", "store");
        String message = "Test Message";
        roleFacade.addNewStoreOwnerToTheMarketForTests(new StoreOwner( "owner12",storeId, true," " ,false));
        roleFacade.addNewStoreOwnerToTheMarketForTests(new StoreOwner("owner22",storeId,  true," " ,false));
        roleFacade.addNewStoreManagerToTheMarketForTests(new StoreManager( "manager12",storeId, false, true," " ,false));

        Map<String, Map<String, List<Integer>>> storeToProducts = new HashMap<>();
        Map<String , List<Integer>> store1 = new HashMap<>();
        List<Integer> prices = new ArrayList<>();
        prices.add(1);
        prices.add(100);
        List<Integer> prices2 = new ArrayList<>();
        prices.add(1);
        prices.add(100);
        store1.put("procuct1", prices);
        store1.put("procuct2", prices2);
        storeToProducts.put(storeId,store1);
//        cartDTO.getStoreToProducts().put();
//        cartDTO.addStoreToProduct("store1", "product2");

        // Call the method under test
        market.sendMessagesOnPurchaseToStoreOwners(new CartDTO("nadav", 100, storeToProducts));



        // Verify WebSocketHandler messages
        List<String> owner1Messages = myWebSocketHandler.getUserNotifications("owner12");
        List<String> owner2Messages = myWebSocketHandler.getUserNotifications("owner22");

        String storeName = "store1";
        String expectedMessage = "A purchase was made from your store - " + storeName;

        assertTrue(owner1Messages.get(0).contains(expectedMessage));
        assertTrue(owner2Messages.get(0).contains(expectedMessage));
    }


}

