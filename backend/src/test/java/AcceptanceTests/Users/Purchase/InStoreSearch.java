package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class InStoreSearch {

    @Autowired
    private BridgeToTests impl;

    private static String userID1;
    private static String storeID1;


    @BeforeEach
    public void setUp() {
        userID1 = impl.enterMarketSystem().getData();
        impl.register(userID1,"user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login(userID1, "user1", "fSijsd281");
        storeID1 = impl.openStore(userID1, "Bershka", "clothing store").getData();
        impl.addProductToStore(userID1, storeID1,"Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(userID1, storeID1,"Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(userID1, storeID1,"Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(userID1, storeID1,"Shoes", 4, 12, "Nike Shoes", "clothing");
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    public void successfulSearchNoFiltersTest() {
        List<String> productNames = new ArrayList<>();
        productNames.add("Milk");
        productNames.add("Cheese");
        productNames.add("Yogurt");
        productNames.add("Shoes");
        Set<String> productsSet = new HashSet<String>(productNames);
        Response<List<String>> res = impl.inStoreProductSearch(userID1, null, null, null, storeID1);
        assertTrue(res.isSuccess());
        List<String> unFilteredProducts = res.getResult();
        Set<String> filteredProductsSet = new HashSet<String>(unFilteredProducts);
        assertIterableEquals(filteredProductsSet, productsSet);
    }

    @Test
    public void successfulSearchWithFiltersTest() {
        List<String> diaryProducts = new ArrayList<>();

        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        Set<String> dairySet = new HashSet<String>(diaryProducts);

        assertTrue(impl.inStoreProductSearch(userID1, null, "FOOD", null, storeID1).isSuccess());
        List<String> filteredProducts = impl.inStoreProductSearch(userID1, null, "FOOD", null, storeID1).getResult();
        Set<String> filteredProductsSet = new HashSet<String>(filteredProducts);
        assertIterableEquals(filteredProductsSet, dairySet);

        List<String> shoes = new ArrayList<>();
        shoes.add("Shoes");
        assertTrue(impl.inStoreProductSearch(userID1, "Shoes", null, null, storeID1).isSuccess());
        assertIterableEquals(impl.inStoreProductSearch(userID1, "Shoes", null, null, storeID1).getResult(), shoes);
    }

    @Test
    public void productNotExistTest() {

        //test asset true, but returns an empty list
        Response<List<String>> response1 = impl.inStoreProductSearch(userID1, "Tomato", null, null, storeID1);
        assertTrue(response1.isSuccess());
        assertTrue(response1.getResult().isEmpty());

        Response<List<String>> response2 = impl.inStoreProductSearch(userID1, "Shirt", null, null, storeID1);
        assertTrue(response2.isSuccess());
        assertTrue(response2.getResult().isEmpty());

    }

    @Test
    public void categoryNotExistTest() {

        Response<List<String>> response1 = impl.inStoreProductSearch(userID1, null, "asdsjd", null, storeID1);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.categoryNotExist.toString(), response1.getDescription());

        Response<List<String>> response2 = impl.inStoreProductSearch(userID1, null, "asdsjdasdkdf", null, storeID1);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.categoryNotExist.toString(), response2.getDescription());

    }

    @Test
    public void negativePriceRangeTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");

        Response<List<String>> response1 = impl.inStoreProductFilter(userID1, null, null, 10, 0, null, storeID1, diaryProducts, null);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.priceRangeInvalid.toString(), response1.getDescription());
    }

    @Test
    public void productRatingInvalidTest() { //
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");

        Response<List<String>> response1 = impl.inStoreProductFilter(userID1, null, null, null, null, 7.0, storeID1, diaryProducts, null);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productRateInvalid.toString(), response1.getDescription());
    }

}
