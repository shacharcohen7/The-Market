//package DomainLayer.Store;
//
//import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
//import Util.*;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//public class StoreFacadeTest {
//
//    //private final String userId = "1";
//    private  String storeId ;
//    private final String productName = "Product1";
//    private final int quantity = 2;
//    private final int totalPrice = 100;
//
//    private StoreFacade storeFacade;
//
//    @Mock
//    private StoreRepository storeRepository;
//
//    @Mock
//    private Store mockStore;
//
//    @Mock
//    private ProductDTO mockProduct;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        storeRepository = mock(StoreRepository.class);
//        mockStore = mock(Store.class);
//        mockProduct = mock(ProductDTO.class);
//
//        storeFacade = new StoreFacade(storeRepository); // Use constructor injection for testing
//    }
//
//    @Test
//    void testOpenStore() {
//        doNothing().when(storeRepository).add(anyString(), any(Store.class)); // Mock add method
//        String returnedStoreId = storeFacade.openStore("Grocery", "");
//        verify(storeRepository).add(anyString(), any(Store.class)); // Verify add method was called
//        assertNotNull(returnedStoreId);
//    }
//
//    @Test
//    void testAddProductToStoreSuccessfully() throws Exception {
//        String storeId = "store1";
//        when(mockProduct.getName()).thenReturn("product1");
//        when(mockProduct.getQuantity()).thenReturn(10);
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.checkProductExists("product1")).thenReturn(false);
//
//        storeFacade.addProductToStore(storeId, mockProduct);
//
//        verify(mockStore).addProduct(mockProduct);
//    }
//
//    @Test
//    void testRemoveProductFromStore() throws Exception {
//        String storeId = "store1";
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.checkProductExists(anyString())).thenReturn(true);
//
//        storeFacade.removeProductFromStore(storeId, "Product1");
//        verify(mockStore).removeProduct("Product1");
//    }
//
//    @Test
//    void testInStoreProductSearch() {
//        String storeId = "store1";
//        List<String> expectedProducts = Arrays.asList("Milk", "Cheese");
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.matchProducts(anyString(), anyString(), anyList())).thenReturn(expectedProducts);
//
//        List<String> result = storeFacade.inStoreProductSearch("Milk", "Dairy", Arrays.asList("Fresh"), storeId);
//
//        assertEquals(expectedProducts, result);
//    }
//
//    /*
//    @Test
//    void testCheckQuantityAndPolicies() {
//        when(mockStore.checkProductExists("Milk")).thenReturn(true);
//        when(mockStore.checkProductQuantity("Milk", 5)).thenReturn(true);
//        when(mockStore.checkPurchasePolicy("1", "Milk")).thenReturn(true);
//        when(mockStore.checkDiscountPolicy("1", "Milk")).thenReturn(true);
//
//
//        //if no exceptions thrown, this test passes
//        storeFacade.checkQuantityAndPolicies("Milk", 5, "0", "1");
//
//        verify(mockStore).checkProductExists("Milk");
//        verify(mockStore).checkProductQuantity("Milk", 5);
//        verify(mockStore).checkPurchasePolicy("1", "Milk");
//        verify(mockStore).checkDiscountPolicy("1", "Milk");
//
//        //fail case for the test
//        when(mockStore.checkProductExists("Milk")).thenReturn(false);
//        assertThrows(Exception.class, () -> storeFacade.checkQuantityAndPolicies("Milk", 5, "0", "1"));
//
//    }
//*/
//    @Test
//    void testCalcPrice() {
//        String storeId = "store1";
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.calcPriceInStore("Milk", 5, "1")).thenReturn(100);
//
//        int price = storeFacade.calcPrice("Milk", 5, storeId, "1");
//
//        assertEquals(100, price);
//        verify(mockStore).calcPriceInStore("Milk", 5, "1");
//    }
//
//    @Test
//    void testUpdateProductInStore() throws Exception {
//        String storeId = "store1";
//        ProductDTO productDTO = new ProductDTO("Milk", 100, 10, "Fresh Milk", "Dairy");
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.checkProductExists(anyString())).thenReturn(true);
//
//        storeFacade.updateProductInStore(storeId, productDTO);
//        verify(mockStore).updateProduct(productDTO);
//    }
//
//    @Test
//    void testVerifyStoreExist() {
//        String storeId = "store1";
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        boolean exists = storeFacade.verifyStoreExist(storeId);
//
//        assertTrue(exists);
//    }
//
//    @Test
//    void testCloseStore() throws Exception {
//        String storeId = "store1";
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.getIsOpened()).thenReturn(true);
//
//        storeFacade.closeStore(storeId);
//
//        verify(mockStore).closeStore();
//    }
//
//    @Test
//    void testGetInformationAboutOpenStores() {
//        String storeId = "store1";
//        when(storeRepository.getAll()).thenReturn(Arrays.asList(mockStore));
//        when(mockStore.getIsOpened()).thenReturn(true);
//        when(mockStore.getStoreID()).thenReturn("store1");
//
//        List<String> openStores = storeFacade.getInformationAboutOpenStores();
//
//        assertTrue(openStores.contains(storeId));
//    }
//
//    @Test
//    void testGetInformationAboutClosedStores() {
//        String storeId = "store1";
//        when(storeRepository.getAll()).thenReturn(Arrays.asList(mockStore));
//        when(mockStore.getIsOpened()).thenReturn(false);
//        when(mockStore.getStoreID()).thenReturn("store1");
//
//        List<String> closedStores = storeFacade.getInformationAboutClosedStores();
//
//        assertTrue(closedStores.contains(storeId));
//    }
//
//    @Test
//    void testGetStoreProducts() {
//        String storeId = "store1";
//        List<String> products = Arrays.asList("Milk", "Cheese");
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.getProducts()).thenReturn(products);
//
//        List<String> result = storeFacade.getStoreProducts(storeId);
//
//        assertEquals(products, result);
//    }
//
//    @Test
//    void testInStoreProductFilter() {
//        String storeId = "store1";
//        List<String> expectedProducts = Arrays.asList("Milk", "Cheese");
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.filterProducts(anyString(), anyList(), anyInt(), anyInt(), anyDouble(), anyList(), anyDouble())).thenReturn(expectedProducts);
//
//        List<String> result = storeFacade.inStoreProductFilter("Dairy", Arrays.asList("Fresh"), 10, 100, 4.5, storeId, Arrays.asList("Milk", "Cheese"), 4.0);
//
//        assertEquals(expectedProducts, result);
//        verify(mockStore).filterProducts("Dairy", Arrays.asList("Fresh"), 10, 100, 4.5, Arrays.asList("Milk", "Cheese"), 4.0);
//    }
//
//    @Test
//    void testCheckCategory() {
//        Exception exception = assertThrows(RuntimeException.class, () -> {
//            storeFacade.checkCategory(ExceptionsEnum.categoryNotExist.toString());
//        });
//
//        assertEquals(ExceptionsEnum.categoryNotExist.toString(), exception.getMessage());
//    }
//
//    @Test
//    void testCheckProductExistInStore() {
//        String storeId = "store1";
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.checkProductExists("NonExistentProduct")).thenReturn(false);
//
//        assertFalse(storeFacade.checkProductExistInStore("NonExistentProduct", storeId));
//    }
//
//    @Test
//    void testGetStores() {
//        List<String> storeIds = Arrays.asList("store1", "store2");
//        when(storeRepository.getAllIds()).thenReturn(storeIds);
//
//        List<String> stores = storeFacade.getStores();
//
//        assertTrue(stores.contains("store1"));
//        assertTrue(stores.contains("store2"));
//    }
//
//    @Test
//    void testAddReceiptToStore() {
//        String storeId = "store1";
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//
//        storeFacade.addReceiptToStore(storeId, "receipt1", "user1");
//
//        verify(mockStore).addReceipt("receipt1", "user1");
//    }
//
//    @Test
//    void testCheckQuantity() throws Exception {
//        String storeId = "store1";
//        String productName = "Milk";
//        int quantity = 5;
//
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.checkProductExists(productName)).thenReturn(true);
//        when(mockStore.checkProductQuantity(productName, quantity)).thenReturn(true);
//        when(mockStore.getProductDTOByName(productName, quantity)).thenReturn(mockProduct);
//        when(mockProduct.getPrice()).thenReturn(10);
//
//        storeFacade.checkQuantityAndPrice(productName, quantity, storeId);
//
//        verify(mockStore).checkProductExists(productName);
//        verify(mockStore).checkProductQuantity(productName, quantity);
//    }
//
//    @Test
//    void testCheckIfProductExists() {
//        String storeId = "store1";
//        String productName = "Milk";
//
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.checkProductExists(productName)).thenReturn(true);
//
//        storeFacade.checkIfProductExists(productName, storeId);
//
//        verify(mockStore).checkProductExists(productName);
//    }
//
//    @Test
//    void testCheckProductQuantityAvailability() {
//        String storeId = "store1";
//        String productName = "Milk";
//        int quantity = 5;
//
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.checkProductQuantity(productName, quantity)).thenReturn(true);
//
//        storeFacade.checkProductQuantityAvailability(productName, storeId, quantity);
//
//        verify(mockStore).checkProductQuantity(productName, quantity);
//    }
//
//    @Test
//    void testCheckIfProductQuantityIsPositive() {
//        int quantity = 5;
//
//        storeFacade.checkIfProductQuantityIsPositive(quantity);
//    }
//
//    @Test
//    void testCheckPurchasePolicy() {
//        String storeId = "store1";
//        UserDTO userDTO = mock(UserDTO.class);
//        List<ProductDTO> products = Arrays.asList(mock(ProductDTO.class));
//
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.checkPurchasePolicy(userDTO, products)).thenReturn(true);
//
//        storeFacade.checkPurchasePolicy(userDTO, products, storeId);
//
//        verify(mockStore).checkPurchasePolicy(userDTO, products);
//    }
//
//    @Test
//    void testCalcDiscountPolicy() {
//        String storeId = "store1";
//        UserDTO userDTO = mock(UserDTO.class);
//        List<ProductDTO> products = Arrays.asList(mock(ProductDTO.class));
//        int expectedDiscount = 10;
//
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//        when(mockStore.calcDiscountPolicy(userDTO, products)).thenReturn(expectedDiscount);
//
//        int discount = storeFacade.calcDiscountPolicy(userDTO, products, storeId);
//
//        assertEquals(expectedDiscount, discount);
//    }
//
//    @Test
//    void testAddPurchaseRuleToStore() {
//        String storeId = "store1";
//        TestRuleDTO rule1 = new TestRuleDTO("Price", "Above", null, null, "Basket price is greater than 100 shekels", true, null, null, null, 100, null);
//        TestRuleDTO rule2 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
//        List<TestRuleDTO> rules = new ArrayList<>();
//        rules.add(rule1);
//        rules.add(rule2);
//        List<String> operators = Arrays.asList("AND");
//
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//
//        storeFacade.addPurchaseRuleToStore(rules, operators, storeId);
//
//        verify(mockStore).addPurchaseRule(anyList(), eq(operators));
//    }
//
//    @Test
//    void testRemovePurchaseRuleFromStore() {
//        String storeId = "store1";
//        int ruleNum = 1;
//
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//
//        storeFacade.removePurchaseRuleFromStore(ruleNum, storeId);
//
//        verify(mockStore).removePurchaseRule(ruleNum);
//    }
//
//    @Test
//    void testAddDiscountCondRuleToStore() {
//        String storeId = "store1";
//        TestRuleDTO rule1 = new TestRuleDTO("Price", "Above", null, null, "Basket price is greater than 100 shekels", true, null, null, null, 100, null);
//        TestRuleDTO rule2 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
//        List<TestRuleDTO> rules = new ArrayList<>();
//        rules.add(rule1);
//        rules.add(rule2);
//        List<String> operators = Arrays.asList("AND");
//        List<DiscountValueDTO> discDetails = Arrays.asList(mock(DiscountValueDTO.class));
//        List<String> numericalOperators = Arrays.asList("GT");
//
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//
//        storeFacade.addDiscountCondRuleToStore(rules, operators, discDetails, numericalOperators, storeId);
//
//        verify(mockStore).addDiscountCondRule(anyList(), eq(operators), anyList(), eq(numericalOperators));
//    }
//
//    @Test
//    void testAddDiscountSimpleRuleToStore() {
//        String storeId = "store1";
//        List<DiscountValueDTO> discDetails = Arrays.asList(mock(DiscountValueDTO.class));
//        List<String> numericalOperators = Arrays.asList("GT");
//
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//
//        storeFacade.addDiscountSimpleRuleToStore(discDetails, numericalOperators, storeId);
//
//        verify(mockStore).addDiscountSimple(anyList(), eq(numericalOperators));
//    }
//
//    @Test
//    void testRemoveDiscountRuleFromStore() {
//        String storeId = "store1";
//        int ruleNum = 1;
//
//        when(storeRepository.get(storeId)).thenReturn(mockStore);
//
//        storeFacade.removeDiscountRuleFromStore(ruleNum, storeId);
//
//        verify(mockStore).removeDiscountRule(ruleNum);
//    }
//
//    @Test
//    void testGetDiscountValuesList() {
//        DiscountValueDTO discountValueDTO = new DiscountValueDTO(10, "Electronics", true, Arrays.asList("Product1"));
//        List<DiscountValueDTO> discDetails = Arrays.asList(discountValueDTO);
//        List<DiscountValue> discountValues = storeFacade.getDiscountValuesList(discDetails);
//
//        assertNotNull(discountValues);
//        assertEquals(1, discountValues.size());
//    }
//
//}
