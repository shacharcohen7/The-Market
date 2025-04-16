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
import static org.junit.jupiter.api.Assertions.assertFalse;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GeneralSerach {

    @Autowired
    private BridgeToTests impl;

    static String userId0;
    static String storeId0;
    static String storeId1;
    


    @BeforeEach
    public void setUp() {
        userId0= impl.enterMarketSystem().getData();
        impl.register(userId0,"user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login(userId0, "user1", "fSijsd281");
        storeId0= impl.openStore(userId0, "Zara", "clothing store").getData();
        storeId1 = impl.openStore(userId0, "Bershka", "clothing store").getData();
        impl.addProductToStore(userId0, storeId0,"Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(userId0, storeId0,"Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(userId0, storeId1,"Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(userId0, storeId1,"Shoes", 4, 12, "Nike Shoes", "clothing");

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
        Response<List<String>> res = impl.generalProductSearch(userId0, null, null, null);
        assertTrue(res.isSuccess());
        List<String> unFilteredProducts = res.getResult();
        Set<String> filteredProductsSet = new HashSet<String>(unFilteredProducts);
        assertEquals(filteredProductsSet, productsSet);
    }

    @Test
    public void successfulSearchWithFiltersTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");

        Set<String> dairySet = new HashSet<String>(diaryProducts);

        assertTrue(impl.generalProductSearch(userId0, null, "FOOD", null).isSuccess());
        List<String> filteredProducts = impl.generalProductSearch(storeId0, null, "FOOD", null).getResult();
        Set<String> filteredProductsSet = new HashSet<String>(filteredProducts);
        assertEquals(filteredProductsSet, dairySet);

        List<String> shoes = new ArrayList<>();
        shoes.add("Shoes");
        assertTrue(impl.generalProductSearch(userId0, "Shoes", null, null).isSuccess());
        assertIterableEquals(impl.generalProductSearch(userId0, "Shoes", null, null).getResult(), shoes);
    }

    @Test
    public void categoryNotExistSearchTest() {
        Response<List<String>> response = impl.generalProductSearch(userId0, null, "asdsjd", null);
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.categoryNotExist.toString(), response.getDescription());
    }

    @Test
    public void categoryNotExistFilterSearchResultTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        Response<List<String>> response = impl.generalProductFilter(userId0, "acjdsfd", null, null, null, null, diaryProducts, null);
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.categoryNotExist.toString(), response.getDescription());
    }

    @Test
    public void negativePriceRangeTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        Response<List<String>> response = impl.generalProductFilter(userId0, null, null, 10, 0, null, diaryProducts, null);
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.priceRangeInvalid.toString(), response.getDescription());
    }

    @Test
    public void productRatingInvalidTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        Response<List<String>> response = impl.generalProductFilter(userId0, null, null, null, null, 7.0, diaryProducts, null);
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productRateInvalid.toString(), response.getDescription());
    }

    @Test
    public void storeRatingInvalidTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        Response<List<String>> response = impl.generalProductFilter(userId0, null, null, null, null, null, diaryProducts, 7.0);
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.storeRateInvalid.toString(), response.getDescription());
    }
}
