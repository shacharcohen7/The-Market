package DomainLayer.Market;

import DomainLayer.AuthenticationAndSecurity.AuthenticationAndSecurityFacade;

//import DomainLayer.Notifications.Notification;
import DomainLayer.Notifications.LateNotificationFacade;
//import DomainLayer.Notifications.StoreNotification;
import DomainLayer.PaymentServices.Acquisition;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.PaymentServices.Receipt;
import DomainLayer.Repositories.InitializedDBRepository;
import DomainLayer.Repositories.InitializedRepository;
import DomainLayer.Role.RoleFacade;

import DomainLayer.Role.SystemManager;
import DomainLayer.Store.Product;

//import PresentationLayer.Vaadin.NotificationsEndPoint;
//import PresentationLayer.WAF.NotificationService;
import DomainLayer.SupplyServices.ExternalSupplyService;
import PresentationLayer.Vaadin.MyWebSocketHandler;
import PresentationLayer.WAF.Service_layer;
import Util.ExceptionsEnum;

import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import Util.*;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Month;
import java.time.Year;
import java.util.Map;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.concurrent.*;

@Service
public class Market {
    private static final Logger logger = LoggerFactory.getLogger(Service_layer.class);
    private static Market MarketInstance;
    private PaymentServicesFacade paymentServicesFacade;
    private SupplyServicesFacade supplyServicesFacade;
    //private Set<String> systemManagerIds;
    private AuthenticationAndSecurityFacade authenticationAndSecurityFacade;
    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private RoleFacade roleFacade;
    //private InitializedStatus initialized = new InitializedStatus(false);
    private InitializedRepository initializedRepository;
    private final Object initializedLock;
    private final Object managersLock;
    private final Object validationLock;


    private MyWebSocketHandler myWebSocketHandler;

    public synchronized static Market getInstance() {
//        if (MarketInstance == null) {
//            MarketInstance = new Market();
//        }
        return MarketInstance;
    }

    public Market() {
        StoreFacade storeFacade1 =  new StoreFacade();
        UserFacade userFacade1 =  new UserFacade();
        RoleFacade roleFacade1 =  new RoleFacade();
        AuthenticationAndSecurityFacade authenticationAndSecurityFacade1 =  new AuthenticationAndSecurityFacade();
        PaymentServicesFacade paymentServicesFacade1 =  new PaymentServicesFacade();
        SupplyServicesFacade supplyServicesFacade1 = new SupplyServicesFacade();
        storeFacade=storeFacade1;
        userFacade = userFacade1;
        roleFacade = roleFacade1;
        authenticationAndSecurityFacade = authenticationAndSecurityFacade1;
        paymentServicesFacade = paymentServicesFacade1;
        supplyServicesFacade = supplyServicesFacade1;
        initializedLock= new Object();
        //systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();
        //lateNotificationFacade = new LateNotificationFacade();

        MarketInstance = this;

        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();
    }



    public Market(UserFacade userFacade, AuthenticationAndSecurityFacade authenticationAndSecurityFacade,
                  StoreFacade storeFacade) {
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = PaymentServicesFacade.getInstance();
        this.authenticationAndSecurityFacade = authenticationAndSecurityFacade;
        supplyServicesFacade= SupplyServicesFacade.getInstance();
        initializedLock= new Object();
        //this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();
        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();
    }

    public Market(UserFacade userFacade, PaymentServicesFacade paymentServicesFacade,
                  SupplyServicesFacade supplyServicesFacade) throws Exception {
        this.storeFacade = StoreFacade.getInstance();
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= supplyServicesFacade;
        initializedLock= new Object();
        //this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();

        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }
    public Market(PaymentServicesFacade paymentServicesFacade,
                  SupplyServicesFacade supplyServicesFacade,AuthenticationAndSecurityFacade authenticationAndSecurityFacade){
        this.storeFacade = StoreFacade.getInstance();
        this.userFacade = UserFacade.getInstance();
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = authenticationAndSecurityFacade;
        this.supplyServicesFacade= supplyServicesFacade;
        initializedLock= new Object();
        //this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();


        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }
    public Market(UserFacade userFacade, AuthenticationAndSecurityFacade authenticationAndSecurityFacade,
                  StoreFacade storeFacade, SupplyServicesFacade supplyServicesFacade) {
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = PaymentServicesFacade.getInstance();
        this.authenticationAndSecurityFacade = authenticationAndSecurityFacade;
        this.supplyServicesFacade= supplyServicesFacade;
        initializedLock= new Object();
        //this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();

        //lateNotificationFacade = new LateNotificationFacade();

        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }
    public Market(UserFacade userFacade,  StoreFacade storeFacade ,
                  PaymentServicesFacade paymentServicesFacade, SupplyServicesFacade supplyServicesFacade){
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= supplyServicesFacade;
        initializedLock= new Object();
        //this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();

        //lateNotificationFacade = new LateNotificationFacade();

        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }

    public Market(UserFacade userFacade,  StoreFacade storeFacade ,
                  PaymentServicesFacade paymentServicesFacade, SupplyServicesFacade supplyServicesFacade, RoleFacade roleFacade) {
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = roleFacade;
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade = supplyServicesFacade;
        initializedLock = new Object();
        //this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();

        //lateNotificationFacade = new LateNotificationFacade();

        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler = MyWebSocketHandler.getInstance();
    }

    public Market(UserFacade userFacade) {
        this.storeFacade = StoreFacade.getInstance();
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = PaymentServicesFacade.getInstance();
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= SupplyServicesFacade.getInstance();
        initializedLock= new Object();
        //this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();

        //lateNotificationFacade = new LateNotificationFacade();

        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }

    @Autowired
    public Market(UserFacade userFacade, StoreFacade storeFacade, SupplyServicesFacade supplyServicesFacade,
                  PaymentServicesFacade paymentServicesFacade, RoleFacade roleFacade,
                  InitializedRepository initializedRepository) throws Exception {
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = roleFacade;
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= supplyServicesFacade;
        this.initializedRepository = initializedRepository;
        initializedLock= new Object();
        //this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();
       // systemManagerIds = new HashSet<>();
        try{
            Optional<InitializedStatus> initialized = initializedRepository.findById("market");
            InitializedStatus initializedStatus = initialized.orElse(null);
            if (initializedStatus == null) {
                initializedRepository.save(new InitializedStatus(false));
            }
        }
        catch(Exception e){
            throw new Exception(ExceptionsEnum.DatabaseIsNotConnected.toString());
        }
        //lateNotificationFacade = new LateNotificationFacade();

        myWebSocketHandler =  MyWebSocketHandler.getInstance();
    }

    public Market(UserFacade userFacade, PaymentServicesFacade paymentServicesFacade){
        this.storeFacade = StoreFacade.getInstance();
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= SupplyServicesFacade.getInstance();
        initializedLock= new Object();
        //this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();
        initializedRepository = null;

        //lateNotificationFacade = new LateNotificationFacade();

        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }

    public Market(PaymentServicesFacade paymentServicesFacade, StoreFacade storeFacade){
        this.storeFacade = storeFacade;
        this.userFacade = UserFacade.getInstance();
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= SupplyServicesFacade.getInstance();
        initializedLock= new Object();
        //this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();
        initializedRepository = null;

        //lateNotificationFacade = new LateNotificationFacade();

        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }


    public PaymentServicesFacade getPaymentServicesFacade(){
        return this.paymentServicesFacade;
    }

    public SupplyServicesFacade getSupplyServicesFacade(){
        return this.supplyServicesFacade;
    }

    @Transactional
    public synchronized Market newForTests(){
        MarketInstance = new Market();
        StoreFacade storeFacade1 =  storeFacade.newForTest();
        UserFacade userFacade1 =  userFacade.newForTest();
        RoleFacade roleFacade1 =  roleFacade.newForTest();
        AuthenticationAndSecurityFacade authenticationAndSecurityFacade1 =  authenticationAndSecurityFacade.newForTest();
        PaymentServicesFacade paymentServicesFacade1 =  paymentServicesFacade.newForTest();
        SupplyServicesFacade supplyServicesFacade1 = supplyServicesFacade.newForTest();
        storeFacade=storeFacade1;
        userFacade = userFacade1;
        roleFacade = roleFacade1;
        authenticationAndSecurityFacade = authenticationAndSecurityFacade1;
        paymentServicesFacade = paymentServicesFacade1;
        supplyServicesFacade = supplyServicesFacade1;
        //lateNotificationFacade = new LateNotificationFacade();


        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();

        return MarketInstance;



    }

    @Transactional
    public Map<String, Object> loadAdminConfiguration(String configFilePath) throws Exception {
        try (InputStream inputStream = new FileInputStream(configFilePath)) {
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(inputStream);
            return config;
        }
    }

//    public boolean checkHandShake(){
//        return paymentServicesFacade.checkHandShake();
//    }


    @Transactional
    public String init() throws Exception {

        logger.info("Starting the initialization of the system.");
        Map<String, Object> adminConfig = null; // Declare adminConfig here
        String configFilePath = "src/main/resources/ConfigurationFile.yaml";
        Map<String, Object> initConfig = loadAdminConfiguration(configFilePath);
        Map<String, Object> paymentConfig  = (Map<String, Object>) initConfig.get("paymentService");
        String paymentURL = (String) paymentConfig.get("url");
        Map<String, Object> serviceConfig  = (Map<String, Object>) initConfig.get("supplyService");
        String supplyURL = (String) serviceConfig.get("url");

        String adminPassword = "";

        synchronized (initializedLock) {
            if (Objects.requireNonNull(initializedRepository.findById("market").orElse(null)).isInitialized()) {
                logger.info("system already initialized");
                return "null";
            }
        }
        try {
            // Check for supply service
            if (supplyURL == null) {
                logger.error("system cannot be initialized, supply url problem");
                throw new IllegalArgumentException(ExceptionsEnum.InvalidSupplyServiceDetails.toString());
            }
            // Check for payment service
            if (paymentURL == null) {
                logger.error("system cannot be initialized, payment url problem");
                throw new IllegalArgumentException(ExceptionsEnum.InvalidPaymentServiceDetails.toString());
            }


             adminConfig = (Map<String, Object>) initConfig.get("admin");
             adminPassword = (String) adminConfig.get("password");
            validateAdminPassword1(adminPassword);

        } catch (Exception e) {
            throw e; // Re-throw the exception to be handled by the caller
        }


            // Extract admin configuration values
            String adminUsername = (String) adminConfig.get("username");
            String adminBirthday = (String) adminConfig.get("birthday");
            String adminCountry = (String) adminConfig.get("country");
            String adminCity = (String) adminConfig.get("city");
            String adminAddress = (String) adminConfig.get("address");
            String adminName = (String) adminConfig.get("name");


            logger.info("try register system admin");
            // Encode admin password
            String encryptedPassword = authenticationAndSecurityFacade.encodePassword(adminPassword);

            // Register user and retrieve system manager ID
            String firstUserID = enterMarketSystem();
            UserDTO userDTO1 = new UserDTO(firstUserID, adminUsername, adminBirthday, adminCountry, adminCity, adminAddress, adminName);
            String systemManagerId = userFacade.register(firstUserID, userDTO1, encryptedPassword);
            roleFacade.addSystemManager(systemManagerId);
//            synchronized (managersLock) {
//                roleFacade.addSystemManager(systemManagerId);
//            }
            if (!paymentServicesFacade.addExternalService(paymentURL)){
                throw new Exception("problem while adding external payment service");
            };
            if (!supplyServicesFacade.addExternalService(supplyURL)){
                throw new Exception("problem while adding external supply service");
            };
            synchronized (initializedLock) {
                try {
                    initializedRepository.save(new InitializedStatus(true));
                }
                catch (Exception e) {
                    throw new Exception(ExceptionsEnum.DatabaseIsNotConnected.toString());
                }
                logger.info("system initialized successfully");
            }

            return firstUserID; // Return the generated user ID
        }

    @Transactional
    public void validateAdminPassword1(String adminPassword) throws Exception {
        if (adminPassword == null || adminPassword.isEmpty()) {
            throw new Exception(ExceptionsEnum.emptyField.toString());
        }

        if (!checkPasswordValidation(adminPassword)) {
            throw new Exception("Password must contain at least one digit, one lowercase letter, one uppercase letter, and be at least 8 characters long.");
        }
    }

    @Transactional
    protected void registerUsersInitState(List<String> usersToRegister, Map<String, Object> usersData) throws Exception {
        for (Map.Entry<String, Object> entry : usersData.entrySet()) {
            String username = entry.getKey();
            if (usersToRegister.contains(username)) {
                Map<String, Object> userData = (Map<String, Object>) entry.getValue();
//
                logger.info("try register user");
                // Extract user details
                String username1 = (String) userData.get("username");
                String password = (String) userData.get("password");
                String birthday = (String) userData.get("birthday");
                String country = (String) userData.get("country");
                String city = (String) userData.get("city");
                String address = (String) userData.get("address");
                String name = (String) userData.get("name");

                String encryptedPassword = authenticationAndSecurityFacade.encodePassword(password);
                // Register user and retrieve system manager ID
                String userId = enterMarketSystem();
                UserDTO userDTO1 = new UserDTO(userId, username1, birthday, country, city, address, name);
                String memberId = userFacade.register(userId, userDTO1, encryptedPassword);
                logger.info(" user registered successfully");
            }
        }
    }

//    public int cancelPayment(String userId,  String transactionID) throws Exception {
//        if (userFacade.isMember(userId)) {
//            String memberId = userFacade.getMemberIdByUserId(userId);
//            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));if (!succeeded) {
//                logout(userId);
//                throw new Exception(ExceptionsEnum.sessionOver.toString());
//            }
//        }
//
//        return userFacade.cancelPaynmet(userId , transactionID);
//    }
//
//    public int cancelSupply(String shippingId) throws Exception {
//        return  supplyServicesFacade.cancelSupply(shippingId);
//
//    }

    @Transactional
    protected Map<String, String> loginUsersInitState(List<String> userToLogin, Map<String, Object> users) throws Exception {
        Map<String, String> usernameToUserIdMap = new HashMap<>();
        for (String username : userToLogin) {
            logger.info("try login user");
            if(userFacade.getMembers().getByUserName(username) !=null){
                Map<String, Object> userData = (Map<String, Object>) users.get(username);
                String password = (String) userData.get("password");
                String userId = enterMarketSystem();
                Login(userId, username, password);
                usernameToUserIdMap.put(username, userId);
            }
            logger.info(" user logged in successfully");
        }
        return usernameToUserIdMap;
    }

    @Transactional
    protected void openStoresInitState( Map<String, Object> stores, Map<String, String> usernameToUserIdMap ) throws Exception {

        for (Map.Entry<String, Object> entry : stores.entrySet()) {
            logger.info(" try open new store");
            Map<String, Object> storeData = (Map<String, Object>) entry.getValue();
//
//            // Extract store details
            String usernameFounder = (String) storeData.get("userNameFounder");
            String founderId = usernameToUserIdMap.get(usernameFounder);
            String storeName = (String) storeData.get("storeName");
            String storeDescription = (String) storeData.get("description");
            openStore(founderId,storeName, storeDescription);
            logger.info(" store opened in successfully");
        }
    }

    @Transactional
    protected void appointManagerToStoreInitState( Map<String, Object> actionData, Map<String, String> usernameToUserIdMap) throws Exception {
        String storeName=  (String) actionData.get("store");
        String storeId=  storeFacade.getStoreId(storeName);
        String userNameNominator=  (String) actionData.get("nominatorUser");
        List<String> nominatedUsers=  (List<String>) actionData.get("nominatedUsers");
        Boolean inventoryPermissions=  (Boolean) actionData.get("inventoryPermissions");
        Boolean purchasePermissions= (Boolean) actionData.get("purchasePermissions");
        String nominatorId = usernameToUserIdMap.get(userNameNominator);
        for(String userName : nominatedUsers){
            logger.info(" try appoint manager to store");
            appointStoreManager(nominatorId, userName, storeId,inventoryPermissions ,purchasePermissions);
            logger.info(" manager appointed successfully");
        }
    }

    @Transactional
    public List<String> getAllExternalSupplyServices(){
        return supplyServicesFacade.getAllSupplyServicesUrl();
    }

    @Transactional
    public List<String> getAllExternalPaymentServices(){
        return paymentServicesFacade.getAllPaymentServicesUrl();
    }

    @Transactional
    public List<StoreManagerDTO> getManagerJobProposal(String userId) throws Exception {
        String memberId = verifyToken(userId);
        if (memberId== null){
            throw new IllegalArgumentException("only members has job proposals");
        }
        return roleFacade.getAllManagerProposal(memberId);
    }

    @Transactional
    public void answerJobProposal(String userId, String storeId, boolean managerProposal, boolean answer) throws Exception {
        if (answer){
            if (managerProposal) {
                approveStoreManagerInvitation(userId, storeId);
            }
            else {
                approveStoreOwnerInvitation(userId, storeId);
            }
        }
        else {
            if (managerProposal){
                declineStoreManagerInvitation(userId, storeId);
            }
            else {
                declineStoreOwnerInvitation(userId, storeId);
            }

        }

    }


    @Transactional
    public List<StoreOwnerDTO> getOwnerJobProposal(String userId) throws Exception {
        String memberId = verifyToken(userId);
        if (memberId== null){
            throw new IllegalArgumentException("only members has job proposals");
        }
        return roleFacade.getAllOwnersProposal(memberId);
    }

    @Transactional
    protected void appointOwnerToStoreInitState(Map<String, Object> actionData, Map<String, String> usernameToUserIdMap) throws Exception {
        String storeName=  (String) actionData.get("store");
        String storeId=  storeFacade.getStoreId(storeName);
        String userNameNominator=  (String) actionData.get("nominatorUser");
        List<String> nominatedUsers=  (List<String>) actionData.get("nominatedUsers");
        String nominatorId = usernameToUserIdMap.get(userNameNominator);
        for(String userName : nominatedUsers){
            logger.info(" try appoint owner to store");
            appointStoreOwner(nominatorId, userName, storeId);
            logger.info(" owner appointed successfully");
        }
    }


    @Transactional
    protected void addProductToStoreInitState(Map<String, Object> products, Map<String, Object> actionData, Map<String, String> usernameToUserIdMap) throws Exception {


        List<String> productToAdd = (List<String>) actionData.get("productName");
        String storeName=  (String) actionData.get("store");
        String userName=  (String) actionData.get("user");

        for (Map.Entry<String, Object> entry : products.entrySet()) {
            logger.info(" try add products to store");
            Map<String, Object> productData = (Map<String, Object>) entry.getValue();
            String productName=  (String) productData.get("productName");

//            // Extract store details
            if(productToAdd.contains(productName)){
                int productPrice=  (int) productData.get("price");
                int productQuantity=  (int) productData.get("quantity");
                String productDescription=  (String) productData.get("description");
                String productCategory=  (String) productData.get("categoryStr");
                ProductDTO productDTO = new ProductDTO(productName,productPrice, productQuantity,productDescription ,productCategory);

                String userId = usernameToUserIdMap.get(userName);
                String storeId=  storeFacade.getStoreId(storeName);
                addProductToStore(userId,  storeId, productDTO);

                logger.info(" add product to store successfully");
            }
        }
    }

    @Transactional
    protected void logOutInitState(List<String> usersToLogout, Map<String, String> usernameToUserIdMap) throws Exception {
        for (String username : usersToLogout) {
            logger.info(" try log out user");
            String userId = usernameToUserIdMap.get(username);
            logout(userId);
            logger.info(" user logout successfully");
        }
    }

    @Transactional
        public void startStateInitialization() throws Exception {
        String configFilePath = "src/main/resources/stateAfterInit.yaml";
        Map<String, String> usernameToUserIdMap = new HashMap<>();
        Map<String, Object> stateConfig = loadAdminConfiguration(configFilePath);
        Map<String, Object> actions = (Map<String, Object>) stateConfig.get("actions");
        Map<String, Object> users = (Map<String, Object>) stateConfig.get("users");
        Map<String, Object> stores = (Map<String, Object>) stateConfig.get("stores");
        Map<String, Object> products = (Map<String, Object>) stateConfig.get("products");


            for (Map.Entry<String, Object> entry : actions.entrySet()) {
            String actionType = entry.getKey();
            Map<String, Object> actionData = (Map<String, Object>) entry.getValue();

            switch (actionType) {
                case "register_users":
                    List<String> userToRegister = (List<String>) actionData.get("users");
                    registerUsersInitState(userToRegister, users);
                    break;
                case "login_user":
                    List<String> userToLogin = (List<String>) actionData.get("users");
                    usernameToUserIdMap = loginUsersInitState(userToLogin, users);
                    break;
                case "open_store":
                    openStoresInitState(stores, usernameToUserIdMap);
                    break;
                case "add_product_to_store":
                    addProductToStoreInitState(products,actionData, usernameToUserIdMap);
                    break;
                case "set_manager_permission":
                    appointManagerToStoreInitState(actionData, usernameToUserIdMap);
                    break;
                case "set_Store_Owner":
                    appointOwnerToStoreInitState(actionData, usernameToUserIdMap);
                    break;
                case "LogOut":
                    List<String> logOutUsers=  (List<String>) actionData.get("users");
                    logOutInitState(logOutUsers, usernameToUserIdMap);
                    break;
            }
        }
    }

    @Transactional
    public List<ReceiptDTO> getStoreReceipts(String userId, String storeId) throws Exception {
        String memberId =  verifyToken(userId);
        verifyMemberExist(memberId);
        if (!roleFacade.verifyStoreOwner(storeId,memberId)){
            throw new IllegalArgumentException(ExceptionsEnum.userIsNotStoreOwner.toString());
        }
        if (!storeFacade.verifyStoreExist(storeId)){
            throw new IllegalArgumentException(ExceptionsEnum.storeNotExist.toString());
        }
        return paymentServicesFacade.getStoreAcquisitions(storeId);
    }


    @Transactional
    public boolean checkInitializedMarket(){
        return Objects.requireNonNull(initializedRepository.findById("market").orElse(null)).isInitialized();
    }

    @Transactional
    public void addExternalPaymentService( String paymentURL, String systemMangerUserId) throws Exception {
        String memberId = verifyToken(systemMangerUserId);
        verifyMemberExist(memberId);
        synchronized (managersLock) {
            if (!roleFacade.verifyMemberIsSystemManager(memberId)) {
                throw new Exception(ExceptionsEnum.SystemManagerPaymentAuthorization.toString());
            }
        }
        if (paymentURL== null  ) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidPaymentServiceParameters.toString());
        }
        if (!paymentServicesFacade.addExternalService( paymentURL)){
            throw new IllegalArgumentException(ExceptionsEnum.InvalidPaymentServiceParameters.toString());
        };
    }

    @Transactional
    public List<AcquisitionDTO> getAllSystemAcquisitions(String systemManagerUserId) throws Exception {
        String memberId = verifyToken(systemManagerUserId);
        verifyMemberExist(memberId);
        if (!getSystemManagerIds().contains(memberId)) {
            throw new Exception(ExceptionsEnum.notSystemManager.toString());
        }
        return paymentServicesFacade.getAllAcquisitions();
    }


    @Transactional
    public void removeExternalPaymentService(String url, String systemMangerUserId) throws Exception {
        String memberId = verifyToken(systemMangerUserId);
        verifyMemberExist(memberId);
        synchronized (managersLock) {
            if (!roleFacade.verifyMemberIsSystemManager(memberId)) {
                throw new Exception(ExceptionsEnum.SystemManagerPaymentAuthorizationRemove.toString());
            }
        }

        paymentServicesFacade.removeExternalService(url);
    }

    @Transactional
    public void addExternalSupplyService(String SupplyURL, String systemManagerUserId) throws Exception {
        String memberId = verifyToken(systemManagerUserId);
        verifyMemberExist(memberId);
        synchronized (managersLock) {
            if (!roleFacade.verifyMemberIsSystemManager(memberId)) {
                throw new Exception(ExceptionsEnum.SystemManagerSupplyAuthorization.toString());
            }
        }
        if (SupplyURL == null   ) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidSupplyServiceParameters.toString());
        }

        if (!supplyServicesFacade.addExternalService(SupplyURL)){
            throw new Exception(ExceptionsEnum.InvalidSupplyServiceParameters.toString());
        };

    }

    @Transactional
    public void removeExternalSupplyService(String supplyURL, String systemManagerUserId) throws Exception {
        String memberId = verifyToken(systemManagerUserId);
        verifyMemberExist(memberId);
        synchronized (managersLock) {
            if (!roleFacade.verifyMemberIsSystemManager(memberId)) {
                throw new Exception(ExceptionsEnum.SystemManagerSupplyAuthorizationRemove.toString());
            }
        }
        supplyServicesFacade.removeExternalService(supplyURL);

    }

    @Transactional
    public List<String> getSystemManagerIds(){
        synchronized (managersLock) {
            return roleFacade.getSystemManagers();
        }
    }

    @Transactional
    public String purchase(PaymentDTO paymentDTO, UserDTO userDTO, CartDTO cartDTO) throws Exception {
        verifyToken(userDTO.getUserId());
        ScheduledFuture<?> timeoutHandle = null;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicBoolean timeoutExpired = new AtomicBoolean(false);

        try {
            timeoutHandle = scheduler.schedule(() -> {
                timeoutExpired.set(true);
            }, 15L, TimeUnit.SECONDS);

            boolean userReadyToPay;
            for(userReadyToPay = false; !userReadyToPay && !timeoutExpired.get(); userReadyToPay = this.getUserConfirmationPurchase(userDTO.getUserId())) {
            }

            if (!userReadyToPay || timeoutExpired.get()) {
                throw new RuntimeException(ExceptionsEnum.TimeExpired.toString());
            }

            //this.payWithExternalPaymentService(cartDTO, paymentDTO, userDTO.getUserId());

            //String availableExternalSupplyService = this.checkAvailableExternalSupplyService(userDTO.getCountry(), userDTO.getCity());
            String acquisitionId = this.payWithExternalPaymentService(cartDTO,paymentDTO, userDTO.getUserId());

            try {
                this.createShiftingDetails(userDTO.getCountry(), userDTO.getCity(), userDTO.getAddress(), userDTO.getUserId(), acquisitionId);
            }
            catch (Exception e ){
                boolean succeed = paymentServicesFacade.cancelPayment(acquisitionId);
                if (cartDTO != null) {
                    this.returnCartToStock(cartDTO.getStoreToProducts());
                }
                if (succeed){
                    throw new Exception("purchase canceled");
                }
                throw new Exception("system Error - please contact us!");
            }
            userFacade.emptyCart(cartDTO.getUserID());
            sendMessagesOnPurchaseToStoreOwners(cartDTO);
            return acquisitionId;
        } catch (Exception exception) {
            Exception e = exception;
            if (cartDTO != null) {
                this.returnCartToStock(cartDTO.getStoreToProducts());
            }
            throw e;
        } finally {
            if (timeoutHandle != null && !timeoutHandle.isDone()) {
                timeoutHandle.cancel(true);
            }

            scheduler.shutdown();
        }

    }



    @Transactional
    public void sendMessagesToOwnersAndManagers(String storeId , String message) throws Exception { // Inject VaadinUserService and NotificationService


        List<String> storeOwnerIds = roleFacade.getAllStoreOwners(storeId);
        //String storeName = storeFacade.getStoreName(storeId);

        List<String> storeManagersIds = roleFacade.getAllStoreManagers(storeId);
        Set<String> hasJob = new HashSet<>();
        hasJob.addAll(storeManagersIds);
        hasJob.addAll(storeOwnerIds);
        for (String memberId : hasJob) {
                //String message = "A purchase was made from your store - " + storeName;
            myWebSocketHandler.handleStringMessage(memberId, message); // Send real-time notification via WebSocket
        }
    }




    @Transactional
    public void sendMessagesOnPurchaseToStoreOwners(CartDTO cartDTO) throws Exception { // Inject VaadinUserService and NotificationService
        for (String storeId : cartDTO.getStoreToProducts().keySet()) {
            List<String> storeOwnerIds = roleFacade.getAllStoreOwners(storeId);
            String storeName = storeFacade.getStoreName(storeId);

            for (String memberId : storeOwnerIds) {
                String message = "A purchase was made from your store - " + storeName;
                myWebSocketHandler.handleStringMessage(memberId, message); // Send real-time notification via WebSocket
            }
        }
    }

    @Transactional
    public List<String> getUserNotifications(String memberId){
        return myWebSocketHandler.getUserNotifications(memberId);
    }

    @Transactional
    public void setUserConfirmationPurchase(String userID) throws Exception {
        verifyToken(userID);
        this.userFacade.setUserConfirmationPurchase(userID);
    }

    @Transactional
    public boolean getUserConfirmationPurchase(String userID) throws Exception {
        verifyToken(userID);
        return userFacade.getUserConfirmationPurchase(userID);
    }

    @Transactional
    public void purchaseForTest(PaymentDTO paymentDTO, UserDTO userDTO) throws Exception {
        CartDTO cartDTO = null;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> timeoutHandle = null;

        try {
            cartDTO = checkingCartValidationBeforePurchase1(userDTO.getUserId(), userDTO);

            // Create a CompletableFuture for user input
//            CompletableFuture<Void> userInputFuture = new CompletableFuture<>();
//
//            // Schedule a task to complete the future with an exception if the user does not respond in time
//            timeoutHandle = scheduler.schedule(() -> {
//                userInputFuture.completeExceptionally(new TimeoutException("User input time expired"));
//            }, 5, TimeUnit.SECONDS);
//
//            // Here you should integrate your actual user input mechanism
//            // For this example, we simulate user input with a manual completion
//            // In a real application, replace this line with actual user input handling
//            // userInputFuture.complete(null);  // Uncomment this to simulate user input completion
//
//            // Wait for user input or timeout
//            userInputFuture.get();

            // Proceed with payment if user input is received
            payWithExternalPaymentService(cartDTO, paymentDTO, userDTO.getUserId());
        } catch (Exception e) {
            if (cartDTO != null) {
                returnCartToStock(cartDTO.getStoreToProducts());
            }
            throw e;
        } finally {
            if (timeoutHandle != null && !timeoutHandle.isDone()) {
                timeoutHandle.cancel(true);
            }
            scheduler.shutdown();
        }
    }

    @Transactional
    public CartDTO checkingCartValidationBeforePurchase1(String user_ID,UserDTO userDTO) throws Exception {
        if (userFacade.isMember(user_ID)) {
            String memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        int totalPrice = 0;
        this.userFacade.isUserCartEmpty(user_ID);

        List<String> stores = this.userFacade.getCartStoresByUser(user_ID);
        for(String store_ID: stores)
        {
            Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(store_ID, user_ID);
            int quantity;
            List<ProductDTO> productDTOS = this.storeFacade.getProductsDTOSByProductsNames(products, store_ID);
            for(String productName: products.keySet()) {
                quantity = products.get(productName).get(0);
                this.storeFacade.checkQuantityAndPrice(productName, quantity, store_ID);
            }

//            this.storeFacade.checkPurchasePolicy(userDTO, productDTOS, store_ID);
//            int priceToReduce = storeFacade.calcDiscountPolicy(userDTO, productDTOS, store_ID);

            String availableExternalSupplyService = this.checkAvailableExternalSupplyService(userDTO.getCountry(), userDTO.getCity());
           // this.createShiftingDetails(userDTO.getCountry(), userDTO.getCity(), availableExternalSupplyService, userDTO.getAddress(), user_ID);

            int storeTotalPriceBeforeDiscount = this.userFacade.getCartPriceByUser(user_ID);
            int storeTotalPrice = storeTotalPriceBeforeDiscount - 100;

//            int storeTotalPrice = storeTotalPriceBeforeDiscount - priceToReduce;
            totalPrice += storeTotalPrice;
        }
        //remove items from stock
        //removeUserCartFromStock(user_ID);
        return new CartDTO(user_ID,totalPrice,getPurchaseList(user_ID));
    }


    @Transactional
    public String payWithExternalPaymentService(CartDTO cartDTO,PaymentDTO payment, String userId) throws Exception{
        verifyToken(userId);
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH)+1;
        if(cartDTO.getCartPrice()<= 0 || payment.getMonth()> 12 || payment.getMonth()<1 || payment.getYear() < Year.now().getValue()
                ||payment.getHolderId()==null || payment.getCreditCardNumber().length()!=16 || cartDTO.getStoreToProducts()==null||
                (payment.getYear() == Year.now().getValue() && month>= payment.getMonth())) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidCreditCardParameters.toString());
        }
        if(paymentServicesFacade.getAllPaymentServices().isEmpty()){
            throw new Exception(ExceptionsEnum.noAvailableExternalPaymentService.toString());
        }
        String memberId = userFacade.getMemberIdByUserId(userId);
        String acquisitionId = paymentServicesFacade.pay(cartDTO.getCartPrice(), payment, userId,memberId, cartDTO.getStoreToProducts());
        Map<String,String> receiptIdStoreId = paymentServicesFacade.getAcquisitionReceipts(acquisitionId); //<receiptId, storeId>
        //print when implement notifications (purchase successes)

        //add acquisitionId to user
        //userFacade.addAcquisitionToUser(userId,acquisitionId);

        //Add the receiptId and userId to the store receipts map
//        for (String receiptId : receiptIdStoreId.keySet()) {
//            storeFacade.addReceiptToStore(receiptIdStoreId.get(receiptId), receiptId, userId);
//        }
        return acquisitionId;
    }

//    private void paymentFailed(CartDTO cartDTO) throws Exception {
//        returnCartToStock(cartDTO.getStoreToProducts());
//    }

    @Transactional
    //Map<StoreID, Map<ProductName, quantity>>
    protected void returnCartToStock(Map<String, Map<String, List<Integer>>> products) throws Exception {
        for (String storeId: products.keySet()){
            storeFacade.returnProductToStore(products.get(storeId), storeId);
        }
    }

    @Transactional
    public void logout(String userId) {
        userFacade.logout(userId);
        authenticationAndSecurityFacade.removeToken(userId);
    }

    @Transactional
    public void exitMarketSystem(String userId){
        userFacade.exitMarketSystem(userId);
    }

    @Transactional
    public String enterMarketSystem(){
        return userFacade.addUser();
    }

    public String register( String userId, UserDTO user, String password) throws Exception {
        //check password validation
        if (password == null || password.equals("")){
            throw new Exception(ExceptionsEnum.emptyField.toString());
        }
        if (!checkPasswordValidation(password)){
            throw new Exception("password must contains at least one digit, lowercase letter and uppercase letter.\n password must contains at least 8 characters");
        }
        String encryptedPassword = authenticationAndSecurityFacade.encodePassword(password);
        String memberId = userFacade.register(userId, user, encryptedPassword);
        //authenticationAndSecurityFacade.generateToken(memberId);
        return memberId;
    }

    @Transactional
    public boolean checkPasswordValidation(String password){
        String PASSWORD_PATTERN =
                "^(?=.*[0-9])" +           // at least one digit
                "(?=.*[a-z])" +            // at least one lowercase letter
                "(?=.*[A-Z])" +            // at least one uppercase letter
                ".{8,}$";
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();

    }

    @Transactional
    public String Login(String userId,String username, String password) throws Exception {
        String encryptedPassword = authenticationAndSecurityFacade.encodePassword(password);
        String memberId = userFacade.Login(userId, username,encryptedPassword);
        authenticationAndSecurityFacade.generateToken(memberId);
        return memberId;
    }

    @Transactional
    public void addProductToBasket(String productName, int quantity, String storeId, String userId) throws Exception {
        verifyToken(userId);
        storeFacade.checkQuantityAndPrice(productName, quantity, storeId);
        Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(storeId, userId);
        products.put(productName, new ArrayList<>(Arrays.asList(quantity)));
        List<ProductDTO> productDTOS = storeFacade.getProductsDTOSByProductsNames(products, storeId);
        UserDTO user = new UserDTO(userFacade.getUserByID(userId));
        storeFacade.checkPurchasePolicy(user, productDTOS, storeId);
        int priceToReduce = storeFacade.calcDiscountPolicy(user, productDTOS, storeId);
        int totalPrice = storeFacade.calcPrice(productName, quantity, storeId, userId);
        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);

    }

    @Transactional
    public void removeProductFromBasket(String productName, String storeId, String userId)throws Exception
    {
        verifyToken(userId);
        userFacade.checkIfCanRemove(productName, storeId, userId);
        userFacade.removeItemFromUserCart(productName, storeId, userId);
    }

    @Transactional
    public String openStore(String user_ID, String name, String description)throws Exception {
        String memberId = verifyToken(user_ID);
        userFacade.isUserLoggedInError(user_ID);
        if(name == null || name.equals("")) {
            throw new IllegalArgumentException(ExceptionsEnum.illegalStoreName.toString());
        }
        String store_ID = this.storeFacade.openStore(name, description);
        //String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        verifyMemberExist(memberId);
        this.roleFacade.createStoreOwnerWithoutAsk(memberId, store_ID, true, "no nominator");
        return store_ID;
    }

    @Transactional
    public void addProductToStore(String userId, String storeId, ProductDTO product) throws Exception {

        userFacade.errorIfUserNotExist(userId);
        //userFacade.errorIfUserNotMember(userId);
        String memberId = verifyToken(userId);
        verifyMemberExist(memberId);
        storeFacade.errorIfStoreNotExist(storeId);
        if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                (roleFacade.verifyStoreManager(storeId, memberId) &&
                        roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
            storeFacade.addProductToStore(storeId, product);
        } else {
            throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
        }
    }

    @Transactional
    public List<ShippingDTO> getUserShippingDTOs(String userId) throws Exception {
        String memberId = verifyToken(userId);
        verifyMemberExist(memberId);
        return supplyServicesFacade.getUserHistory(memberId);
    }

    @Transactional
    public List<ShippingDTO> getSystemsupplyHistory(String systemManagerUserId) throws Exception {
        String memberId = verifyToken(systemManagerUserId);
        verifyMemberExist(memberId);
        if (!roleFacade.verifyMemberIsSystemManager(memberId)){
            throw new IllegalArgumentException(ExceptionsEnum.notSystemManager.toString());
        }
        return supplyServicesFacade.getSystemHistory();
    }

    @Transactional
    public void removeProductFromStore(String userId, String storeId, String productName) throws Exception {
        String memberId = verifyToken(userId);
        userFacade.errorIfUserNotExist(userId);
        userFacade.errorIfUserNotMember(userId);
        //String memberId = userFacade.getMemberIdByUserId(userId);
        verifyMemberExist(memberId);
        storeFacade.errorIfStoreNotExist(storeId);
        if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                (roleFacade.verifyStoreManager(storeId, memberId) &&
                        roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
            storeFacade.removeProductFromStore(storeId, productName);
        } else {
            throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
        }
    }

    @Transactional
    public void updateProductInStore(String userId, String storeId, ProductDTO product) throws Exception {
        String memberId = verifyToken(userId);
        verifyMemberExist(memberId);
        userFacade.errorIfUserNotExist(userId);
        storeFacade.errorIfStoreNotExist(storeId);
        if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                (roleFacade.verifyStoreManager(storeId, memberId) &&
                        roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
            storeFacade.updateProductInStore(storeId, product);
        } else {
            throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
        }
    }

    @Transactional
    public void approveStoreOwnerInvitation(String userId,  String storeId) throws Exception {
        String memberId = verifyToken(userId);
        verifyMemberExist(memberId);
        storeFacade.errorIfStoreNotExist(storeId);
        String proposerMemberId = roleFacade.approveInvitationStoreOwner(memberId, storeId);
        myWebSocketHandler.handleStringMessage(proposerMemberId,
                userFacade.getMemberName(memberId)  + " approved your offer to be store Manager in store " + storeFacade.getStoreName(storeId));
    }

    @Transactional
    public void declineStoreOwnerInvitation(String userId,  String storeId) throws Exception {
        String memberId = verifyToken(userId);
        verifyMemberExist(memberId);
        storeFacade.errorIfStoreNotExist(storeId);
        String proposerMemberId = roleFacade.declineInvitationStoreOwner(memberId, storeId);
        myWebSocketHandler.handleStringMessage(proposerMemberId,
                userFacade.getMemberName(memberId)  + " declined your offer to be store Manager in store " + storeFacade.getStoreName(storeId));

    }

    @Transactional
    public void declineStoreManagerInvitation(String userId,  String storeId) throws Exception {
        String memberId = verifyToken(userId);
        verifyMemberExist(memberId);

        storeFacade.errorIfStoreNotExist(storeId);
        String proposerMemberId = roleFacade.declineInvitationStoreManager(memberId, storeId);
        myWebSocketHandler.handleStringMessage(proposerMemberId,
                userFacade.getMemberName(memberId)  + " declined your offer to be store Manager in store " + storeFacade.getStoreName(storeId));

    }

    @Transactional
    public void approveStoreManagerInvitation(String userId,  String storeId) throws Exception {
        String memberId = verifyToken(userId);
        verifyMemberExist(memberId);
        storeFacade.errorIfStoreNotExist(storeId);
        String proposerMemberId = roleFacade.approveInvitationStoreManager(memberId, storeId);
        myWebSocketHandler.handleStringMessage(proposerMemberId,
                userFacade.getMemberName(memberId)  + " approved your offer to be store Manager in store " + storeFacade.getStoreName(storeId));
    }

    @Transactional
    public void appointStoreOwner(String nominatorUserId, String nominatedUsername, String storeId) throws Exception {
        userFacade.errorIfUserNotExist(nominatorUserId);
        String nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
        verifyMemberExist(nominatorMemberID);
        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.verifyStoreOwnerError(storeId, nominatorMemberID);
        userFacade.errorIfUsernameNotFound(nominatedUsername);
        String nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
        roleFacade.addOwnerNominator(nominatedMemberID, storeId, false, nominatorMemberID);
        myWebSocketHandler.handleStringMessage(nominatedMemberID,
                "you have new job proposal from " +userFacade.getMemberName(nominatorMemberID)+  " to be store owner in store " + storeFacade.getStoreName(storeId) + " please accept or decline the proposal in the jobs page");
    }

    @Transactional
    public void fireStoreOwner(String nominatorUserId, String nominatedUsername, String storeId) throws Exception {
        userFacade.errorIfUserNotExist(nominatorUserId);
        String nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
        verifyMemberExist(nominatorMemberID);
        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.verifyStoreOwnerError(storeId, nominatorMemberID);
        userFacade.errorIfUsernameNotFound(nominatedUsername);
        String nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
        roleFacade.verifyOwnerNominatorError(nominatorMemberID, nominatedMemberID, storeId);
        roleFacade.fireStoreOwner(nominatedMemberID, storeId);

        myWebSocketHandler.handleStringMessage(nominatedMemberID , "you fired as store owner of store -by " + userFacade.getMemberName(nominatorMemberID));
    }

    @Transactional
    public void appointStoreManager(String nominatorUserId, String nominatedUsername, String storeId,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        userFacade.errorIfUserNotExist(nominatorUserId);
        String nominatorMemberID = verifyToken(nominatorUserId);
        userFacade.errorIfUserNotMember(nominatorUserId);
        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.verifyStoreOwnerError(storeId, nominatorMemberID);
        userFacade.errorIfUsernameNotFound(nominatedUsername);
        String nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
        roleFacade.addManagerNominator(nominatedMemberID, storeId, inventoryPermissions, purchasePermissions, nominatorMemberID);
        myWebSocketHandler.handleStringMessage(nominatedMemberID,
                "you have new job proposal from " +userFacade.getMemberName(nominatorMemberID)+  " to be store manager in store " + storeFacade.getStoreName(storeId) + " please accept or decline the proposal in the jobs page");
    }

    @Transactional
    public void fireStoreManager(String nominatorUserId, String nominatedUsername, String storeId) throws Exception {
        userFacade.errorIfUserNotExist(nominatorUserId);
        String nominatorMemberID = verifyToken(nominatorUserId);
        userFacade.errorIfUserNotMember(nominatorUserId);
        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.verifyStoreOwnerError(storeId, nominatorMemberID);
        userFacade.errorIfUsernameNotFound(nominatedUsername);
        String nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
        roleFacade.verifyMangerNominatorError(nominatorMemberID, nominatedMemberID, storeId);
        roleFacade.fireStoreManager(nominatedMemberID, storeId);
        myWebSocketHandler.handleStringMessage(nominatedMemberID , "you fired as store owner of store -by " + userFacade.getMemberName(nominatorMemberID));
    }

    @Transactional
    public void updateStoreManagerPermissions(String nominatorUserId, String nominatedUsername, String storeId,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        userFacade.errorIfUserNotExist(nominatorUserId);
        String nominatorMemberID = verifyToken(nominatorUserId);
        verifyMemberExist(nominatorMemberID);
        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.verifyStoreOwnerError(storeId, nominatorMemberID);
        userFacade.errorIfUsernameNotFound(nominatedUsername);
        String nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
        if (!roleFacade.verifyStoreOwner( storeId, nominatorMemberID)){
            throw new Exception(ExceptionsEnum.userIsNotStoreOwner.toString());
        };
        roleFacade.updateStoreManagerPermissions(nominatedMemberID, storeId, inventoryPermissions, purchasePermissions, nominatorMemberID);
    }

    @Transactional
    public List<ProductDTO> getStoreProducts(String storeId) throws Exception {
        return storeFacade.getStoreProductsDTO(storeId);
    }

    @Transactional
    public List<UserDTO> getStoreWorkers(String storeId){
        List<UserDTO> workers = new ArrayList<>();
        List<String> managersIdList = roleFacade.getAllStoreManagers(storeId);
        workers.addAll(userFacade.getUserDTOByMemberId(managersIdList));
        List<String> ownersIdList2 = roleFacade.getAllStoreOwners(storeId);
        workers.addAll(userFacade.getUserDTOByMemberId(ownersIdList2));
        return workers;

    }

    @Transactional
    public List<UserDTO> getStoreManagersDTO(String storeId){
        List<UserDTO> managers = new ArrayList<>();
        List<String> managersIdList = roleFacade.getAllStoreManagers(storeId);
        managers.addAll(userFacade.getUserDTOByMemberId(managersIdList));

        return managers;

    }

    @Transactional
    public List<UserDTO> getStoreOwnersDTO(String storeId){
        List<UserDTO> owners = new ArrayList<>();
        List<String> ownersIdList = roleFacade.getAllStoreOwners(storeId);
        owners.addAll(userFacade.getUserDTOByMemberId(ownersIdList));
        return owners;

    }

    @Transactional
    public List<String> getStoreManagers(String storeId){

        return roleFacade.getAllStoreManagers(storeId);
    }

    @Transactional
    public List<String> getStoreOwners(String storeId){

        return roleFacade.getAllStoreOwners(storeId);

    }

    @Transactional
    public void closeStore(String user_ID, String store_ID) throws Exception
    {
        String memberId = verifyToken(user_ID);
        verifyMemberExist(memberId);
        userFacade.isUserLoggedInError(user_ID);
        //String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        roleFacade.verifyStoreOwnerError(store_ID, memberId);
        roleFacade.verifyStoreOwnerIsFounder(store_ID, memberId);
        storeFacade.verifyStoreExistError(store_ID);
        storeFacade.closeStore(store_ID);
        List<String> storeManagers = roleFacade.getAllStoreManagers(store_ID);
        List<String> storeOwners = roleFacade.getAllStoreOwners(store_ID);
        //todo: add function which send notification to all store roles (notification component).
        String message = "store " + storeFacade.getStoreName(store_ID) + " closed by " + userFacade.getMemberName(memberId);
        for (String currMemId : storeManagers) {
            myWebSocketHandler.handleStringMessage(currMemId, message);
        }
        for (String currMemId : storeOwners) {
            myWebSocketHandler.handleStringMessage(currMemId, message);
        }
       /* String storeName = storeFacade.getStoreByID(store_ID).getStoreName();

        Notification n =new StoreNotification(storeName,"The store is now inactive");
        sendMessageToStaffOfStore(n,member_ID);*/
        //todo: update use-case parameters

    }

    @Transactional
    public void reopenStore(String user_ID, String store_ID) throws Exception
    {
        String memberId = verifyToken(user_ID);
        verifyMemberExist(memberId);
        userFacade.isUserLoggedInError(user_ID);
        //String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        roleFacade.verifyStoreOwnerError(store_ID, memberId);
        roleFacade.verifyStoreOwnerIsFounder(store_ID, memberId);
        storeFacade.verifyStoreExistError(store_ID);
        storeFacade.reopenStore(store_ID);

        sendMessagesToOwnersAndManagers(store_ID , "your store - " + storeFacade.getStoreName(store_ID) +" has been reopened");

    }


    @Transactional
    public Map<String, String> getInformationAboutRolesInStore(String user_ID, String store_ID) throws Exception {
        String memberId = verifyToken(user_ID);
        verifyMemberExist(memberId);
        Map<String, String> information = null;
        //userFacade.isUserLoggedInError(user_ID);
        //String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        storeFacade.verifyStoreExistError(store_ID);
        roleFacade.verifyStoreOwnerError(store_ID, memberId);
        information = roleFacade.getInformationAboutStoreRoles(store_ID);

        return information;
    }

    @Transactional
    public Map<String, List<Integer>> getAuthorizationsOfManagersInStore(String user_ID, String store_ID) throws Exception {

        String memberId = verifyToken(user_ID);
        verifyMemberExist(memberId);
        Map<String, List<Integer>> managersAuthorizations;

        //userFacade.isUserLoggedInError(user_ID);

        //String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        storeFacade.verifyStoreExistError(store_ID);
        roleFacade.verifyStoreOwnerError(store_ID, memberId);
        managersAuthorizations = roleFacade.getStoreManagersAuthorizations(store_ID);


        return managersAuthorizations;
    }

    @Transactional
    public Map<String, Map<String, List<Integer>>> getPurchaseList(String userId)throws Exception{
        verifyToken(userId);
        Map<String, Map<String, List<Integer>>> purchaseList = new HashMap<>();
        List<String> usersStores = userFacade.getCartStoresByUser(userId);
        for (String storeId: usersStores) {
            Map<String, List<Integer>> productAndQuantity = new HashMap<>();
            purchaseList.put(storeId, productAndQuantity) ;
            Map <String, List<Integer>> returnedMap = userFacade.getCartProductsByStoreAndUser(storeId , userId);
            for (String productName : returnedMap.keySet()){
                productAndQuantity.put(productName,returnedMap.get(productName));
            }

        }
        return purchaseList;
    }

    @Transactional
    public List<String> getInformationAboutStores(String user_ID) throws Exception {
        verifyToken(user_ID);
        List<String> openedStores = storeFacade.getInformationAboutOpenStores(); // open stores available for everyone
        List<String> closedStores = storeFacade.getInformationAboutClosedStores(); //closed stores available only for owners/ system managers
        List<String> closedStoreAvailable = null;

        if(userFacade.isMember(user_ID)) {
            String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
            if (!roleFacade.verifyMemberIsSystemManager(user_ID))
                closedStoreAvailable = roleFacade.getStoresByOwner(closedStores, member_ID);
            else
                closedStoreAvailable = closedStores; //all stores are available for system managers
        }

        List<String> allAvailableStores = new ArrayList<>(openedStores);
        if (closedStoreAvailable != null) {
            allAvailableStores.addAll(closedStoreAvailable);
        }

        return allAvailableStores;
    }

    @Transactional
    public List<String> getStoreCategories(){
        return storeFacade.getStoreCategories();
    }

    @Transactional
    public void modifyShoppingCart(String productName, int quantity, String storeId, String userId)throws Exception
    {
        verifyToken(userId);
        if (quantity == 0)
            removeProductFromBasket(productName, storeId, userId);
        else
        {
            userFacade.checkIfCanRemove(productName, storeId, userId);
            storeFacade.checkQuantityAndPrice(productName, quantity, storeId);
            Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(storeId, userId);
            int totalPrice = storeFacade.calcPrice(productName, quantity, storeId, userId);
            products.put(productName, new ArrayList<>(Arrays.asList(quantity, totalPrice)));
            List<ProductDTO> productDTOS = storeFacade.getProductsDTOSByProductsNames(products, storeId);
            UserDTO user = new UserDTO(userFacade.getUserByID(userId));
            storeFacade.checkPurchasePolicy(user, productDTOS, storeId);
            userFacade.modifyBasketProduct(productName, quantity, storeId, userId, totalPrice);
        }
    }

    @Transactional
    public Map<String, Integer> marketManagerAskInfo(String user_ID)throws Exception
    {
        String memberId = verifyToken(user_ID);
        verifyMemberExist(memberId);
        //userFacade.isUserLoggedInError(user_ID);
        if (!roleFacade.verifyMemberIsSystemManager(memberId)) {
            throw new IllegalArgumentException(ExceptionsEnum.notSystemManager.toString());
        }
        return paymentServicesFacade.getStorePurchaseInfo();
    }


    @Transactional
    public Map<String, Integer> storeOwnerGetInfoAboutStore(String user_ID, String store_ID) throws Exception //return receiptId and total amount in the receipt for the specific store
    {
        String memberId = verifyToken(user_ID);
        verifyMemberExist(memberId);
        Map<String, Integer> storeReceiptsAndTotalAmount;
        //userFacade.isUserLoggedInError(user_ID);
        //String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        roleFacade.verifyStoreOwnerError(store_ID, memberId);
        storeFacade.verifyStoreExistError(store_ID);
        storeReceiptsAndTotalAmount = paymentServicesFacade.getStoreReceiptsAndTotalAmount(store_ID);

        if (storeReceiptsAndTotalAmount.isEmpty())
            throw new IllegalArgumentException("There are no purchases in the store");
        return storeReceiptsAndTotalAmount;
    }

//    public int checkingCartValidationBeforePurchase(String user_ID, UserDTO userDTO) throws Exception {
//        if (this.userFacade.isMember(user_ID)) {
//            String memberId = this.userFacade.getMemberIdByUserId(user_ID);
//            boolean succeeded = this.authenticationAndSecurityFacade.validateToken(this.authenticationAndSecurityFacade.getToken(memberId));
//            if (!succeeded) {
//                this.logout(user_ID);
//                throw new Exception(ExceptionsEnum.sessionOver.toString());
//            }
//        }
//
//        int totalPrice = 0;
//        this.userFacade.isUserCartEmpty(user_ID);
//        List<String> stores = this.userFacade.getCartStoresByUser(user_ID);
//
//        int storeTotalPrice;
//        for(Iterator storeIt = stores.iterator(); storeIt.hasNext(); totalPrice += storeTotalPrice) {
//            String store_ID = (String)storeIt.next();
//            Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(store_ID, user_ID);
//            List<ProductDTO> productDTOS = this.storeFacade.getProductsDTOSByProductsNames(products, store_ID);
//            Iterator productIt = products.keySet().iterator();
//
//            String productId;
//            while(productIt.hasNext()) {
//                productId = (String)productIt.next();
//                int quantity = (Integer)((List)products.get(productId)).get(0);
//                this.storeFacade.checkQuantityAndPrice(productId, quantity, store_ID);
//            }
//            this.storeFacade.checkPurchasePolicy(userDTO, productDTOS, store_ID);
//            int priceToReduce = this.storeFacade.calcDiscountPolicy(userDTO, productDTOS, store_ID);
//            int storeTotalPriceBeforeDiscount = this.userFacade.getCartPriceByUser(user_ID);
//            storeTotalPrice = storeTotalPriceBeforeDiscount - priceToReduce;
//        }
//
//        this.removeUserCartFromStock(user_ID);
//        return totalPrice;
//    }

    @Transactional
    public CartDTO checkingCartValidationBeforePurchaseDTO(String user_ID, UserDTO userDTO) throws Exception {
        verifyToken(user_ID);
        int totalPrice = 0;
        this.userFacade.isUserCartEmpty(user_ID);
        List<String> stores = this.userFacade.getCartStoresByUser(user_ID);

        int storeTotalPrice;
        for(Iterator storeIt = stores.iterator(); storeIt.hasNext(); totalPrice += storeTotalPrice) {
            String store_ID = (String)storeIt.next();
            Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(store_ID, user_ID);
            List<ProductDTO> productDTOS = this.storeFacade.getProductsDTOSByProductsNames(products, store_ID);
            Iterator productIt = products.keySet().iterator();

            String productId;
            while(productIt.hasNext()) {
                productId = (String)productIt.next();
                int quantity = (Integer)((List)products.get(productId)).get(0);
                this.storeFacade.checkQuantityAndPrice(productId, quantity, store_ID);
            }
            this.storeFacade.checkPurchasePolicy(userDTO, productDTOS, store_ID);
            int priceToReduce = this.storeFacade.calcDiscountPolicy(userDTO, productDTOS, store_ID);
            int storeTotalPriceBeforeDiscount = this.userFacade.getCartPriceByUser(user_ID);
            storeTotalPrice = storeTotalPriceBeforeDiscount - priceToReduce;
        }

        if(totalPrice < 0)
            totalPrice = 0;

        this.removeUserCartFromStock(user_ID);
        return new CartDTO(user_ID, totalPrice, this.getPurchaseList(user_ID));
    }

    @Transactional
    public String checkAvailableExternalSupplyService(String country, String city) throws Exception {
        String availibleExteranlSupplyService =this.supplyServicesFacade.checkAvailableExternalSupplyService(country,city);
        if("-1".equals(availibleExteranlSupplyService)) {
            throw new Exception(ExceptionsEnum.NoExternalSupplyService.toString());
        }
        if("-2".equals(availibleExteranlSupplyService)) {
            throw new Exception(ExceptionsEnum.ExternalSupplyServiceIsNotAvailableForArea.toString());
        }
        return availibleExteranlSupplyService;
    }

    @Transactional
    public void createShiftingDetails(String country, String city, String address, String user_ID, String acquisitionId) throws Exception
    {
        String memberId = this.userFacade.getMemberIdByUserId(user_ID);
        if (memberId== null)
        {
            memberId = "unregister user";
        }
        if(!supplyServicesFacade.createShiftingDetails( memberId,country,city,address, acquisitionId)){
            throw new Exception(ExceptionsEnum.createShiftingError.toString());
        }
    }

    @Transactional
    public void removeUserCartFromStock(String userId) throws Exception {
        verifyToken(userId);
        if (userFacade.getUserByID(userId) == null) {
            throw new Exception("User not found.");
        }
        if (userFacade.getUserByID(userId).getCart() == null || userFacade.getUserByID(userId).getCart().isCartEmpty()) {
            throw new Exception("User cart is empty, there's nothing to remove from stock.");
        }
        List<String> storeIds = userFacade.getUserByID(userId).getCart().getCartStores();
        for (String storeId : storeIds) {
            Map<String, Integer> products = userFacade.getUserByID(userId).getCart().getProductsQuantityByStore(storeId); //Map<productName, quantity> products
            for (Map.Entry<String, Integer> entry : products.entrySet()) {
                String productName = entry.getKey();
                int quantity = entry.getValue();
                storeFacade.getStoreByID(storeId).removeProductQuantity(productName,quantity);
            }
        }
    }

    @Transactional
    public List<String> inStoreProductSearch(String userId, String productName, String categoryStr, List<String> keywords, String storeId) throws Exception {
        verifyToken(userId);
        storeFacade.checkCategory(categoryStr);
        storeFacade.verifyStoreExistError(storeId);
        return storeFacade.inStoreProductSearch(productName, categoryStr, keywords, storeId);
    }

    @Transactional
    public List<ProductDTO> inStoreProductSearchDTO(String userId, String productName, String categoryStr, List<String> keywords, String storeId) throws Exception {
        verifyToken(userId);
        storeFacade.checkCategory(categoryStr);
        storeFacade.verifyStoreExistError(storeId);
        return storeFacade.inStoreProductSearchDTO(productName, categoryStr, keywords, storeId);
    }

    @Transactional
    public List<String> generalProductSearch(String userId, String productName, String categoryStr, List<String> keywords) throws Exception {
        verifyToken(userId);

        storeFacade.checkCategory(categoryStr);

        List<String> filteredProductNames = new ArrayList<>();
        List<String> stores = this.storeFacade.getStores();
        for(String store_ID: stores)
        {
            try {
                filteredProductNames.addAll(inStoreProductSearch(userId, productName, categoryStr, keywords,store_ID));
            }
            catch (Exception e)
            {
                continue;
            }
        }
        return filteredProductNames;
    }

    @Transactional
    public Map<String, List<ProductDTO>> generalProductSearchDTO(String userId, String productName, String categoryStr, List<String> keywords) throws Exception {
        verifyToken(userId);
        storeFacade.checkCategory(categoryStr);

        Map<String, List<ProductDTO>> filteredProductNames = new HashMap<>();
        List<String> stores = this.storeFacade.getStores();
        for(String store_ID: stores)
        {
            try {

                filteredProductNames.put(store_ID, inStoreProductSearchDTO(userId, productName, categoryStr, keywords,store_ID));
            }
            catch (Exception e)
            {
                continue;
            }
        }
        return filteredProductNames;
    }

    @Transactional
    public List<String> inStoreProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, String storeId, List<String> productsFromSearch, Double storeMinRating)throws Exception {
        verifyToken(userId);
        checkPrice(minPrice, maxPrice);
        checkProductRating(productMinRating);
        checkStoreRating(storeMinRating);
        storeFacade.checkCategory(categoryStr);
        storeFacade.verifyStoreExistError(storeId);
        List<String> filteredProductNames = storeFacade.inStoreProductFilter(categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);

        return filteredProductNames;
    }

    @Transactional
    public List<String> generalProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating) throws Exception {
        verifyToken(userId);
        checkPrice(minPrice, maxPrice);
        checkProductRating(productMinRating);
        checkStoreRating(storeMinRating);
        storeFacade.checkCategory(categoryStr);

        List<String> filteredProductNames = new ArrayList<>();

        List<String > stores = this.storeFacade.getStores();
        for(String store_ID: stores)
        {
            try{
                filteredProductNames.addAll(inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, store_ID, productsFromSearch, storeMinRating));
            }catch (Exception e)
            {
                continue;
            }
        }
        return filteredProductNames;
    }

    @Transactional
    public boolean isInitialized() {
        synchronized (initializedLock) {
            return Objects.requireNonNull(initializedRepository.findById("market").orElse(null)).isInitialized();
        }

    }



//
    @Transactional
    protected void checkPrice(Integer minPrice, Integer maxPrice)
    {
        if (!(minPrice == null || maxPrice == null) && !(minPrice != null && maxPrice != null && minPrice <= maxPrice))
            throw new IllegalArgumentException(ExceptionsEnum.priceRangeInvalid.toString());
    }

    @Transactional
    protected void checkProductRating(Double productMinRating)
    {

        if (productMinRating != null && (productMinRating < 0 || productMinRating > 5))
            throw new IllegalArgumentException(ExceptionsEnum.productRateInvalid.toString());
    }

    @Transactional
    protected void checkStoreRating(Double storeMinRating)
    {
        if (storeMinRating != null && (storeMinRating < 0 || storeMinRating > 5))
            throw new IllegalArgumentException(ExceptionsEnum.storeRateInvalid.toString());
    }

    @Transactional
    public void addPurchaseRuleToStore(List<TestRuleDTO> testRules, List<String> operators, String userId, String storeId) throws Exception {
        verifyToken(userId);
        for (TestRuleDTO curr : testRules){
            if (curr.isContains()==null){
                curr.setContains(false);
            }
        }

        checkLogicalRulesAndOperators(testRules, operators);
        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        //TODO: check testrules
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.addPurchaseRuleToStore(testRules, operators, storeId);
    }

    @Transactional
    public void removePurchaseRuleFromStore(int ruleNum,  String userId, String storeId) throws Exception {
        String member_ID = verifyToken(userId);
        verifyMemberExist(member_ID);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.removePurchaseRuleFromStore(ruleNum, storeId);
    }

    @Transactional
    public void addDiscountCondRuleToStore(List<TestRuleDTO> testRules, List<String> logicOperators, List<DiscountValueDTO> discDetails, List<String> numericalOperators, String userId ,String storeId) throws Exception {
        String memberId = verifyToken(userId);
        verifyMemberExist(memberId);
        checkLogicalRulesAndOperators(testRules, logicOperators);
        checkNumericalRulesAndOperators(discDetails, numericalOperators);
        checkProductDiscountDetails(discDetails);
        //String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, memberId);
        storeFacade.addDiscountCondRuleToStore(testRules, logicOperators, discDetails, numericalOperators, storeId);
    }

    @Transactional
    public void addDiscountSimpleRuleToStore(List<DiscountValueDTO> discDetails, List<String> numericalOperators, String userId ,String storeId) throws Exception {
        String member_ID = verifyToken(userId);
        verifyMemberExist(member_ID);
        checkNumericalRulesAndOperators(discDetails, numericalOperators);
        checkProductDiscountDetails(discDetails);
        //String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.addDiscountSimpleRuleToStore(discDetails, numericalOperators, storeId);
    }

    @Transactional
    public void removeDiscountRuleFromStore(int ruleNum, String userId ,String storeId) throws Exception {
        String member_ID = verifyToken(userId);
        verifyMemberExist(member_ID);
        //String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.removeDiscountRuleFromStore(ruleNum, storeId);
    }

    @Transactional
    protected void checkProductDiscountDetails(List<DiscountValueDTO> discDetails) {
        for (DiscountValueDTO discountValueDTO : discDetails) {
            if (discountValueDTO.getPercentage() < 0 || discountValueDTO.getPercentage() > 100) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidDiscountValueParameters.toString());
            }
            int count = 0;
            if (discountValueDTO.getCategory() != null) {
                count++;
            }
            if (discountValueDTO.getIsStoreDiscount()) {
                count++;
            }
            if (discountValueDTO.getProductsNames() != null) {
                count++;
            }
            if (count != 1) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidDiscountValueParameters.toString());
            }
        }
    }

    @Transactional
    protected void checkLogicalRulesAndOperators(List<TestRuleDTO> ruleNums, List<String> operators) throws Exception {
        if (ruleNums.size() != operators.size() + 1) {
            throw new IllegalArgumentException(ExceptionsEnum.rulesNotMatchOpeators.toString());
        }
        for (int i = 0; i < operators.size(); i++) {
            if (!operators.get(i).equals("AND") && !operators.get(i).equals("OR") && !operators.get(i).equals("ONLY IF") && !operators.get(i).equals("XOR")) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
            }
        }
    }

    @Transactional
    protected void checkNumericalRulesAndOperators(List<DiscountValueDTO> discDetails, List<String> numericalOperators) throws Exception {
        if (discDetails.size() != numericalOperators.size() + 1) {
            throw new IllegalArgumentException(ExceptionsEnum.rulesNotMatchOpeators.toString());
        }
        for (int i = 0; i < numericalOperators.size(); i++) {
            if (!numericalOperators.get(i).equals("MAX") && !numericalOperators.get(i).equals("ADDITION")) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
            }
        }
    }

    @Transactional
    public List<String> getStoreCurrentPurchaseRules(String userId, String storeId) throws Exception {
        String member_ID = verifyToken(userId);
        verifyMemberExist(member_ID);
        //String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        return storeFacade.getStoreCurrentPurchaseRules(storeId);
    }


    @Transactional
    public List<String> getStoreCurrentDiscountRules(String userId, String storeId) throws Exception {
        String member_ID = verifyToken(userId);
        verifyMemberExist(member_ID);
        //String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        return storeFacade.getStoreCurrentDiscountRules(storeId);
    }

    @Transactional
    public List<AcquisitionDTO> getUserAcquisitionsHistory(String userId) throws Exception {
        verifyToken(userId);
        List<Acquisition> acquisitions;
        if (userFacade.isMember(userId)){
            String memberID = userFacade.getMemberIdByUserId(userId);
            acquisitions = paymentServicesFacade.getMemberAcquisitionsHistory(memberID);
        }
        else {
            throw new IllegalArgumentException("only members can see there purchase  history");
        }
        //List<Acquisition> acquisitions = paymentServicesFacade.getUserAcquisitionsHistory(userId);
        //List<String> acquisitions = userFacade.getUserAcquisitionsHistory(userId);
        return paymentServicesFacade.getAcquisitionsDTO(acquisitions);
    }

    @Transactional
    public Map<String, ReceiptDTO> getUserReceiptsByAcquisition(String userId, String acquisitionId) throws Exception {
        String memberId = verifyToken(userId);
        verifyMemberExist(memberId);
        //check if user has the acquisition
        //userFacade.checkIfUserHasAcquisition(userId, acquisitionId);
        //return paymentServicesFacade.getReceiptsDTOByAcquisition(acquisitionId);
        Map<String,ReceiptDTO> receiptDTOMap = new HashMap<>();
        List<Receipt> receipts = paymentServicesFacade.getUserReceiptsByAcquisition(acquisitionId,memberId);
        for (Receipt receipt : receipts) {
            receiptDTOMap.put(receipt.getReceiptId(),paymentServicesFacade.getReceiptDTOFromReceipt(receipt));
        }
        return receiptDTOMap;
    }

    @Transactional
    public void composeCurrentPurchaseRules(int ruleIndex1, int ruleIndex2, String operator, String userId, String storeId) throws Exception {
        String member_ID =verifyToken(userId);

        if (!operator.equals("AND") && !operator.equals("OR") && !operator.equals("ONLY IF")) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
        }
        verifyMemberExist(member_ID);
        // this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.composeCurrentPurchaseRules(ruleIndex1, ruleIndex2, operator, storeId);
    }

    @Transactional
    public void composeCurrentSimpleDiscountRules(int ruleIndex1, int ruleIndex2, String numericalOperator, String userId, String storeId) throws Exception {
        String member_ID = verifyToken(userId);
        if (!numericalOperator.equals("MAX") && !numericalOperator.equals("ADDITION")) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
        }
        verifyMemberExist(member_ID);
        //String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.composeCurrentSimpleDiscountRules(ruleIndex1, ruleIndex2, numericalOperator, storeId);
    }

    @Transactional
    public void composeCurrentCondDiscountRules(int ruleIndex1, int ruleIndex2, String logicalOperator, String numericalOperator, String userId, String storeId) throws Exception {
        String member_ID = verifyToken(userId);

        if (!logicalOperator.equals("AND") && !logicalOperator.equals("OR") && !logicalOperator.equals("XOR")) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
        }

        if (!numericalOperator.equals("MAX") && !numericalOperator.equals("ADDITION")) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
        }
        verifyMemberExist(member_ID);
        //String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.composeCurrentCondDiscountRules(ruleIndex1, ruleIndex2, logicalOperator, numericalOperator, storeId);
    }

    @Transactional
    public List<String> getStoreCurrentSimpleDiscountRules(String userId, String storeId) throws Exception {
        String member_ID = verifyToken(userId);
        verifyMemberExist(member_ID);
        //String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        return storeFacade.getStoreCurrentSimpleDiscountRules(storeId);
    }

    @Transactional
    public List<String> getStoreCurrentCondDiscountRules(String userId, String storeId) throws Exception {
        String member_ID =verifyToken(userId);
        verifyMemberExist(member_ID);
        //String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        return storeFacade.getStoreCurrentCondDiscountRules(storeId);
    }

    @Transactional
    public void checkSystemInitialized() throws Exception {
        if (!isInitialized()){
            throw new Exception("system must initialized first");
        }
    }

    @Transactional
    protected String verifyToken(String userId) throws Exception {
        String memberId= null;
        if (userFacade.isMember(userId)){
            memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        return memberId;
    }

    @Transactional
    public void verifyMemberExist(String memberId ){
        if (memberId== null){
            throw new IllegalArgumentException(ExceptionsEnum.userIsNotMember.toString());
        }
    }

    public UserFacade getUserFacade() {
        return userFacade;
    }


    @Transactional
    public String init(String configFilePath ) throws Exception {

        logger.info("Starting the initialization of the system.");
        Map<String, Object> adminConfig = null; // Declare adminConfig here
        //String configFilePath = "src/main/resources/ConfigurationFile.yaml";
        Map<String, Object> initConfig = loadAdminConfiguration(configFilePath);
        Map<String, Object> paymentConfig  = (Map<String, Object>) initConfig.get("paymentService");
        String paymentURL = (String) paymentConfig.get("url");
        Map<String, Object> serviceConfig  = (Map<String, Object>) initConfig.get("supplyService");
        String supplyURL = (String) serviceConfig.get("url");

        String adminPassword = "";

        synchronized (initializedLock) {
            if (Objects.requireNonNull(initializedRepository.findById("market").orElse(null)).isInitialized()) {
                logger.info("system already initialized");
                return "null";
            }
        }
        try {
            // Check for supply service
            if (supplyURL == null) {
                logger.error("system cannot be initialized, supply url problem");
                throw new IllegalArgumentException(ExceptionsEnum.InvalidSupplyServiceDetails.toString());
            }
            // Check for payment service
            if (paymentURL == null) {
                logger.error("system cannot be initialized, payment url problem");
                throw new IllegalArgumentException(ExceptionsEnum.InvalidPaymentServiceDetails.toString());
            }


            adminConfig = (Map<String, Object>) initConfig.get("admin");
            adminPassword = (String) adminConfig.get("password");
            validateAdminPassword1(adminPassword);

        } catch (Exception e) {
            throw e; // Re-throw the exception to be handled by the caller
        }


        // Extract admin configuration values
        String adminUsername = (String) adminConfig.get("username");
        String adminBirthday = (String) adminConfig.get("birthday");
        String adminCountry = (String) adminConfig.get("country");
        String adminCity = (String) adminConfig.get("city");
        String adminAddress = (String) adminConfig.get("address");
        String adminName = (String) adminConfig.get("name");


        logger.info("try register system admin");
        // Encode admin password
        String encryptedPassword = authenticationAndSecurityFacade.encodePassword(adminPassword);

        // Register user and retrieve system manager ID
        String firstUserID = enterMarketSystem();
        UserDTO userDTO1 = new UserDTO(firstUserID, adminUsername, adminBirthday, adminCountry, adminCity, adminAddress, adminName);
        String systemManagerId = userFacade.register(firstUserID, userDTO1, encryptedPassword);
        roleFacade.addSystemManager(systemManagerId);
//        synchronized (managersLock) {
//            systemManagerIds.add(systemManagerId);
//        }
        if (!paymentServicesFacade.addExternalService(paymentURL)){
            throw new Exception("problem while adding external payment service");
        };
        if (!supplyServicesFacade.addExternalService(supplyURL)){
            throw new Exception("problem while adding external supply service");
        };
        synchronized (initializedLock) {
            try {
                initializedRepository.save(new InitializedStatus(true));
            }
            catch (Exception e) {
                throw new Exception(ExceptionsEnum.DatabaseIsNotConnected.toString());
            }
            logger.info("system initialized successfully");
        }

        return firstUserID; // Return the generated user ID
    }

    @Transactional
    public void addSystemManagerForTest(String memberId){
        roleFacade.addSystemManager(memberId);
    }

    public RoleFacade getRoleFacade() {
        return roleFacade;
    }

    public StoreFacade getStoreFacade() {
        return storeFacade;
    }

    public PaymentServicesFacade getPaymentServiceFacade(){
        return paymentServicesFacade;
    }
    public AuthenticationAndSecurityFacade getAuthenticationAndSecurityFacade(){
        return authenticationAndSecurityFacade;
    }


    public MyWebSocketHandler getMyWebSocketHandler() {
        return myWebSocketHandler;
    }

    @Transactional
    public void resetAllTables() {
        storeFacade.reset();
        userFacade.reset();
        roleFacade.reset();
        paymentServicesFacade.reset();
        supplyServicesFacade.reset();
        initializedRepository.deleteAll();

    }
}
