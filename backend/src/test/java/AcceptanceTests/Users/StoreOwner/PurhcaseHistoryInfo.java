package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.RealToTest;
import DomainLayer.Market.Market;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Repositories.InitializedRepository;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import DomainLayer.User.UserFacade;
import PresentationLayer.Application;
import Util.*;
import org.junit.jupiter.api.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PurhcaseHistoryInfo {

    @Autowired
    private BridgeToTests impl;

    @Mock
    private PaymentServicesFacade paymentServicesFacade;

    @Mock
    private InitializedRepository initializedRepository;

    @Mock
    private UserFacade userFacade;

    @Mock
    private StoreFacade storeFacade;

    @Mock
    private SupplyServicesFacade supplyServicesFacade;

    @Mock
    private RoleFacade roleFacade;

    @InjectMocks
    private Market market;

    private static String userID1;
    private static String userID2;
    private static String storeID;
    private static PaymentDTO paymentDTO;
    private static UserDTO userDTO;
    private static Map<String, Map<String, List<Integer>>> products;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Mocking the payWithExternalPaymentService method in the Market class
        //when(market.payWithExternalPaymentService(any(CartDTO.class), any(PaymentDTO.class), anyString()))
          //      .thenReturn("mockAcquisitionId");

        // Initializing market with mocked dependencies
        //market = new Market(userFacade, storeFacade, supplyServicesFacade,
        //        paymentServicesFacade, roleFacade, initializedRepository);

        HashSet<String> countries = new HashSet<>();
        countries.add("Israel");
        HashSet<String> cities = new HashSet<>();
        cities.add("BeerSheva");
        impl.init();

        userID1 = impl.enterMarketSystem().getData();
        userID2 = impl.enterMarketSystem().getData();
        impl.register(userID1, "newUser1", "12/12/2000", "Israel", "BeerSheva", "bialik", "noa", "Password123");
        impl.login(userID1, "newUser1", "Password123");

        storeID = impl.openStore(userID1, "superStore", "supermarket").getData();
        impl.addProductToStore(userID1, storeID, "Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(userID1, storeID, "Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(userID1, storeID, "Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(userID1, storeID, "Shoes", 4, 12, "Nike Shoes", "clothing");
        impl.enterMarketSystem();
        impl.addProductToBasket("Milk", 2, storeID, userID2);
        impl.addProductToBasket("Cheese", 4, storeID, userID2);
        impl.addProductToBasket("Yogurt", 5, storeID, userID2);

        products = new HashMap<>();
        Map<String, List<Integer>> basketProducts = new HashMap<>();
        List<Integer> Milk = new ArrayList<>();
        Milk.add(2);
        Milk.add(20);
        List<Integer> Cheese = new ArrayList<>();
        Cheese.add(4);
        Cheese.add(60);
        List<Integer> Yogurt = new ArrayList<>();
        Yogurt.add(5);
        Yogurt.add(20);
        basketProducts.put("Milk", Milk);
        basketProducts.put("Cheese", Cheese);
        basketProducts.put("Yogurt", Yogurt);
        products.put(storeID, basketProducts);

        // Initialize paymentDTO, userDTO and cartDTO
        paymentDTO = new PaymentDTO("holderId", "holderName", "USD", "1111222233334444", 1, 12, 2025);
        userDTO = new UserDTO(userID1, "newUser1", "12/12/2000", "Israel", "BeerSheva", "bialik", "noa");
        int price = impl.checkingCartValidationBeforePurchase(userID2, userDTO.getUserName(), userDTO.getBirthday(), userDTO.getName(),
                userDTO.getCountry(), userDTO.getCity(), userDTO.getAddress()).getResult();
        CartDTO cartDTO = new CartDTO(userID2, price, products);
        impl.setUserConfirmationPurchase(userID2);
        System.out.println(impl.purchase(userID2, userDTO.getCountry(), userDTO.getCity(), userDTO.getAddress(),
                paymentDTO.getCreditCardNumber(), paymentDTO.getCurrency(), paymentDTO.getHolderName(), paymentDTO.getCvv(), paymentDTO.getMonth(), paymentDTO.getYear(), paymentDTO.getHolderId(),
                cartDTO.getCartPrice(), cartDTO.getStoreToProducts()).isSuccess());
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulRequestTest() {
        assertEquals(impl.storeOwnerGetInfoAboutStore(userID1, storeID).getResult().size(), 1);
    }
}
