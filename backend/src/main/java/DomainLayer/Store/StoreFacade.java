package DomainLayer.Store;

import DomainLayer.Repositories.StoreMemoryRepository;
import DomainLayer.Repositories.StoreRepository;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.SimpleDiscountValue;
import Util.*;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.SimpleRule;
//import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional;
import org.apache.tomcat.util.digester.Rules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreFacade {
    private static StoreFacade storeFacadeInstance;

    private StoreRepository allStores ;


    public StoreFacade()
    {
        allStores = new StoreMemoryRepository();
    }

    public synchronized static StoreFacade getInstance() {
        if (storeFacadeInstance == null) {
            storeFacadeInstance = new StoreFacade();
        }
        return storeFacadeInstance;
    }


    // Added for testing purposes
    @Autowired
    public StoreFacade(StoreRepository storeRepository) throws Exception {// throws Exception {
        this.allStores = storeRepository;
        //setAllStores();
    }


    // Added for testing purposes
    @Transactional
    public void setAllStores() throws Exception {
//        String id = openStore("store1", "store1");
//        addProductToStore(id, new ProductDTO("Fries", 10, 10, "good", "FOOD"));

//        List<TestRuleDTO> testRules1 = new ArrayList<>();
//        testRules1.add(new TestRuleDTO("Age", "Above", null, null, "User must be above 18 to buy", true, 18, null, null, null, null));
//        addPurchaseRuleToStore(testRules1, new ArrayList<>(), id);
//
//        List<TestRuleDTO> testRules2 = new ArrayList<>();
//        testRules2.add(new TestRuleDTO("Age", "Above", null, null, "User must be above 18 to buy", true, 18, null, null, null, null));
//        testRules2.add(new TestRuleDTO("Age", "Below", null, null, "User must be below 30 to buy", true, 30, null, null, null, null));
//        List<String> operators = new ArrayList<>();
//        operators.add("AND");
//        addPurchaseRuleToStore(testRules2, operators, id);

//        List<DiscountValueDTO> discDetails1 = new ArrayList<>();
//        discDetails1.add(new DiscountValueDTO(10, "FOOD", false, null));
//        List<String> numericalOperators1 = new ArrayList<>();
//        addDiscountSimpleRuleToStore(discDetails1, numericalOperators1, id);
//
//        List<DiscountValueDTO> discDetails2 = new ArrayList<>();
//        discDetails2.add(new DiscountValueDTO(20, "ELECTRONICS", false, null));
//        discDetails2.add(new DiscountValueDTO(30, "FOOD", false, null));
//        List<String> numericalOperators2 = new ArrayList<>();
//        numericalOperators2.add("MAX");
//        addDiscountSimpleRuleToStore(discDetails2, numericalOperators2, id);
//
//        List<TestRuleDTO> testRules3 = new ArrayList<>();
//        testRules3.add(new TestRuleDTO("Age", "Above", null, null, "User must be above 18 to buy", true, 18, null, null, null, null));
//        List<DiscountValueDTO> discDetails3 = new ArrayList<>();
//        discDetails3.add(new DiscountValueDTO(20, "DAIRY", false, null));
//        List<String> numericalOperators3 = new ArrayList<>();
//        addDiscountCondRuleToStore(testRules3, new ArrayList<>(), discDetails3, numericalOperators3, id);
//
//        UserDTO user = new UserDTO("1", "Tomer", "12/12/1998", "israel", "Bash", "Bash", "Tomer");
//
//        checkPurchasePolicy(user, new ArrayList<>(), id);
//
//        removeProductFromStore(id, "Fries");
//
//        removeDiscountRuleFromStore(0, id);
//
//        composeCurrentPurchaseRules(0, 1, "OR", id);
    }

    public void reset() {
        allStores.deleteAll();
    }

    public StoreFacade newForTest(){
        storeFacadeInstance= new StoreFacade();
        return storeFacadeInstance;
    }


    public boolean isStoreOpen(String storeId) throws Exception {
        if (getStoreByID(storeId)==null){
            return false;
        }
        return getStoreByID(storeId).getIsOpened();
    }


    public String getStoreName(String storeId) throws Exception {
        return getStoreByID(storeId).getStoreName();
    }

    public String getStoreId(String storeName){
        List<Store> stores = getAllStores();
        for (Store store : stores) {
            if(store.getStoreName().equals(storeName)){
                return store.getStore_ID();
            }
        }
        return "not found";
    }


    public void returnProductToStore(Map<String, List<Integer>> products , String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        store.returnProductToStore(products);
        allStores.save(store);
    }

    public Store getStoreByID(String storeID) throws Exception {
        try {
            Optional<Store> store = allStores.findById(storeID);
            return store.orElse(null);
        } catch (Exception e) {
            throw new Exception(ExceptionsEnum.DatabaseIsNotConnected.toString());
        }

        //return allStores.getById(storeID);
    }

    public List<Store> getAllStores() {
        return allStores.findAll();
    }

    public StoreDTO getStoreDTOFromStore(Store store){
        return new StoreDTO(store.getProducts(), store.getStoreID(), store.getIsOpened(),store.getRating(), store.getNumOfRatings(),store.getStoreName() , store.getDescription());
    }

    public List<StoreDTO> getAllDTOs(){
        List<StoreDTO> storesDTOList= new ArrayList<>();
        List<Store> allStoresList = getAllStores();
        for (Store store : allStoresList){
            storesDTOList.add(getStoreDTOFromStore(store));
        }
        return storesDTOList;
    }


    public List<ProductDTO> getStoreProductsDTO(String storeId) throws Exception {
        //Store store = getStoreByID(storeId);
        Store store = getStoreByID(storeId);
        return  store.getProductsDTO();
    }

    public StoreDTO getStoreDTOById(String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        return getStoreDTOFromStore(store);
    }

    public void errorIfStoreNotExist(String storeID) throws Exception {
        if(getStoreByID(storeID) == null){
            throw new Exception(ExceptionsEnum.storeNotExist.toString());
        }
    }

    public List<String> getStoreCategories(){
        List<String> categories = new ArrayList<>();
        for (Category cat : EnumSet.allOf(Category.class)){
            categories.add(cat.toString());
        }
        return categories;
    }

    public String getNewStoreId(){
        UUID uuid = UUID.randomUUID();
        String storeId = "store" + uuid.toString();
        return storeId;
    }

    public String openStore(String name, String description)
    {
        String storeId = getNewStoreId();
        Store newStore = new Store(storeId, name, description); //todo: add this to list in repository
        this.allStores.save(newStore);
        return newStore.getStoreID();
    }


    public void checkQuantityAndPrice(String productName, int quantity, String storeId) throws Exception {
        this.checkIfProductExists(productName, storeId);
        this.checkProductQuantityAvailability(productName, storeId, quantity);
        this.checkIfProductQuantityIsPositive(quantity);
        Store store = getStoreByID(storeId);
        checkProductPrice(store.getProductDTOByName(productName, quantity));
    }

    public void checkIfProductExists(String productName, String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        if (!store.checkProductExists(productName))
        {
            throw new IllegalArgumentException(ExceptionsEnum.productNotExistInStore.toString());
        }
    }

    public List<ProductDTO> getProductsDTOSByProductsNames(Map<String, List<Integer>> products, String storeId) throws Exception {
        List<ProductDTO> productDTOS = new ArrayList<>();
        Store store = getStoreByID(storeId);
        for (Map.Entry<String, List<Integer>> product : products.entrySet()) {
            String productName = product.getKey();
            int quantity = product.getValue().get(0);
            productDTOS.add(store.getProductDTOByName(productName, quantity));
        }
        return productDTOS;
    }

    public void checkProductQuantityAvailability(String productName, String storeId, int quantity) throws Exception {
        Store store = getStoreByID(storeId);
        if (!store.checkProductQuantity(productName, quantity))
        {
            throw new IllegalArgumentException(ExceptionsEnum.productQuantityNotExist.toString());
        }
    }

    public void checkIfProductQuantityIsPositive(int quantity)
    {
        if (quantity <= 0)
        {
            throw new IllegalArgumentException(ExceptionsEnum.productQuantityIsNegative.toString());
        }
    }

    public void checkPurchasePolicy(UserDTO userDTO, List<ProductDTO> products, String storeId) throws Exception {
        Store store = getStoreByID(storeId);

        if (!store.checkPurchasePolicy(userDTO, products))
        {
            throw new IllegalArgumentException(ExceptionsEnum.purchasePolicyIsNotMet.toString());
        }
    }

    public int calcDiscountPolicy(UserDTO userDTO, List<ProductDTO> products, String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        return store.calcDiscountPolicy(userDTO, products);
    }

    public int calcPrice(String productName, int quantity, String storeId, String userId) throws Exception {
        Store store = getStoreByID(storeId);
        return store.calcPriceInStore(productName, quantity, userId);
    }

    @Transactional
    public void addProductToStore(String storeId, ProductDTO product) throws Exception {

        checkProductPrice(product);
        if (!checkProductExistInStore(product.getName(), storeId)) {
            if (product.getQuantity() >= 0) {
                Store store = getStoreByID(storeId);
                store.addProduct(product);
                allStores.save(store);
               // allStores.addProductToStore(storeId, product.getName(), product.getPrice(), product.getQuantity(), product.getCategoryStr(), product.getDescription());
                //getStoreByID(storeId).addProduct(product);
            } else {
                throw new Exception(ExceptionsEnum.productQuantityIsNegative.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.productAlreadyExistInStore.toString());
        }
    }

    private void checkProductPrice(ProductDTO product) {
        if (product.getPrice() < 0)
            throw new IllegalArgumentException(ExceptionsEnum.NegativePrice.toString());
    }


    public void removeProductFromStore(String storeId, String productName) throws Exception {
        if (checkProductExistInStore(productName, storeId)) {
            Store store = getStoreByID(storeId);
            store.removeProduct(productName);
            allStores.save(store);
        } else {
            throw new Exception(ExceptionsEnum.productNotExistInStore.toString());
        }

    }

    public void updateProductInStore(String storeId, ProductDTO product) throws Exception {

        checkProductPrice(product);
        if (checkProductExistInStore(product.getName(), storeId)) {
            if (product.getQuantity() >= 0) {
                Store store = getStoreByID(storeId);
                store.updateProduct(product);
                allStores.save(store);
            } else {
                throw new Exception(ExceptionsEnum.productQuantityIsNegative.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.productNotExistInStore.toString());
        }
    }

    public boolean verifyStoreExist(String storeID) throws Exception {
        return getStoreByID(storeID) != null;
    }

    public void verifyStoreExistError(String storeID) throws Exception {
        if(!verifyStoreExist(storeID))
            throw new Exception(ExceptionsEnum.storeNotExist.toString());
    }

    public void closeStore(String store_ID) throws Exception
    {
        Store storeToClose = getStoreByID(store_ID);
        if (!storeToClose.getIsOpened()){
            throw new Exception(ExceptionsEnum.storeNotExist.toString());
        }
        storeToClose.closeStore();
        allStores.save(storeToClose);
    }

    public void reopenStore(String store_ID) throws Exception
    {
        Store storeToReOpen = getStoreByID(store_ID);
        storeToReOpen.reopenStore();
        allStores.save(storeToReOpen);
    }

    public List<String> getInformationAboutOpenStores()
    {
        List<String> openStoreInformation = new ArrayList<>();
        for (Store store : getAllStores()) {
            //int storeId = entry.getKey();
            //Store store = entry.getValue();
            if (store.getIsOpened())
                openStoreInformation.add(store.getStoreID());
        }
        return openStoreInformation;
    }

    public List<String> getInformationAboutClosedStores() {
        List<String> closedStoreInformation = new ArrayList<>();
        for (Store store : getAllStores()) {
            //int storeId = entry.getKey();
            //Store store = entry.getValue();
            if (!store.getIsOpened()) {
                closedStoreInformation.add(store.getStoreID());
            }

        }
        return closedStoreInformation;
    }

    public List<String> getStoreProducts(String store_ID) throws Exception {
        Store store = getStoreByID(store_ID);
        return store.getProducts();
    }

    public List<String> inStoreProductSearch(String productName, String categoryStr, List<String> keywords, String storeId) throws Exception {
        Store storeToSearchIn = getStoreByID(storeId);
        List<String> filteredProducts = storeToSearchIn.matchProducts(productName, categoryStr, keywords);
        return filteredProducts;
    }

    public List<ProductDTO> inStoreProductSearchDTO(String productName, String categoryStr, List<String> keywords, String storeId) throws Exception {
        Store storeToSearchIn = getStoreByID(storeId);
        List<ProductDTO> filteredProducts = storeToSearchIn.matchProductsDTO(productName, categoryStr, keywords);
        return filteredProducts;
    }


    public List<String> inStoreProductFilter(String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double minRating, String storeId, List<String> productsFromSearch, Double storeMinRating) throws Exception {
        Store storeToSearchIn = getStoreByID(storeId);
        List<String> filteredProducts = storeToSearchIn.filterProducts(categoryStr, keywords, minPrice, maxPrice, minRating, productsFromSearch, storeMinRating);
        return filteredProducts;
    }

    public void checkCategory(String categoryStr)
    {
        if (categoryStr != null)
        {
            Category.fromString(categoryStr);
        }
    }

    public boolean checkProductExistInStore(String productName, String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        return store.checkProductExists(productName);
    }

    public List<String> getStores() throws Exception {

        try{
            return allStores.getAllIds();
        }
        catch(Exception e) {
            throw new Exception(ExceptionsEnum.DatabaseIsNotConnected.toString());
//        return allStores.getAllIds();
        }
    }


//    public void addReceiptToStore(String storeId, String  receiptId, String userId)
//    {
//        allStores.getById(storeId).addReceipt(receiptId, userId);
//       // getStoreByID(storeId).addReceipt(receiptId, userId);
//    }


    public void addPurchaseRuleToStore(List<TestRuleDTO> testRules, List<String> operators, String storeId) throws Exception {
        List<Rule> rules = new ArrayList<>();
        for (TestRuleDTO testRule : testRules) {
            rules.add(new SimpleRule(testRule));
        }
        Store store = getStoreByID(storeId);
        store.addPurchaseRule(rules, operators);
        allStores.save(store);
    }

    //implement removeRuleFromStore
    public void removePurchaseRuleFromStore(int ruleNum, String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        store.removePurchaseRule(ruleNum);
        allStores.save(store);
    }

    public void addDiscountCondRuleToStore(List<TestRuleDTO> testRules, List<String> operators, List<DiscountValueDTO> discDetails, List<String> numericalOperators, String storeId) throws Exception {

        List<DiscountValue> discountValue = getDiscountValuesList(discDetails);

        List<Rule> rules = new ArrayList<>();
        for (TestRuleDTO testRule : testRules) {
            rules.add(new SimpleRule(testRule));
        }
        Store store = getStoreByID(storeId);
        store.addDiscountCondRule(rules, operators, discountValue, numericalOperators);
        allStores.save(store);
    }

    public void addDiscountSimpleRuleToStore(List<DiscountValueDTO> discDetails, List<String> numericalOperators, String storeId) throws Exception {
        List<DiscountValue> discountValue = getDiscountValuesList(discDetails);
        Store store = getStoreByID(storeId);
        store.addDiscountSimple(discountValue, numericalOperators);
        allStores.save(store);
    }

    public void removeDiscountRuleFromStore(int ruleNum, String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        store.removeDiscountRule(ruleNum);
        allStores.save(store);
    }

    public List<DiscountValue> getDiscountValuesList(List<DiscountValueDTO> discDetails) {
        List<DiscountValue> discountValue = new ArrayList<>();

        for (DiscountValueDTO discDetail : discDetails) {
            discountValue.add(new SimpleDiscountValue(discDetail.getPercentage(), Category.fromString(discDetail.getCategory()) , discDetail.getIsStoreDiscount(), discDetail.getProductsNames()));
        }
        return discountValue;
    }

    public List<String> getStoreCurrentPurchaseRules(String storeId) throws Exception {
        //return getStoreByID(storeId).getStoreCurrentPurchaseRules();
        return getStoreByID(storeId).getStoreCurrentPurchaseRules();
    }

    public List<String> getStoreCurrentDiscountRules(String storeId) throws Exception {
        //return getStoreByID(storeId).getStoreCurrentDiscountRules();
        return getStoreByID(storeId).getStoreCurrentDiscountRules();
    }

    public void composeCurrentPurchaseRules(int ruleIndex1, int ruleIndex2, String operator, String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        store.composeCurrentPurchaseRules(ruleIndex1, ruleIndex2, operator);
        allStores.save(store);
    }

    public void composeCurrentSimpleDiscountRules(int ruleIndex1, int ruleIndex2, String numericalOperator, String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        store.composeCurrentSimpleDiscountRules(ruleIndex1, ruleIndex2, numericalOperator);
        allStores.save(store);
    }

    public void composeCurrentCondDiscountRules(int ruleIndex1, int ruleIndex2, String logicalOperator, String numericalOperator, String storeId) throws Exception {
        Store store = getStoreByID(storeId);
        store.composeCurrentCondDiscountRules(ruleIndex1, ruleIndex2, logicalOperator, numericalOperator);
        allStores.save(store);
    }

    public List<String> getStoreCurrentSimpleDiscountRules(String storeId) throws Exception {
       // return getStoreByID(storeId).getStoreCurrentSimpleDiscountRules();
        return getStoreByID(storeId).getStoreCurrentSimpleDiscountRules();
    }

    public List<String> getStoreCurrentCondDiscountRules(String storeId) throws Exception {
    //    return getStoreByID(storeId).getStoreCurrentCondDiscountRules();
        return getStoreByID(storeId).getStoreCurrentCondDiscountRules();
    }
}

