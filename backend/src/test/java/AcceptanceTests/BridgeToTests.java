package AcceptanceTests;

import ServiceLayer.Response;
import Util.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface BridgeToTests {

    Response<String> init();


//    Response<String> payWithExternalPaymentService(int price,String cardNumber, int cvv, int month, int year, String holderID, String userID);


    Response<String> exitMarketSystem(String userID);

    Response<String> enterMarketSystem();

    Response<String> register(String userID, String userName, String birthdate, String country, String city, String address, String name, String password);

    Response<String> login(String userID, String username, String password);

    Response<String> logout(String userId);

    Response<String> addProductToStore(String userId, String storeID, String productName, int price, int quantity, String description, String categoryStr);

    Response<String> removeProductFromStore(String userId, String storeID, String productName);

    Response<String> updateProductInStore(String userId, String storeID, String productName, int price, int quantity, String description, String categoryStr);

    Response<String> appointStoreOwner(String nominatorUserId, String nominatedUsername, String storeID);

    Response<String> appointStoreManager(String nominatorUserId, String nominatedUsername, String storeID,
                                         boolean inventoryPermissions, boolean purchasePermissions);

    Response<String> updateStoreManagerPermissions(String nominatorUserId, String nominatedUsername, String storeID,
                                                   boolean inventoryPermissions, boolean purchasePermissions);

    Response<List<String>> generalProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating);

    Response<List<String>> generalProductSearch(String userId, String productName, String categoryStr, List<String> keywords);

    Response<Integer> checkingCartValidationBeforePurchase(String user_ID, String username, String birthday, String name, String country, String city, String address);

    Response<List<String>> getInformationAboutStores(String user_ID);

    Response<Map<String, String>> getInformationAboutRolesInStore(String user_ID, String store_ID);

    Response<Map<String, List<Integer>>> getAuthorizationsOfManagersInStore(String user_ID, String store_ID);

    Response<String> closeStore(String user_ID, String store_ID);

    Response<String> openStore(String user_ID, String name, String description);

    Response<String> addProductToBasket(String productName, int quantity, String storeId, String userId);

    Response<String> removeProductFromBasket(String productName, String storeId, String userId);

    Response<String> modifyShoppingCart(String productName, int quantity, String storeId, String userId);

    Response<Map<String, Integer>> marketManagerAskInfo(String user_ID);

    Response<Map<String, Integer>> storeOwnerGetInfoAboutStore(String user_ID, String store_ID);

    Response<List<String>> inStoreProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, String storeId, List<String> productsFromSearch, Double storeMinRating);

    Response<List<String>> inStoreProductSearch(String userId, String productName, String categoryStr, List<String> keywords, String storeId);

    Response<String> purchase(String user_ID, String country, String city, String address, String cardNumber,String currency, String holderName, int cvv, int month, int year, String holderID, int price, Map<String,Map<String, List<Integer>>> products) ;

    Response<String> addPurchaseRuleToStore(List<TestRuleDTO> testRules, List<String> operators, String userId, String storeId);

    Response<String> removePurchaseRuleFromStore(int ruleNum, String userId, String storeId);

    Response<String> addDiscountCondRuleToStore(List<TestRuleDTO> testRules, List<String> logicOperators, List<DiscountValueDTO> discDetails, List<String> numericalOperators,String userId, String storeId);

    Response<String> addDiscountSimpleRuleToStore(List<DiscountValueDTO> discDetails, List<String> discountValueOperators, String userId, String storeId);

    Response<String> removeDiscountRuleFromStore(int ruleNum, String userId, String storeId);

    Response<String> setUserConfirmationPurchase(String userID);

    Response<String> composeCurrentPurchaseRules(int ruleIndex1, int ruleIndex2, String operator, String userId, String storeId);

    Response<String> composeCurrentSimpleDiscountRules(int ruleIndex1, int ruleIndex2, String numericalOperator, String userId, String storeId);

    Response<String> composeCurrentCondDiscountRules(int ruleIndex1, int ruleIndex2, String logicalOperator, String numericalOperator, String userId, String storeId);

    Response<String> answerJobProposal(String userId, String storeId, boolean managerProposal, boolean answer);

    void resetAllTables();

}