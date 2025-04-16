package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import DomainLayer.Market.Market;
import DomainLayer.PaymentServices.Acquisition;
import DomainLayer.PaymentServices.ExternalPaymentService;
import DomainLayer.PaymentServices.HttpClient;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.User;
import DomainLayer.User.UserFacade;
import PresentationLayer.Application;
import Util.*;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.stubbing.answers.ThrowsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class Payment {

    @Autowired
    private Market market;

    @Mock
    private PaymentServicesFacade paymentServicesFacade;

    @Mock
    private UserFacade userFacade;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        this.paymentServicesFacade = market.getPaymentServiceFacade();
        this.userFacade = market.getUserFacade();
        //this.market = new Market(userFacade,paymentServicesFacade);
    }

    @AfterEach
    public void tearDown() {
        market.resetAllTables();
    }


    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testPayWithExternalPaymentService_Success() throws Exception {
        market.init();
        CartDTO cartDTO = Mockito.mock(CartDTO.class);
        // Stub the methods as needed
        Mockito.when(cartDTO.getUserID()).thenReturn("77");
        Mockito.when(cartDTO.getCartPrice()).thenReturn(100);
        int price = 100;
        String cardNumber = "1111222233334444";
        int cvv = 982;
        int month = 12;
        int year = 2024;
        String holderID = "123456789";
        String newUserId = userFacade.addUser();
        UserDTO userDTO = new UserDTO(newUserId, "username", "19/09/1996", "country",  "city", "address",  "name");
        userFacade.register(newUserId, userDTO , "password");

        String url = "https://damp-lynna-wsep-1984852e.koyeb.app/";
        //  int systemMangerId1 = userFacade.registerSystemAdmin("david", "password", "birthday","country","city","address","name");

        ExternalPaymentService externalPaymentService = paymentServicesFacade.getAllPaymentServices().get(url);

        int result = paymentServicesFacade.getIdAndAcquisition().size();
        assertEquals(0, result);


        // Act and Assert
        assertDoesNotThrow(() -> {paymentServicesFacade.pay(cartDTO.getCartPrice(), new PaymentDTO(holderID,"nadav", "USD",cardNumber, cvv, month, year), cartDTO.getUserID(),null, cartDTO.getStoreToProducts());
        });

        String res =paymentServicesFacade.pay(cartDTO.getCartPrice(), new PaymentDTO(holderID,"nadav" ,"USD",cardNumber, cvv, month, year), cartDTO.getUserID(), null, cartDTO.getStoreToProducts());
        //int res1 = Integer.valueOf(res);
        Acquisition acquisition = paymentServicesFacade.getAcquisitionById(res);
        assertNotNull(acquisition);
        assertTrue(acquisition.getTransactionId()>=10000);
        assertTrue(acquisition.getTransactionId()<=100000);

        int result1 = paymentServicesFacade.getIdAndAcquisition().size();
        assertEquals(2, result1);

    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void invalidPaymentServiceTest() throws Exception {
        if(!market.isInitialized()){
            market.init();
        }
        int price = 100;
        String cardNumber = "12345678";
        int cvv = 123;
        int month = 12;
        int year = 1997; // Invalid year
        String holderID = "123456789";
        String userID = "77";
        String url = "https://damp-lynna-wsep-1984852e.koyeb.app/";


        ExternalPaymentService externalPaymentService = paymentServicesFacade.getAllPaymentServices().get(url);
        int result = paymentServicesFacade.getIdAndAcquisition().size();
        assertEquals(0, result);
        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
//

//
        Exception exception = assertThrows(Exception.class, () -> {
            market.payWithExternalPaymentService(new CartDTO(userID,price,productList), new PaymentDTO("d",holderID,"USD", cardNumber, cvv, month, year), userID);
        });
        int result1 = paymentServicesFacade.getIdAndAcquisition().size();
        assertEquals(0, result1);
        assertEquals( ExceptionsEnum.InvalidCreditCardParameters.toString(), exception.getMessage());

    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void noAvailableExternalPaymentServiceTest() {

        int price = 100;
        String cardNumber = "1111222233334444";
        int cvv = 996;
        int month = 12;
        int year = 20244;
        String holderID = "123456789";
        String userID = "77";

        String url = "http://paypal.com";
        assertEquals(0, paymentServicesFacade.getAllPaymentServices().size()); // assert there is no AvailableExternalPaymentServiceTest
        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
//

        Exception exception = assertThrows(Exception.class, () -> {
            market.payWithExternalPaymentService(new CartDTO(userID,price,productList),new PaymentDTO("D",holderID,"USD", cardNumber, cvv, month, year), userID);
        });

        assertEquals(ExceptionsEnum.noAvailableExternalPaymentService.toString(), exception.getMessage());


    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void CreditCardNotPassingTest() throws Exception {

        CartDTO cartDTO = Mockito.mock(CartDTO.class);
        // Stub the methods as needed
        Mockito.when(cartDTO.getUserID()).thenReturn("77");
        Mockito.when(cartDTO.getCartPrice()).thenReturn(100);
        int price = 100;
        String cardNumber = "1111222233334444";
        int cvv = 986;
        int month = 12;
        int year = 2024;
        String holderID = "123456789";
        //String userID = "77";
//        String newUserId = userFacade.addUser();
//        UserDTO userDTO = new UserDTO(newUserId, "username", "19/09/1996", "country",  "city", "address",  "name");
//        userFacade.register(newUserId, userDTO , "password");
        String newUserId = userFacade.addUser();
        UserDTO userDTO = new UserDTO(newUserId, "username", "19/09/1996", "country",  "city", "address",  "name");
        userFacade.register(newUserId,  userDTO, "password");

        String systemMangerId = "77";

        market.addSystemManagerForTest(systemMangerId);
        String url = "https://damp-lynna-wsep-1984852e.koyeb.app/";
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.checkCreditCard(Mockito.eq(url), Mockito.any(PaymentDTO.class))).thenReturn(false);

        paymentServicesFacade.addExternalService(url);
        ExternalPaymentService externalPaymentService = paymentServicesFacade.getAllPaymentServices().get(url);

        int result = paymentServicesFacade.getIdAndAcquisition().size();
        assertEquals(0, result);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                paymentServicesFacade.pay(cartDTO.getCartPrice(), new PaymentDTO(holderID,"name", "USD", cardNumber, cvv, month, year), cartDTO.getUserID(),null, cartDTO.getStoreToProducts()));

        assertEquals(ExceptionsEnum.ExternalPaymentFailed.toString(), exception.getMessage());
    }
        //int res1 = Integer.valueOf(res)




}
