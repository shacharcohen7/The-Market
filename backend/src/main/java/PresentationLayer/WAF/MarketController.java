package PresentationLayer.WAF;

import DomainLayer.PaymentServices.Receipt;
import ServiceLayer.Response;
import Util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.*;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    //private final MarketService marketService;
    private final ObjectMapper objectMapper;
    private final Service_layer serviceLayer;

    @Autowired
    public MarketController(Service_layer serviceLayer) {
        //this.marketService = marketService;
        this.serviceLayer = serviceLayer;
        this.objectMapper =new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @GetMapping("/checkInitializedMarket")
    public ResponseEntity<APIResponse<Boolean>> checkInitializedMarket() {
        try {
            Response<Boolean> response = serviceLayer.checkInitializedMarket();
            if (response.isSuccess()) {
                Boolean res = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<Boolean>(res, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<Boolean>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/getUserNotifications/{memberId}")
    public ResponseEntity<APIResponse<List<String>>> getUserNotifications(@PathVariable String memberId) {
        try {
            Response<List<String>> response = serviceLayer.getUserNotifications(memberId);
            if (response.isSuccess()) {
                List<String> res = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<>(res, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }


    @GetMapping("/enterSystem")
    public ResponseEntity<APIResponse<String>> enterMarket() {
        try {
            Response<String> response = serviceLayer.enterMarketSystem();
            if (response.isSuccess()) {
                String userId = response.getData();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(userId, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/initiate")
    public ResponseEntity<APIResponse<String>> initiate(@RequestParam Map<String,String> params) {
        try {
//            String userDTO = params.get("userDTO");
//            String password = params.get("password");
//            String paymentServiceDTO = params.get("paymentServiceDTO");
//            //String supplyServiceDTO = params.get("supplyServiceDTO");
//            String supplyDealerNumberField = params.get("supplyDealerNumberField");
//            String supplyServiceName = params.get("supplyServiceName");
//            String countriesSet = params.get("countriesSet");
//            String citiesSet = params.get("citiesSet");
//            Set<String> coutries = new HashSet();
//            coutries.add(countriesSet);
//            Set<String> cities = new HashSet();
//            cities.add(citiesSet);
            Response<String> response = serviceLayer.init();
            if (response.isSuccess()) {
                String userId = response.getData();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(userId, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }


    @PostMapping("/addDiscountCondRuleToStore")
    public ResponseEntity<APIResponse<String>> addDiscountCondRuleToStore(@RequestParam Map<String,String> params) {
        try {

            String testRules = params.get("testRules");
            String logicOperators = params.get("logicOperators");
            String discDetails = params.get("discDetails");
            String numericalOperators = params.get("numericalOperators");
            String userId = params.get("userId");
            String storeId = params.get("storeId");
            List<TestRuleDTO> rules= null;
            //List<TestRuleDTO> logicOperators= null;
            TestRuleDTO[] dtosRule = null;
            List<String> stringRules = objectMapper.readValue(testRules, new TypeReference<List<String>>() {});
            // Deserialize each JSON string into a TestRuleDTO object
            rules = new ArrayList<>();
            for (String stringRule : stringRules) {
                TestRuleDTO rule = objectMapper.readValue(stringRule, TestRuleDTO.class);
                rules.add(rule);
            }
            List<String> logicOperatorsList = objectMapper.readValue(logicOperators, new TypeReference<List<String>>() {});
            List<DiscountValueDTO> discsList = null;
            List<String> stringDiscs = objectMapper.readValue(discDetails, new TypeReference<List<String>>() {});
            // Deserialize each JSON string into a TestRuleDTO object
            discsList = new ArrayList<>();
            for (String stringDisc : stringDiscs) {
                DiscountValueDTO rule = objectMapper.readValue(stringDisc, DiscountValueDTO.class);
                discsList.add(rule);
            }
            List<String> numericOperatorsList = objectMapper.readValue(numericalOperators, new TypeReference<List<String>>() {});
            Response<String> response = serviceLayer.addDiscountCondRuleToStore(rules, logicOperatorsList, discsList, numericOperatorsList, userId, storeId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/addDiscountSimpleRuleToStore")
    public ResponseEntity<APIResponse<String>> addDiscountSimpleRuleToStore(@RequestParam Map<String,String> params) {
        try {
            String discs = params.get("discs");
            String numericalOperators = params.get("numericalOperators");
            String userId = params.get("userId");
            String storeId = params.get("storeId");
//            List<DiscountValueDTO> discDetailsList = objectMapper.readValue(discs, objectMapper.getTypeFactory().constructCollectionType(List.class, DiscountValueDTO.class));
//            List<String> numericalOperatorsList = objectMapper.readValue(numericalOperators, objectMapper.getTypeFactory().constructCollectionType(List.class, String.class));
//            DiscountValueDTO[] dtosDiscs = null;
            List<DiscountValueDTO> discsList = null;
            List<String> stringDiscs = objectMapper.readValue(discs, new TypeReference<List<String>>() {});
            // Deserialize each JSON string into a TestRuleDTO object
            discsList = new ArrayList<>();
            for (String stringDisc : stringDiscs) {
                DiscountValueDTO rule = objectMapper.readValue(stringDisc, DiscountValueDTO.class);
                discsList.add(rule);
            }
            List<String> numericOperatorsList = objectMapper.readValue(numericalOperators, new TypeReference<List<String>>() {});

            Response<String> response = serviceLayer.addDiscountSimpleRuleToStore(discsList, numericOperatorsList, userId, storeId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }


    @PostMapping("/addExternalPaymentService")
    public ResponseEntity<APIResponse<String>> addExternalPaymentService(@RequestParam Map<String,String> params) {
        try {
            String paymentUrl =params.get("paymentServiceDTO");
            String systemMangerUserId = params.get("systemMangerUserId");
            Response<String> response = serviceLayer.addExternalPaymentService( paymentUrl, systemMangerUserId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @DeleteMapping("/removeExternalPaymentService")
    public ResponseEntity<APIResponse<String>> removeExternalPaymentService(@RequestParam Map<String,String> params) {
        try {
            String paymentUrl =params.get("url");
            String systemMangerUserId = params.get("systemMangerUserId");
            String decodedUrl = URLDecoder.decode(paymentUrl, "UTF-8");
            Response<String> response = serviceLayer.removeExternalPaymentService(decodedUrl, systemMangerUserId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/addExternalSupplyService")
    public ResponseEntity<APIResponse<String>> addExternalSupplyService(@RequestParam Map<String,String> params) {
        try {
            String supplyURL = params.get("supplyServiceDTO");
            String systemMangerUserId = params.get("systemMangerUserId");
            Response<String> response = serviceLayer.addExternalSupplyService(supplyURL, systemMangerUserId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @DeleteMapping("/removeExternalSupplyService")
    public ResponseEntity<APIResponse<String>> removeExternalSupplyService(@RequestParam Map<String,String> params) {
        try {
            String paymentUrl =params.get("url");
            String systemMangerUserId = params.get("systemMangerUserId");
            String decodedUrl = URLDecoder.decode(paymentUrl, "UTF-8");
            Response<String> response = serviceLayer.removeExternalSupplyService(decodedUrl, systemMangerUserId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<APIResponse<String>> purchase(@RequestBody Map<String,String> params) {
        try {
            String userDTO = params.get("userDTO");
            String paymentDTO= params.get("paymentDTO");
            String cartDTO = params.get("cartDTO");
            Response<String> response = serviceLayer.purchase(objectMapper.readValue(userDTO, UserDTO.class),objectMapper.readValue( paymentDTO, PaymentDTO.class), objectMapper.readValue( cartDTO, CartDTO.class));
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/exitMarketSystem/{userId}")
    public ResponseEntity<APIResponse<String>> purchase(@PathVariable String userId) {
        try {
            Response<String> response = serviceLayer.exitMarketSystem(userId);
            if (response.isSuccess()) {
                String result = response.getData();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/getCartAfterValidation")
    public ResponseEntity<APIResponse<String>> getCartAfterValidation(@RequestParam Map<String,String> params) {
        try {
            String userId = params.get("userID");
            String userDTO = params.get("userDTO");
            ObjectMapper objectMapper= new ObjectMapper();
            Response<CartDTO> response = serviceLayer.checkingCartValidationBeforePurchaseDTO(userId, objectMapper.readValue(userDTO, UserDTO.class));
            if (response.isSuccess()) {
                String result = objectMapper.writeValueAsString(response.getResult());
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }


    @PostMapping("/register")
    //@PostMapping("/register")
    public ResponseEntity<APIResponse<String>> register(@RequestParam Map<String,String> params) {
        try{
            String userDTO = params.get("userDTO");
            String password = params.get("password");

            Response<String> response = serviceLayer.register(objectMapper.readValue(userDTO, UserDTO.class), password);
            if (response.isSuccess()) {
                String result = response.getData();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/getCategories")
    public ResponseEntity<APIResponse<List<String>>> getCategories() {
        try {
            Response<List<String>> response = serviceLayer.getStoreCategories();
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(response.getResult(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }


    @GetMapping("/getExternalSupplyServices")
    public ResponseEntity<APIResponse<List<String>>> getExternalSupplyServices() {
        try {
            Response<List<String>> response = serviceLayer.getAllSupplyServices();
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(response.getResult(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/getExternalPaymentServices")
    public ResponseEntity<APIResponse<List<String>>> getExternalPaymentServices() {
        try {
            Response<List<String>> response = serviceLayer.getAllPaymentServices();
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(response.getResult(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }



    @GetMapping("/getStoreProducts/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreProducts(@PathVariable String storeId) {
        try {
            ObjectMapper objectMapper= new ObjectMapper();
            Response<List<ProductDTO>> response = serviceLayer.getProductStores(storeId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            List<String> dtosRes = new ArrayList<>();
            for (ProductDTO productDTO : response.getResult() ){
                dtosRes.add(objectMapper.writeValueAsString(productDTO));
            }

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(dtosRes, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/getAllAcquisitions/{systemManagerUserId}")
    public ResponseEntity<APIResponse<List<String>>> getAllAcquisitions(@PathVariable String systemManagerUserId) {
        try {
            ObjectMapper objectMapper= new ObjectMapper();
            Response<List<AcquisitionDTO>> response = serviceLayer.getAllAcquisitions(systemManagerUserId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            List<String> dtosRes = new ArrayList<>();
            for (AcquisitionDTO acquisitionDTO : response.getResult() ){
                dtosRes.add(objectMapper.writeValueAsString(acquisitionDTO));
            }

            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(dtosRes, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/getManagerJobProposal/{userId}")
    public ResponseEntity<APIResponse<List<String>>> getManagerJobProposal(@PathVariable String userId) {
        try {
            ObjectMapper objectMapper= new ObjectMapper();
            Response<List<StoreManagerDTO>> response = serviceLayer.getManagerJobProposal(userId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            List<String> dtosRes = new ArrayList<>();
            for (StoreManagerDTO storeManagerDTO : response.getResult() ){
                dtosRes.add(objectMapper.writeValueAsString(storeManagerDTO));
            }
            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(dtosRes, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @GetMapping("/getOwnerJobProposal/{userId}")
    public ResponseEntity<APIResponse<List<String>>> getOwnerJobProposal(@PathVariable String userId) {
        try {
            ObjectMapper objectMapper= new ObjectMapper();
            Response<List<StoreOwnerDTO>> response = serviceLayer.getOwnerJobProposal(userId);
            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            List<String> dtosRes = new ArrayList<>();
            for (StoreOwnerDTO storeOwnerDTO : response.getResult() ){
                dtosRes.add(objectMapper.writeValueAsString(storeOwnerDTO));
            }
            return ResponseEntity.status(HttpStatus.OK).headers(headers)
                    .body(new APIResponse<>(dtosRes, null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }



    @PostMapping("/login/{userId}/{userName}/{password}")
    public ResponseEntity<APIResponse<String>> login(@PathVariable String userId, @PathVariable String userName, @PathVariable String password) {
        try {
            Response<String> response = serviceLayer.login(userId, userName, password);
            if (response.isSuccess()) {
                String result = response.getData();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<APIResponse<String>> logout(@PathVariable String userId) {
        try {
            Response<String> response = serviceLayer.logout(userId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }

    @PostMapping("/addProductToStore")
    public ResponseEntity<APIResponse<String>> addProductToStore(@RequestParam Map<String,String> params) {
        try {
            String userId = params.get("userId");
            String storeId = params.get("storeId");
            String productDTO = params.get("productDTO");
            Response<String> response = serviceLayer.addProductToStore(userId, storeId, objectMapper.readValue(productDTO, ProductDTO.class));
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }

    }


    @DeleteMapping("/removeProductFromStore/{userId}/{storeId}/{productId}")
    public ResponseEntity<APIResponse<String>> removeProductFromStore(@PathVariable String userId, @PathVariable String storeId, @PathVariable String productId) {

        Response<String> response = serviceLayer.removeProductFromStore(userId, storeId, productId);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/updateProductInStore")
    public ResponseEntity<APIResponse<String>> updateProductInStore(@RequestParam Map<String,String> params) {
        try {
            String userId = params.get("userId");
            String storeId= params.get("storeId");
            String productDTO= params.get("productDTO");

            Response<String> response = serviceLayer.updateProductInStore(userId, storeId, objectMapper.readValue(productDTO, ProductDTO.class));
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }




    @PostMapping("/appointStoreOwner/{userId}/{appointedId}/{storeId}")
    public ResponseEntity<APIResponse<String>> appointStoreOwner(@PathVariable String userId, @PathVariable String appointedId, @PathVariable String storeId) {

        Response<String> response = serviceLayer.appointStoreOwner(userId, appointedId, storeId);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/appointStoreManager/{userId}/{appointedId}/{storeId}/{invPer}/{purPer}")
    public ResponseEntity<APIResponse<String>> appointStoreManager(@PathVariable String userId, @PathVariable String appointedId, @PathVariable String storeId,@PathVariable boolean invPer, @PathVariable boolean purPer) {

        Response<String> response = serviceLayer.appointStoreManager(userId, appointedId, storeId, invPer , purPer);
        return checkIfResponseIsGood(response);
    }


    @PostMapping("/updateStoreManagerPermissions/{userId}/{appointedId}/{storeId}/{invPer}/{purPer}")
    public ResponseEntity<APIResponse<String>> updateStoreManagerPermissions(@PathVariable String userId, @PathVariable String appointedId, @PathVariable String storeId,@PathVariable boolean invPer, @PathVariable boolean purPer) {

        Response<String> response = serviceLayer.updateStoreManagerPermissions(userId, appointedId, storeId, invPer , purPer);
        return checkIfResponseIsGood(response);
    }

    @GetMapping("/generalProductFilter/{userId}/{categoryStr}/{keywords}/{minPrice}/{maxPrice}/{productMinRating}/{productsFromSearch}/{storeMinRating}")
    public ResponseEntity<APIResponse<List<String>>> generalProductFilter(@PathVariable String userId,@PathVariable String categoryStr,@PathVariable List<String> keywords,@PathVariable Integer minPrice, @PathVariable Integer maxPrice,@PathVariable Double productMinRating,@PathVariable List<String> productsFromSearch,@PathVariable Double storeMinRating) {
        try {
            Response<List<String>> response = serviceLayer.generalProductFilter(userId,categoryStr , keywords, minPrice, maxPrice , productMinRating, productsFromSearch,storeMinRating);
            if (response.isSuccess()) {
            List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/generalProductSearch/{userId}/{productName}/{categoryStr}/{keywords}")
    public ResponseEntity<APIResponse<Map<String,List<String>>>> generalProductSearch(@PathVariable String userId,@PathVariable String productName,@PathVariable String categoryStr,@PathVariable List<String> keywords) {
        try {
            Response<Map<String, List<ProductDTO>>> response = serviceLayer.generalProductSearchDTO(userId,productName , categoryStr , keywords);
            Map<String, List<ProductDTO>> result = response.getResult();
            if (response.isSuccess()) {
                Map<String , List<String>> dtos = new HashMap<>();
                for (String storeID: result.keySet()){
                    List<String> stringDTOs= new ArrayList<>();
                    for (ProductDTO productDTO : result.get(storeID)){
                        stringDTOs.add(objectMapper.writeValueAsString(productDTO));
                    }
                    dtos.put(storeID, stringDTOs);
                }
                //List<ProductDTO> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<>(dtos, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }



    @GetMapping("/getInformationAboutStores/{userId}")
    public ResponseEntity<APIResponse<List<String>>> getInformationAboutStores(@PathVariable String userId) {
        try {
            Response<List<String>> response = serviceLayer.getInformationAboutStores( userId);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getStoreWorkers/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreWorkers(@PathVariable String storeId) {
        try {
            Response<List<UserDTO>> response = serviceLayer.getStoreWorkers( storeId);
            List<String> dtosRes = new ArrayList<>();
            if (response.isSuccess()) {
                List<UserDTO> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                for (UserDTO userDTO : response.getResult() ) {
                    dtosRes.add(objectMapper.writeValueAsString(userDTO));
                }
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(dtosRes, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getSupplySystemHistory/{systemManagerUserId}")
    public ResponseEntity<APIResponse<List<String>>> getSupplySystemHistory(@PathVariable String systemManagerUserId) {
        try {
            Response<List<ShippingDTO>> response = serviceLayer.getSupplySystemHistory( systemManagerUserId);
            List<String> dtosRes = new ArrayList<>();
            if (response.isSuccess()) {
                List<ShippingDTO> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                for (ShippingDTO shippingDTO : response.getResult() ) {
                    dtosRes.add(objectMapper.writeValueAsString(shippingDTO));
                }
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(dtosRes, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }


    @GetMapping("/getAllSupplyByUser/{userId}")
    public ResponseEntity<APIResponse<List<String>>> getAllSupplyByUser(@PathVariable String userId) {
        try {
            Response<List<ShippingDTO>> response = serviceLayer.getAllSupplyByUser( userId);
            List<String> dtosRes = new ArrayList<>();
            if (response.isSuccess()) {
                List<ShippingDTO> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                for (ShippingDTO shippingDTO : response.getResult() ) {
                    dtosRes.add(objectMapper.writeValueAsString(shippingDTO));
                }
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(dtosRes, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getStoreOwnersDTO/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreOwnersDTO(@PathVariable String storeId) {
        try {
            Response<List<UserDTO>> response = serviceLayer.getStoreOwnersDTO( storeId);
            List<String> dtosRes = new ArrayList<>();
            if (response.isSuccess()) {
                List<UserDTO> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                for (UserDTO userDTO : response.getResult() ) {
                    dtosRes.add(objectMapper.writeValueAsString(userDTO));
                }
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(dtosRes, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }
    @GetMapping("/getStoreManagersDTO/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreManagersDTO(@PathVariable String storeId) {
        try {
            Response<List<UserDTO>> response = serviceLayer.getStoreManagersDTO( storeId);
            List<String> dtosRes = new ArrayList<>();
            if (response.isSuccess()) {
                List<UserDTO> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                for (UserDTO userDTO : result) {
                    dtosRes.add(objectMapper.writeValueAsString(userDTO));
                }
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(dtosRes, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getInformationAboutRolesInStore/{userId}/{storeId}")
    public ResponseEntity<APIResponse<Map<String, String>>> getInformationAboutRolesInStore(@PathVariable String userId, @PathVariable String storeId) {
        try {
            Response<Map<String, String>> response = serviceLayer.getInformationAboutRolesInStore( userId, storeId);
            if (response.isSuccess()) {
                Map<String, String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getStoreOwners/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreOwners(@PathVariable String storeId) {
        try {
            Response<List<String>> response = serviceLayer.getStoreOwners(storeId);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }
    @GetMapping("/getStoreManagers/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreManagers(@PathVariable String storeId) {
        try {
            Response<List<String>> response = serviceLayer.getStoreManagers(storeId);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");
                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getAuthorizationsOfManagersInStore/{userId}/{storeId}")
    public ResponseEntity<APIResponse<Map<String, List<Integer>>>> getAuthorizationsOfManagersInStore(@PathVariable String userId, @PathVariable String storeId) {
        try {
            Response<Map<String, List<Integer>>> response = serviceLayer.getAuthorizationsOfManagersInStore( userId, storeId);
            if (response.isSuccess()) {
                Map<String, List<Integer>> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }


    @PostMapping("/closeStore/{userId}/{storeId}")
    public ResponseEntity<APIResponse<String>> closeStore(@PathVariable String userId,  @PathVariable String storeId) {

        Response<String> response = serviceLayer.closeStore(userId,  storeId);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/openStore/{userId}/{storeName}/{storeDes}")
    public ResponseEntity<APIResponse<String>> openStore(@PathVariable String userId,  @PathVariable String storeName, @PathVariable String storeDes ) {

        Response<String> response = serviceLayer.openStore(userId,  storeName,storeDes);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/addProductToBasket/{productName}/{quantity}/{storeId}/{userId}")
    public ResponseEntity<APIResponse<String>> addProductToBasket(@PathVariable String productName,  @PathVariable int quantity,  @PathVariable String storeId, @PathVariable String userId ) {

        Response<String> response = serviceLayer.addProductToBasket(productName,quantity,  storeId,userId);
        return checkIfResponseIsGood(response);
    }

    @DeleteMapping("/removeProductFromBasket/{productName}/{storeId}/{userId}")
    public ResponseEntity<APIResponse<String>> removeProductFromBasket(@PathVariable String productName,   @PathVariable String storeId, @PathVariable String userId ) {

        Response<String> response = serviceLayer.removeProductFromBasket(productName,storeId,userId);
        return checkIfResponseIsGood(response);
    }


    @PostMapping("/setUserConfirmationPurchase/{userId}")
    public ResponseEntity<APIResponse<String>> setUserConfirmationPurchase(@PathVariable String userId ) {

        Response<String> response = serviceLayer.setUserConfirmationPurchase(userId);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/modifyShoppingCart/{productName}/{quantity}/{storeId}/{userId}")
    public ResponseEntity<APIResponse<String>> modifyShoppingCart(@PathVariable String productName,  @PathVariable int quantity,  @PathVariable String storeId, @PathVariable String userId ) {

        Response<String> response = serviceLayer.modifyShoppingCart(productName,quantity,  storeId,userId);
        return checkIfResponseIsGood(response);
    }


    @GetMapping("/marketManagerAskInfo/{userId}")
    public ResponseEntity<APIResponse<Map<String, Integer>>> marketManagerAskInfo( @PathVariable String userId ) {
        try {
            Response<Map<String, Integer>> response = serviceLayer.marketManagerAskInfo( userId);
            if (response.isSuccess()) {
                Map<String, Integer> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }
    @GetMapping("/storeOwnerGetInfoAboutStore/{userId}/{storeId}")
    public ResponseEntity<APIResponse<Map<String, Integer>>> storeOwnerGetInfoAboutStore( @PathVariable String userId ,@PathVariable String storeId ) {
        try {
            Response<Map<String, Integer>> response = serviceLayer.storeOwnerGetInfoAboutStore( userId, storeId);
            if (response.isSuccess()) {
                Map<String, Integer> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }



    @GetMapping("/generalProductFilterinStoreProductFilter/{userId}/{categoryStr}/{keywords}/{minPrice}/{maxPrice}/{productMinRating}/{storeId}/{productsFromSearch}/{storeMinRating}")
    public ResponseEntity<APIResponse<List<String>>> generalProductFilterinStoreProductFilter(@PathVariable String userId,@PathVariable String categoryStr,@PathVariable List<String> keywords,@PathVariable Integer minPrice, @PathVariable Integer maxPrice,@PathVariable Double productMinRating,@PathVariable String storeId,@PathVariable List<String> productsFromSearch,@PathVariable Double storeMinRating) {
        try {
            Response<List<String>> response = serviceLayer.inStoreProductFilter(userId,categoryStr , keywords, minPrice, maxPrice , productMinRating,storeId,  productsFromSearch,storeMinRating);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/inStoreProductSearch/{userId}/{productName}/{categoryStr}/{keywords}/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> inStoreProductSearch(@PathVariable String userId,@PathVariable String productName,@PathVariable String categoryStr,@PathVariable List<String> keywords, @PathVariable String storeId) {
        try {
            Response<List<ProductDTO>> response = serviceLayer.inStoreProductSearchDTO(userId,productName , categoryStr , keywords,storeId);
            if (response.isSuccess()) {
                List<String> dtos = new ArrayList<>();
                for (ProductDTO productDTO: response.getResult()){
                    dtos.add(objectMapper.writeValueAsString(productDTO));
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(dtos, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }


    @PostMapping("/addPurchaseRuleToStore")
    public ResponseEntity<APIResponse<String>> addPurchaseRuleToStore(@RequestParam Map<String,String> params) {
        try {
            String testRules = params.get("testRules");
            String operators = params.get("operators");
            String userId = params.get("userId");
            String storeId = params.get("storeId");
            List<TestRuleDTO> rules= null;
            //List<TestRuleDTO> logicOperators= null;
            TestRuleDTO[] dtosRule = null;
            List<String> stringRules = objectMapper.readValue(testRules, new TypeReference<List<String>>() {});
                // Deserialize each JSON string into a TestRuleDTO object
            rules = new ArrayList<>();
            for (String stringRule : stringRules) {
                TestRuleDTO rule = objectMapper.readValue(stringRule, TestRuleDTO.class);
                rules.add(rule);
            }
            List<String> logicOperators = objectMapper.readValue(operators, new TypeReference<List<String>>() {});
            Response<String> response = serviceLayer.addPurchaseRuleToStore(rules, logicOperators, userId, storeId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));
        }
    }


    @DeleteMapping("/removePurchaseRuleFromStore/{ruleNum}/{userId}/{storeId}")
    public ResponseEntity<APIResponse<String>> removePurchaseRuleFromStore(@PathVariable int ruleNum,  @PathVariable String userId, @PathVariable String storeId ) {

        Response<String> response = serviceLayer.removePurchaseRuleFromStore(ruleNum,userId, storeId);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/composeCurrentPurchaseRules/{ruleIndex1}/{ruleIndex2}/{operator}/{userId}/{storeId}")
    public ResponseEntity<APIResponse<String>> composeCurrentPurchaseRules(@PathVariable int ruleIndex1, @PathVariable int ruleIndex2, @PathVariable String operator, @PathVariable String userId, @PathVariable String storeId ) {

        Response<String> response = serviceLayer.composeCurrentPurchaseRules(ruleIndex1, ruleIndex2, operator, userId, storeId);
        return checkIfResponseIsGood(response);
    }

    @GetMapping("/getStoreCurrentPurchaseRules/{userId}/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreCurrentPurchaseRules(@PathVariable String userId, @PathVariable String storeId) {
        try {
            Response<List<String>> response = serviceLayer.getStoreCurrentPurchaseRules(userId, storeId);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getStoreCurrentDiscountRules/{userId}/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreCurrentDiscountRules(@PathVariable String userId, @PathVariable String storeId) {
        try {
            Response<List<String>> response = serviceLayer.getStoreCurrentDiscountRules(userId, storeId);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getStoreCurrentSimpleDiscountRules/{userId}/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreCurrentSimpleDiscountRules(@PathVariable String userId, @PathVariable String storeId) {
        try {
            Response<List<String>> response = serviceLayer.getStoreCurrentSimpleDiscountRules(userId, storeId);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getStoreCurrentCondDiscountRules/{userId}/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getStoreCurrentCondDiscountRules(@PathVariable String userId, @PathVariable String storeId) {
        try {
            Response<List<String>> response = serviceLayer.getStoreCurrentCondDiscountRules(userId, storeId);
            if (response.isSuccess()) {
                List<String> result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }


    @PostMapping("/reopenStore/{userId}/{storeId}")
    public ResponseEntity<APIResponse<String>> reopenStore(@PathVariable String userId, @PathVariable String storeId) {
        try {
            Response<String> response = serviceLayer.reopenStore(userId, storeId);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @PostMapping("/fireStoreManager/{nominatorUserId}/{nominatedUsername}/{storeID}")
    public ResponseEntity<APIResponse<String>> fireStoreManager(@PathVariable String nominatorUserId,@PathVariable String nominatedUsername,@PathVariable String storeID) {
        try {
            Response<String> response = serviceLayer.fireStoreManager(nominatorUserId,nominatedUsername ,storeID);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }


    @PostMapping("/answerJobProposal/{userId}/{storeID}/{manager_proposal}/{answer}")
    public ResponseEntity<APIResponse<String>> answerJobProposal(@PathVariable String userId,@PathVariable String storeID,@PathVariable boolean manager_proposal, @PathVariable boolean answer) {
        try {
            Response<String> response = serviceLayer.answerJobProposal(userId ,storeID ,manager_proposal, answer);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

//    @PostMapping("/cancelAcquisition/{userId}/{acquisitionId}")
//    public ResponseEntity<APIResponse<String>> cancel(@PathVariable String userId, @PathVariable String acquisitionId) {
//        try {
//            Response<String> response = serviceLayer.cancelPayment(userId, acquisitionId);
//            if (response.isSuccess()) {
//                String result = response.getResult();
//                HttpHeaders headers = new HttpHeaders();
//                headers.add("accept", "*/*");
//
//                return ResponseEntity.status(HttpStatus.OK).headers(headers)
//                        .body(new APIResponse<String>(result, null));
//            } else {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body(new APIResponse<>(null, response.getDescription()));
//            }
//        }  catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body(new APIResponse<>(null, e.getMessage()));
//
//        }
//    }



    @PostMapping("/fireStoreOwner/{nominatorUserId}/{nominatedUsername}/{storeID}")
    public ResponseEntity<APIResponse<String>> fireStoreOwner(@PathVariable String nominatorUserId,@PathVariable String nominatedUsername,@PathVariable String storeID) {
        try {
            Response<String> response = serviceLayer.fireStoreOwner(nominatorUserId,nominatedUsername ,storeID);
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }







    @PostMapping("/composeCurrentSimpleDiscountRules/{ruleIndex1}/{ruleIndex2}/{numericalOperator}/{userId}/{storeId}")
    public ResponseEntity<APIResponse<String>> composeCurrentSimpleDiscountRules(@PathVariable int ruleIndex1, @PathVariable int ruleIndex2, @PathVariable String numericalOperator, @PathVariable String userId, @PathVariable String storeId ) {

        Response<String> response = serviceLayer.composeCurrentSimpleDiscountRules(ruleIndex1, ruleIndex2, numericalOperator, userId, storeId);
        return checkIfResponseIsGood(response);
    }

    @PostMapping("/composeCurrentCondDiscountRules/{ruleIndex1}/{ruleIndex2}/{logicalOperator}/{numericalOperator}/{userId}/{storeId}")
    public ResponseEntity<APIResponse<String>> composeCurrentCondDiscountRules(@PathVariable int ruleIndex1, @PathVariable int ruleIndex2, @PathVariable String logicalOperator, @PathVariable String numericalOperator, @PathVariable String userId, @PathVariable String storeId ) {

        Response<String> response = serviceLayer.composeCurrentCondDiscountRules(ruleIndex1, ruleIndex2, logicalOperator, numericalOperator, userId, storeId);
        return checkIfResponseIsGood(response);
    }

    @DeleteMapping("/removeDiscountRuleFromStore/{ruleIndex}/{userId}/{storeId}")
    public ResponseEntity<APIResponse<String>> removeDiscountRuleFromStore(@PathVariable int ruleIndex, @PathVariable String userId, @PathVariable String storeId)
    {
        Response<String> response = serviceLayer.removeDiscountRuleFromStore(ruleIndex, userId, storeId);
        return checkIfResponseIsGood(response);
    }

    private ResponseEntity<APIResponse<String>> checkIfResponseIsGood(Response<String> response) {
        try {
            if (response.isSuccess()) {
                String result = response.getResult();
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<String>(result, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<String>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }

    @GetMapping("/getUserAcquisitionsHistory/{userId}")
    public ResponseEntity<APIResponse<List<String>>> getUserAcquisitionsHistory(@PathVariable String userId) {
        try {
            Response<List<AcquisitionDTO>> response = serviceLayer.getUserAcquisitionsHistory(userId);
            if (response.isSuccess()) {
                List<String> dtos = new ArrayList<>();
                for (AcquisitionDTO acquisitionDTO : response.getResult()){
                    dtos.add(objectMapper.writeValueAsString(acquisitionDTO));
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(dtos, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }


    @GetMapping("/getAllStoreReceipts/{userId}/{storeId}")
    public ResponseEntity<APIResponse<List<String>>> getAllStoreReceipts(@PathVariable String userId,@PathVariable String storeId) {
        try {
            Response<List<ReceiptDTO>> response = serviceLayer.getAllStoreReceipts(userId, storeId);
            if (response.isSuccess()) {
                List<String> stringDTOs = new ArrayList<>();
                for (ReceiptDTO receiptDTO: response.getResult()) {
                    stringDTOs.add(objectMapper.writeValueAsString(receiptDTO));
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<List<String>>(stringDTOs, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }


    @GetMapping("/getUserReceiptsByAcquisition/{userId}/{acquisitionId}")
    public ResponseEntity<APIResponse<Map<String,String>>> getUserReceiptsByAcquisition(@PathVariable String userId,@PathVariable String acquisitionId) {
        try {
            Response<Map<String, ReceiptDTO>> response = serviceLayer.getUserReceiptsByAcquisition(userId, acquisitionId);
            if (response.isSuccess()) {
                Map<String,String> idToDtos = new HashMap<>();
                for (Map.Entry<String, ReceiptDTO> entry : response.getResult().entrySet()) {
                    idToDtos.put(entry.getKey(), objectMapper.writeValueAsString(entry.getValue()));
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add("accept", "*/*");

                return ResponseEntity.status(HttpStatus.OK).headers(headers)
                        .body(new APIResponse<Map<String,String>>(idToDtos, null));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new APIResponse<>(null, response.getDescription()));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse<>(null, e.getMessage()));

        }
    }



}



