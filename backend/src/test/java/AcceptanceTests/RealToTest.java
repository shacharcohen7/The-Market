package AcceptanceTests;

import DomainLayer.Market.Market;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import DomainLayer.User.UserFacade;
import ServiceLayer.Response;
import PresentationLayer.WAF.Service_layer;
import Util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RealToTest implements BridgeToTests {


    private Service_layer service;

    public RealToTest()
    {
        //Market market = new Market();
        //this.service = new Service_layer(1, market);
    }

    @Autowired
    public RealToTest(Service_layer service)
    {
        this.service = service;
    }

//    @Autowired
//    public RealToTest(UserFacade userFacade, StoreFacade storeFacade, RoleFacade roleFacade,
//                      PaymentServicesFacade paymentServicesFacade, SupplyServicesFacade supplyServicesFacade)
//    {
//        Market market = new Market(userFacade,storeFacade,supplyServicesFacade,paymentServicesFacade,roleFacade);
//        this.service = new Service_layer(1, market);
//
//    }

    @Override
    public Response<String> init( ) {

        return service.init( );

    }

//    @Override
//    public Response<String> payWithExternalPaymentService(int price,String cardNumber, int cvv, int month, int year, String holderID, String userID)
//    {
//       // return service.payWithExternalPaymentService(price, cardNumber, cvv, month, year, holderID, userID);
//        // TODO
//    }



    public Response<String> exitMarketSystem(String userID)
    {
        return service.exitMarketSystem(userID);
    }

    public Response<String> enterMarketSystem()
    {
        return service.enterMarketSystem();
    }

    public Response<String> register(String userID, String userName, String birthdate, String country, String city, String address, String name, String password)
    {
        return service.register(new UserDTO(userID, userName, birthdate, country, city, address, name) , password);
    }

    public Response<String> login(String userID, String username, String password)
    {
        return service.login(userID, username, password);
    }

    public Response<String> logout(String userId)
    {
        return service.logout(userId);
    }

    public Response<String> addProductToStore(String userId, String storeID, String productName, int price, int quantity, String description, String categoryStr)
    {
        return service.addProductToStore(userId, storeID, new ProductDTO(productName, price, quantity, description, categoryStr));
    }

    public Response<String> removeProductFromStore(String userId, String storeID, String productName)
    {
        return service.removeProductFromStore(userId, storeID, productName);
    }

    public Response<String> updateProductInStore(String userId, String storeID, String productName, int price, int quantity, String description, String categoryStr)
    {
        return service.updateProductInStore(userId, storeID, new ProductDTO(productName, price, quantity, description, categoryStr));
    }

    public Response<String> appointStoreOwner(String nominatorUserId, String nominatorUsername, String storeID)
    {
        return service.appointStoreOwner(nominatorUserId, nominatorUsername, storeID);
    }

    public Response<String> appointStoreManager(String nominatorUserId, String nominatedUsername, String storeID,
                                         boolean inventoryPermissions, boolean purchasePermissions)
    {
        return service.appointStoreManager(nominatorUserId, nominatedUsername, storeID, inventoryPermissions, purchasePermissions);
    }

    public Response<String> updateStoreManagerPermissions(String nominatorUserId, String nominatedUsername, String storeID,
                                                   boolean inventoryPermissions, boolean purchasePermissions)
    {
        return service.updateStoreManagerPermissions(nominatorUserId, nominatedUsername, storeID, inventoryPermissions, purchasePermissions);
    }

    public Response<List<String>> generalProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating,
                                                List<String> productsFromSearch, Double storeMinRating)
    {
        return service.generalProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, productsFromSearch, storeMinRating);
    }

    public Response<List<String>> generalProductSearch(String userId, String productName, String categoryStr, List<String> keywords)
    {
        return service.generalProductSearch(userId, productName, categoryStr, keywords);
    }

    public Response<Integer> checkingCartValidationBeforePurchase(String user_ID, String username, String birthday, String name, String country, String city, String address)
    {

        return service.checkingCartValidationBeforePurchase(user_ID, new UserDTO(user_ID,username,birthday,country,city,address,name));
    }

    public Response<List<String>> getInformationAboutStores(String user_ID)
    {
        return service.getInformationAboutStores(user_ID);
    }

    public Response<Map<String, String>> getInformationAboutRolesInStore(String user_ID, String store_ID)
    {
        return service.getInformationAboutRolesInStore(user_ID, store_ID);
    }

    public Response<Map<String, List<Integer>>> getAuthorizationsOfManagersInStore(String user_ID, String store_ID)
    {
        return service.getAuthorizationsOfManagersInStore(user_ID, store_ID);
    }

    public Response<String> closeStore(String user_ID, String store_ID)
    {
        return service.closeStore(user_ID, store_ID);
    }

    public Response<String> openStore(String user_ID, String name, String description)
    {

        return service.openStore(user_ID, name, description);
    }

    public Response<String> addProductToBasket(String productName, int quantity, String storeId, String userId)
    {
        return service.addProductToBasket(productName, quantity, storeId, userId);
    }

    public Response<String> removeProductFromBasket(String productName, String storeId, String userId)
    {
        return service.removeProductFromBasket(productName, storeId, userId);
    }

    public Response<String> modifyShoppingCart(String productName, int quantity, String storeId, String userId)
    {
        return service.modifyShoppingCart(productName, quantity, storeId, userId);
    }

    public Response<Map<String, Integer>> marketManagerAskInfo(String user_ID)
    {
        return service.marketManagerAskInfo(user_ID);
    }

    public Response<Map<String, Integer>> storeOwnerGetInfoAboutStore(String user_ID, String store_ID)
    {
        return service.storeOwnerGetInfoAboutStore(user_ID, store_ID);
    }

    public Response<List<String>> inStoreProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating,
                                                String storeId, List<String> productsFromSearch, Double storeMinRating)
    {
        return service.inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);
    }

    public Response<List<String>> inStoreProductSearch(String userId, String productName, String categoryStr, List<String> keywords, String storeId)
    {
        return service.inStoreProductSearch(userId, productName, categoryStr, keywords, storeId);
    }

    @Override
    public Response<String> purchase(String user_ID, String country, String city, String address, String cardNumber,String currency, String holderName, int cvv, int month, int year, String holderID, int price, Map<String,Map<String, List<Integer>>> products) {
        return service.purchase(new UserDTO(user_ID,null, null, country, city, address, null) ,
                new PaymentDTO(holderName, holderID,currency, cardNumber,  cvv, month, year), new CartDTO(user_ID,price, products));

    }


    public Response<String> addPurchaseRuleToStore(List<TestRuleDTO> testRules, List<String> operators, String userId, String storeId) {
        return service.addPurchaseRuleToStore(testRules, operators, userId, storeId);
    }


    public Response<String> removePurchaseRuleFromStore(int ruleNum, String userId, String storeId) {
        return service.removePurchaseRuleFromStore(ruleNum, userId, storeId);
    }


    public Response<String> addDiscountCondRuleToStore(List<TestRuleDTO> testRules, List<String> logicOperators, List<DiscountValueDTO> discDetails, List<String> numericalOperators, String userId, String storeId) {
        return service.addDiscountCondRuleToStore(testRules, logicOperators, discDetails, numericalOperators, userId, storeId);
    }


    public Response<String> addDiscountSimpleRuleToStore(List<DiscountValueDTO> discDetails, List<String> discountValueOperators, String userId, String storeId) {
        return service.addDiscountSimpleRuleToStore(discDetails, discountValueOperators, userId, storeId);
    }


    public Response<String> removeDiscountRuleFromStore(int ruleNum, String userId, String storeId) {
        return service.removeDiscountRuleFromStore(ruleNum, userId, storeId);
    }

    public Response<String> setUserConfirmationPurchase(String userID) {
        return service.setUserConfirmationPurchase(userID);
    }

    @Override
    public Response<String> composeCurrentPurchaseRules(int ruleIndex1, int ruleIndex2, String operator, String userId, String storeId) {
        return service.composeCurrentPurchaseRules(ruleIndex1, ruleIndex2, operator, userId, storeId);
    }

    @Override
    public Response<String> composeCurrentSimpleDiscountRules(int ruleIndex1, int ruleIndex2, String numericalOperator, String userId, String storeId) {
        return service.composeCurrentSimpleDiscountRules(ruleIndex1, ruleIndex2, numericalOperator, userId, storeId);
    }

    @Override
    public Response<String> composeCurrentCondDiscountRules(int ruleIndex1, int ruleIndex2, String logicalOperator, String numericalOperator, String userId, String storeId) {
        return service.composeCurrentCondDiscountRules(ruleIndex1, ruleIndex2, logicalOperator, numericalOperator, userId, storeId);
    }

    @Override
    public Response<String> answerJobProposal(String userId, String storeId, boolean managerProposal, boolean answer) {
        return service.answerJobProposal(userId,storeId,managerProposal, answer);
    }

    @Override
    public void resetAllTables() {
        service.resetAllTables();
    }

}
