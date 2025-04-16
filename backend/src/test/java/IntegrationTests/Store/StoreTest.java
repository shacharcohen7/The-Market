package IntegrationTests.Store;

import DomainLayer.Store.Category;
import DomainLayer.Store.Product;
import DomainLayer.Store.Store;
import Util.ProductDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {

    private Store store;
    private final String storeId = "1";

    @BeforeEach
    public void setUp() {
        store = new Store(storeId, "Grocery", "");
    }

    @Test
    void testGetProductByName() {
        store.addProduct(new ProductDTO("Milk", 10, 100, "Dairy product", "Dairy"));
        Product product = store.getProductByName("Milk");

        assertNotNull(product);
        assertEquals("Milk", product.getProductName());
    }

    @Test
    void testAddProduct() {
        store.addProduct(new ProductDTO("Milk", 10, 100, "Dairy product", "Dairy"));
        Product product = store.getProductByName("Milk");

        assertNotNull(product);
        assertEquals("Milk", product.getProductName());
        assertEquals(10, product.getPrice());
        assertEquals(100, product.getQuantity());
        assertEquals("Dairy product", product.getDescription());
        Assertions.assertEquals(Category.DAIRY.toString(), product.getCategoryName());
    }

    @Test
    void testCheckProductExists() {
        store.addProduct(new ProductDTO("Milk", 10, 100, "Dairy product", "Dairy"));
        assertTrue(store.checkProductExists("Milk"));
        assertFalse(store.checkProductExists("Cheese"));
    }

    @Test
    void testCheckProductQuantity() {
        store.addProduct(new ProductDTO("Milk", 10, 100, "Dairy product", "Dairy"));
        assertTrue(store.checkProductQuantity("Milk", 50));
        assertFalse(store.checkProductQuantity("Milk", 150));
    }

    @Test
    void testCalcPriceInStore() {
        store.addProduct(new ProductDTO("Milk", 10, 100, "Dairy product", "Dairy"));
        int price = store.calcPriceInStore("Milk", 5, "1");

        assertEquals(50, price);
    }

    @Test
    void testRemoveProduct() {
        store.addProduct(new ProductDTO("Milk", 10, 100, "Dairy product", "Dairy"));
        store.removeProduct("Milk");

        assertFalse(store.checkProductExists("Milk"));
    }

    @Test
    void testUpdateProduct() {
        store.addProduct(new ProductDTO("Milk", 10, 100, "Dairy product", "Dairy"));
        store.updateProduct(new ProductDTO("Milk", 15, 200, "Updated description", "food"));

        Product product = store.getProductByName("Milk");
        assertEquals(15, product.getPrice());
        assertEquals(200, product.getQuantity());
        assertEquals("Updated description", product.getDescription());
        assertEquals(Category.FOOD.toString(), product.getCategoryName());
    }

    @Test
    void testCloseStore() {
        store.closeStore();
        assertFalse(store.getIsOpened());
    }

    @Test
    void testGetIsOpened() {
        assertTrue(store.getIsOpened());
    }

    @Test
    void testGetProducts() {
        store.addProduct(new ProductDTO("Milk", 10, 100, "Dairy product", "Dairy"));
        store.addProduct(new ProductDTO("Cheese", 20, 50, "Dairy product", "Dairy"));

        List<String> products = store.getProducts();

        assertEquals(2, products.size());
        assertTrue(products.contains("Milk"));
        assertTrue(products.contains("Cheese"));
    }

    @Test
    void testMatchProducts() {
        store.addProduct(new ProductDTO("Milk", 10, 100, "Dairy product", "Dairy"));
        store.addProduct(new ProductDTO("Cheese", 20, 50, "Dairy product", "Dairy"));

        List<String> matchedProducts1 = store.matchProducts("Milk", null, null);

        assertEquals(1, matchedProducts1.size());
        assertTrue(matchedProducts1.contains("Milk"));

        List<String> matchedProducts2 = store.matchProducts(null, "Dairy", null);

        assertEquals(2, matchedProducts2.size());
        assertTrue(matchedProducts2.contains("Milk"));
        assertTrue(matchedProducts2.contains("Cheese"));
    }

    @Test
    void testFilterProducts() {
        store.addProduct(new ProductDTO("Milk", 10, 100, "Dairy product", "Dairy"));
        store.addProduct(new ProductDTO("Cheese", 20, 50, "Dairy product", "Dairy"));

        List<String> productsFromSearch = Arrays.asList("Milk", "Cheese");
        List<String> filteredProducts = store.filterProducts("Dairy", null, 5, 15, null, productsFromSearch, null);

        assertEquals(1, filteredProducts.size());
        assertTrue(filteredProducts.contains("Milk"));
    }

    @Test
    void testReturnProductToStore() {
        store.addProduct(new ProductDTO("Milk", 10, 100, "Dairy product", "Dairy"));
        Map<String, List<Integer>> productsToReturn = new HashMap<>();
        List<Integer> quantityAndPrice = new ArrayList();
        quantityAndPrice.add(10);
        quantityAndPrice.add(50);
        productsToReturn.put("Milk", quantityAndPrice);

        store.returnProductToStore(productsToReturn);

        Product product = store.getProductByName("Milk");
        assertEquals(110, product.getQuantity());
    }

}