package AcceptanceTests.Users.SystemManager;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import ServiceLayer.Response;
import Util.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class MarketPurchaseHistory {

    @Autowired
    private BridgeToTests impl;

    private static String userID1;
    private static String userID2;
    private static String managerID1;
    private static String storeID;
    private static PaymentDTO paymentDTO;
    private static UserDTO userDTO;
    static Map<String, Map<String,List<Integer>>> products;



    @BeforeEach
    public void setUp() throws JsonProcessingException {
        HashSet<String> countries = new HashSet<>();
        countries.add("Israel");
        HashSet<String> cities = new HashSet<>();
        cities.add("BeerSheva");
        managerID1 = impl.init().getData();


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
        Milk.add(0,2);
        Milk.add(1,20);
        List<Integer> Cheese = new ArrayList<>();
        Cheese.add(0,4);
        Cheese.add(1,60);
        List<Integer> Yogurt = new ArrayList<>();
        Yogurt.add(0,5);
        Yogurt.add(1,20);
        basketProducts.put("Milk", Milk);
        basketProducts.put("Cheese", Cheese);
        basketProducts.put("Yogurt", Yogurt);
        products.put(storeID, basketProducts);
        //Map<String, Map<String, List<Integer>>> prod

        // Initialize paymentDTO, userDTO, cartDTO
        paymentDTO = new PaymentDTO("userId","holderName", "USD","1111222233334444", 986, 12, 2025);
        userDTO = new UserDTO(userID1, "newUser1", "12/12/2000", "Israel", "BeerSheva", "bialik", "noa");
        int price = impl.checkingCartValidationBeforePurchase(userID2, userDTO.getUserName(), userDTO.getBirthday(), userDTO.getName()
                , userDTO.getCountry(),userDTO.getCity(), userDTO.getAddress()).getResult();
        CartDTO cartDTO = new CartDTO(userID2,price,products);
        impl.setUserConfirmationPurchase(userID2);
        impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                paymentDTO.getCreditCardNumber(),paymentDTO.getCurrency(),paymentDTO.getHolderName(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId(),
                cartDTO.getCartPrice(), cartDTO.getStoreToProducts()).isSuccess();
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

//    @Test
//    public void successfulRequestTest() {
//        Map<String, Integer> storeIdAndNumOfAcquistion = new HashMap<>();
//        storeIdAndNumOfAcquistion.put(storeID, 1);
//        assertEquals(impl.marketManagerAskInfo(managerID1).getResult(), storeIdAndNumOfAcquistion);
//    }

    @Test
    public void noPermissionsTest() {
        Response<Map<String, Integer>> response1 = impl.marketManagerAskInfo(userID1);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.notSystemManager.toString(), response1.getDescription());
    }
}
