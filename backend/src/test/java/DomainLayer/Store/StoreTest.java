package DomainLayer.Store;

import DomainLayer.Store.StoreDiscountPolicy.DiscountPolicy;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StorePurchasePolicy.PurchasePolicy;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoreTest {

    @InjectMocks
    private Store store;

    @Mock
    private DiscountPolicy mockDiscountPolicy;

    @Mock
    private PurchasePolicy mockPurchasePolicy;

    @Mock
    private Product mockProduct;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        store = new Store("store1", "Test Store", "A store for testing");
        store.setDiscountPolicy(mockDiscountPolicy);
        store.setPurchasePolicy(mockPurchasePolicy);
    }

    @Test
    public void testGetStoreName() {
        assertEquals("Test Store", store.getStoreName());
    }

    @Test
    public void testReturnProductToStore() {
        when(mockProduct.getProductName()).thenReturn("product1");
        when(mockProduct.getQuantity()).thenReturn(10);
        store.addProduct(mockProduct);
        Map<String, List<Integer>> products = new HashMap<>();
        products.put("product1", Collections.singletonList(5));

        store.returnProductToStore(products);

        verify(mockProduct).addToStock(5);
    }

    @Test
    public void testGetStoreID() {
        assertEquals("store1", store.getStoreID());
    }

    @Test
    public void testAddProduct() {
        ProductDTO productDTO = new ProductDTO("product1", 10, 5, "A product", "TOYS");
        store.addProduct(productDTO);

        assertTrue(store.checkProductExists("product1"));
    }

    @Test
    public void testGetProductByName() {
        when(mockProduct.getProductName()).thenReturn("product1");
        store.addProduct(mockProduct);

        assertEquals(mockProduct, store.getProductByName("product1"));
    }

    @Test
    public void testCheckProductExists() {
        when(mockProduct.getProductName()).thenReturn("product1");
        store.addProduct(mockProduct);

        assertTrue(store.checkProductExists("product1"));
        assertFalse(store.checkProductExists("product2"));
    }

    @Test
    public void testCheckProductQuantity() {
        when(mockProduct.getProductName()).thenReturn("product1");
        when(mockProduct.getQuantity()).thenReturn(10);
        store.addProduct(mockProduct);

        assertTrue(store.checkProductQuantity("product1", 5));
        assertFalse(store.checkProductQuantity("product1", 15));
    }

    @Test
    public void testCalcPriceInStore() {
        when(mockProduct.getProductName()).thenReturn("product1");
        when(mockProduct.getPrice()).thenReturn(10);
        store.addProduct(mockProduct);

        assertEquals(50, store.calcPriceInStore("product1", 5, "user1"));
    }

    @Test
    public void testRemoveProduct() {
        store.addProduct(mockProduct);
        store.removeProduct("product1");

        assertFalse(store.checkProductExists("product1"));
    }

    @Test
    public void testUpdateProduct() {
        ProductDTO productDTO = new ProductDTO("product1", 20, 15, "Updated product", "BOOKS");
        when(mockProduct.getProductName()).thenReturn("product1");
        store.addProduct(mockProduct);

        store.updateProduct(productDTO);

        verify(mockProduct).setPrice(20);
        verify(mockProduct).setQuantity(15);
        verify(mockProduct).setDescription("Updated product");
        verify(mockProduct).setCategory(any(Category.class));
    }

    @Test
    public void testRemoveProductQuantity() {
        when(mockProduct.getProductName()).thenReturn("product1");
        when(mockProduct.getQuantity()).thenReturn(10);
        store.addProduct(mockProduct);

        store.removeProductQuantity("product1", 5);

        verify(mockProduct).setQuantity(5);
    }

    @Test
    public void testCloseStore() {
        store.closeStore();
        assertFalse(store.getIsOpened());
    }

    @Test
    public void testGetIsOpened() {
        assertTrue(store.getIsOpened());
        store.closeStore();
        assertFalse(store.getIsOpened());
    }

    @Test
    public void testGetProducts() {
        when(mockProduct.getProductName()).thenReturn("product1");
        store.addProduct(mockProduct);

        List<String> products = store.getProducts();

        assertEquals(1, products.size());
        assertEquals("product1", products.get(0));
    }

    @Test
    public void testCalcDiscountPolicy() {
        UserDTO userDTO = new UserDTO("user1");
        List<ProductDTO> products = Arrays.asList(new ProductDTO("product1", 10, 5, "A product", "TOYS"));
        when(mockDiscountPolicy.calcDiscountPolicy(userDTO, products)).thenReturn(5);

        assertEquals(5, store.calcDiscountPolicy(userDTO, products));
    }

    @Test
    public void testCheckPurchasePolicy() {
        UserDTO userDTO = new UserDTO("user1");
        List<ProductDTO> products = Arrays.asList(new ProductDTO("product1", 10, 5, "A product", "TOYS"));
        when(mockPurchasePolicy.checkPurchasePolicy(userDTO, products)).thenReturn(true);

        assertTrue(store.checkPurchasePolicy(userDTO, products));
    }

    @Test
    public void testMatchProducts() {
        when(mockProduct.getProductName()).thenReturn("product1");
        when(mockProduct.getCategoryName()).thenReturn("TOYS");
        when(mockProduct.getDescription()).thenReturn("A product description");
        store.addProduct(mockProduct);

        List<String> matchedProducts = store.matchProducts("product1", "TOYS", Arrays.asList("description"));

        assertEquals(1, matchedProducts.size());
        assertEquals("product1", matchedProducts.get(0));
    }

    @Test
    public void testMatchProductsDTO() {
        when(mockProduct.getProductName()).thenReturn("product1");
        when(mockProduct.getCategoryName()).thenReturn("TOYS");
        when(mockProduct.getDescription()).thenReturn("A product description");
        when(mockProduct.getProductDTO()).thenReturn(new ProductDTO("product1", 10, 5, "A product", "TOYS"));
        store.addProduct(mockProduct);

        List<ProductDTO> matchedProductsDTO = store.matchProductsDTO("product1", "TOYS", Arrays.asList("description"));

        assertEquals(1, matchedProductsDTO.size());
        assertEquals("product1", matchedProductsDTO.get(0).getName());
    }

    @Test
    public void testFilterProducts() {
        when(mockProduct.getProductName()).thenReturn("product1");
        when(mockProduct.getCategoryName()).thenReturn("TOYS");
        when(mockProduct.getDescription()).thenReturn("A product description");
        when(mockProduct.getPrice()).thenReturn(10);
        when(mockProduct.getRating()).thenReturn(4.5);
        store.addProduct(mockProduct);

        List<String> filteredProducts = store.filterProducts("TOYS", null, null, null, null, Arrays.asList(mockProduct.getProductName()), null);

        assertEquals(1, filteredProducts.size());
        assertEquals("product1", filteredProducts.get(0));
    }

    @Test
    public void testAddPurchaseRule() {
        List<Rule> rules = new ArrayList<>();
        List<String> operators = new ArrayList<>();
        store.addPurchaseRule(rules, operators);

        verify(mockPurchasePolicy).addRule(rules, operators);
    }

    @Test
    public void testRemovePurchaseRule() {
        store.removePurchaseRule(1);

        verify(mockPurchasePolicy).removeRule(1);
    }

    @Test
    public void testAddDiscountCondRule() {
        List<Rule> rules = new ArrayList<>();
        List<String> logicalOperators = new ArrayList<>();
        List<DiscountValue> discDetails = new ArrayList<>();
        List<String> numericalOperators = new ArrayList<>();
        store.addDiscountCondRule(rules, logicalOperators, discDetails, numericalOperators);

        verify(mockDiscountPolicy).addCondRule(rules, logicalOperators, discDetails, numericalOperators);
    }

    @Test
    public void testAddDiscountSimple() {
        List<DiscountValue> discDetails = new ArrayList<>();
        List<String> numericalOperators = new ArrayList<>();
        store.addDiscountSimple(discDetails, numericalOperators);

        verify(mockDiscountPolicy).addSimple(discDetails, numericalOperators);
    }

    @Test
    public void testRemoveDiscountRule() {
        store.removeDiscountRule(1);

        verify(mockDiscountPolicy).removeRule(1);
    }
}
