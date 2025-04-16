package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CompletingPayment {
    private static BridgeToTests impl;

//todo think if needed

//     @BeforeAll
//     public static void setUp() {
//         impl = new ProxyToTest("Real");
//         HashSet<String> countries = new HashSet<>();
//         countries.add("Israel");
//         HashSet<String> cities = new HashSet<>();
//         cities.add("Beer Sheva");
//         impl.enterMarketSystem();
//         impl.init("KobiM", "123", "27/4/95", "Israel", "Beer Sheva", "Mesada", "kobi Menashe", 1, "payementService", "kobi@gmail.com", 2, "supplyService", countries, cities);
//         impl.enterMarketSystem();
//         impl.register("1", "user1",  "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
//         impl.login("1", "user1", "fSijsd281");
//         impl.openStore("1", "super-li", "food store");
//         impl.addProductToStore("1", "0","Milk", 10, 5, "Milk 5%", "food");
//         impl.addProductToStore("1", "0","Cheese", 15, 8, "Cheese 22%", "food");
//         impl.addProductToStore("1", "0","Yogurt", 4, 12, "Yogurt 20%", "food");
//         impl.addProductToStore("1", "0","Shoes", 4, 12, "Nike Shoes", "clothing");
//         impl.enterMarketSystem();
//         impl.addProductToBasket("Milk", 2, "0", "2");
//         impl.addProductToBasket("Cheese", 4, "0", "2");
//         impl.addProductToBasket("Yogurt", 5, "0", "2");

//     }

//     @Test
//     public void successfulPurchaseTest() {
//         // Mock payment info
//         int price = 100;
//         String creditCard = "1234567890123456";
//         int cvv = 123;
//         int month = 12;
//         int year = 2024;
//         String holderID = "123456789";
//         String userId = "2";

//         // Complete payment
//         assertTrue(impl.payWithExternalPaymentService(price, creditCard, cvv, month, year, holderID, userId).isSuccess());
//     }

//     @Test
//     public void paymentFailureTest() {
//         // Mock invalid payment info
//         int price = 100;
//         String creditCard = "1234567890123456";
//         int cvv = 123;
//         int month = 13; // Invalid month
//         int year = 2024;
//         String holderID = "123456789";
//         String userId = "2";

//         // Complete payment
//         assertFalse(impl.payWithExternalPaymentService(price, creditCard, cvv, month, year, holderID, userId).isSuccess());
//     }
}
