package com.example.application.Model;

import com.example.application.Util.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.*;

public class APIcalls {
    static RestTemplate restTemplate = new RestTemplate();;
    static ObjectMapper mapper = new ObjectMapper();



    public static String startSession() {
        String url = "http://localhost:8080/api/market/enterSystem";  // Absolute URL

        HttpHeaders headers = new HttpHeaders();
        headers.add("accept" , "*/*");
        try {
            URI uri = new URI(url);// Create URI object

            HttpEntity<String> entity = new HttpEntity<String>(headers);
            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {});

            if (response.getStatusCode().is2xxSuccessful()) {
                APIResponse<String> responseBody = response.getBody();
                if (responseBody != null) {
                    String data = responseBody.getData();
                    if (data != null) {
                        return data;
                    }
                }
            } else {
                System.err.println("Error occurred: " + response.getStatusCode());
            }
        } catch (URISyntaxException e) {
            System.err.println("Invalid URL: " + e.getMessage());
            // Handle the exception accordingly
        }
        return "notFound";
    }

    public static String init() {
        try {
            String url = "http://localhost:8080/api/market/initiate";  // Absolute URL

            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean checkInitializedMarket(){
        try {
            String url = "http://localhost:8080/api/market/checkInitializedMarket";  // Absolute URL

            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<Boolean>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<Boolean>>() {
                    });
            APIResponse<Boolean> responseBody = response.getBody();
            Boolean data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isMember(String userID){
        try {
            String url = "http://localhost:8080/api/member/isMember/{userId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<Boolean>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<Boolean>>() {
                    });
            APIResponse<Boolean> responseBody = response.getBody();
            Boolean data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isAdmin(String memberId){
        try {
            String url = "http://localhost:8080/api/member/isAdmin/{memberId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(memberId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<Boolean>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<Boolean>>() {
                    });
            APIResponse<Boolean> responseBody = response.getBody();
            Boolean data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String getMemberName(String memberID){
        try {
            String url = "http://localhost:8080/api/member/getMemberName/{memberId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(memberID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public static String cancelAcquisition(String userId , String acquisitionId){
        try {
            String url = "http://localhost:8080/api/market/cancelAcquisition/{userId}/{acquisitionId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userId, acquisitionId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }




    public static List<StoreDTO> getAllStores(){
        try {
            String url = "http://localhost:8080/api/store/getAllStores";  // Absolute URL

            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            List<StoreDTO> data = new LinkedList<StoreDTO>();
            for(int i=0 ; i<responseBody.getData().size() ; i++){
                data.add(mapper.readValue(responseBody.getData().get(i), StoreDTO.class));
            }
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static CartDTO getCart(String userID){
        try {
            String url = "http://localhost:8080/api/user/getCart/{id}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            return mapper.readValue(responseBody.getData(), CartDTO.class);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getStoreCurrentPurchaseRules(String userID, String storeID){
        try {
            String url = "http://localhost:8080/api/market/getStoreCurrentPurchaseRules/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getStoreCurrentDiscountRules(String userID, String storeID){
        try {
            String url = "http://localhost:8080/api/market/getStoreCurrentDiscountRules/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getStoreCurrentSimpleDiscountRules(String userID, String storeID){
        try {
            String url = "http://localhost:8080/api/market/getStoreCurrentSimpleDiscountRules/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getStoreCurrentCondDiscountRules(String userID, String storeID){
        try {
            String url = "http://localhost:8080/api/market/getStoreCurrentCondDiscountRules/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Map<Integer, String> getAllPurchaseRules(String userID, String storeID){
        try {
            String url = "http://localhost:8080/api/market/getAllPurchaseRules/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<Map<Integer, String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<Map<Integer, String>>>() {
                    });
            APIResponse<Map<Integer, String>> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String addPurchaseRuleToStore(List<TestRuleDTO> Rules, List<String> logicOperators, String userID, String storeID){
        try {
            ObjectMapper mapper2 = new ObjectMapper();
            mapper2.registerModule(new JavaTimeModule());
            mapper2.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String url = "http://localhost:8080/api/market/addPurchaseRuleToStore";  // Absolute URL

            List<String> stringRules = new ArrayList<>();
            for (TestRuleDTO testRuleDTO : Rules){
                stringRules.add(mapper2.writeValueAsString(testRuleDTO));
            }
            String jsonRules = mapper2.writeValueAsString(stringRules);
            String logicOp = mapper2.writeValueAsString(logicOperators);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("userId", userID);
            params.add("storeId", storeID);
            params.add("testRules", jsonRules);
            params.add("operators", logicOp);

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            String data = response.getBody();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            System.err.println("addPurchaseRuleToStore error occurred");
            return null;
        }
    }

    public static String addDiscountSimpleRuleToStore(List<DiscountValueDTO> discDetailsList, List<String> numericalOperatorsList, String userID, String storeID){
        try {
            ObjectMapper mapper2 = new ObjectMapper();
            mapper2.registerModule(new JavaTimeModule());
            mapper2.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String url = "http://localhost:8080/api/market/addDiscountSimpleRuleToStore";  // Absolute URL

            List<String> stringDisc = new ArrayList<>();
            for (DiscountValueDTO discountValueDTO : discDetailsList){
                stringDisc.add(mapper2.writeValueAsString(discountValueDTO));
            }
            String jsonDisc = mapper2.writeValueAsString(stringDisc);
            String numericOp = mapper2.writeValueAsString(numericalOperatorsList);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("userId", userID);
            params.add("storeId", storeID);
            params.add("discs", jsonDisc);
            params.add("numericalOperators", numericOp);

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            String data = response.getBody();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            System.err.println("addDiscountSimpleRuleToStore error occurred");
            return null;
        }
    }

    public static String addDiscountCondRuleToStore(List<TestRuleDTO> Rules, List<String> logicOperators, List<DiscountValueDTO> discDetailsList, List<String> numericalOperatorsList, String userID, String storeID){
        try {
            ObjectMapper mapper2 = new ObjectMapper();
            mapper2.registerModule(new JavaTimeModule());
            mapper2.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String url = "http://localhost:8080/api/market/addDiscountCondRuleToStore";  // Absolute URL

            List<String> stringDisc = new ArrayList<>();
            for (DiscountValueDTO discountValueDTO : discDetailsList){
                stringDisc.add(mapper2.writeValueAsString(discountValueDTO));
            }
            String jsonDisc = mapper2.writeValueAsString(stringDisc);
            String numericOp = mapper2.writeValueAsString(numericalOperatorsList);

            List<String> stringRules = new ArrayList<>();
            for (TestRuleDTO testRuleDTO : Rules){
                stringRules.add(mapper2.writeValueAsString(testRuleDTO));
            }
            String jsonRules = mapper2.writeValueAsString(stringRules);
            String logicOp = mapper2.writeValueAsString(logicOperators);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("userId", userID);
            params.add("storeId", storeID);
            params.add("discDetails", jsonDisc);
            params.add("numericalOperators", numericOp);
            params.add("testRules", jsonRules);
            params.add("logicOperators", logicOp);

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            String data = response.getBody();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            System.err.println("addDiscountCondRuleToStore error occurred");
            return null;
        }
    }

    public static String composeCurrentPurchaseRules(int ruleIndex1, int ruleIndex2, String operator, String userId, String storeId){
        try {
            String url = "http://localhost:8080/api/market/composeCurrentPurchaseRules/{ruleIndex1}/{ruleIndex2}/{operator}/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(ruleIndex1, ruleIndex2, operator, userId, storeId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            System.err.println("composeCurrentPurchaseRules error occurred");
            return null;
        }
    }

    public static String composeCurrentSimpleDiscountRules(int ruleIndex1, int ruleIndex2, String numericalOperator, String userId, String storeId){
        try {
            String url = "http://localhost:8080/api/market/composeCurrentSimpleDiscountRules/{ruleIndex1}/{ruleIndex2}/{numericalOperator}/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(ruleIndex1, ruleIndex2, numericalOperator, userId, storeId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            System.err.println("composeCurrentSimpleDiscountRules error occurred");
            return null;
        }
    }

    public static String composeCurrentCondDiscountRules(int ruleIndex1, int ruleIndex2, String logicalOperator, String numericalOperator, String userId, String storeId){
        try {
            String url = "http://localhost:8080/api/market/composeCurrentCondDiscountRules/{ruleIndex1}/{ruleIndex2}/{logicalOperator}/{numericalOperator}/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(ruleIndex1, ruleIndex2, logicalOperator, numericalOperator, userId, storeId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            System.err.println("composeCurrentCondDiscountRules error occurred");
            return null;
        }
    }

    public static String removePurchaseRuleFromStore(int ruleNum, String userId, String storeId){
        try {
            String url = "http://localhost:8080/api/market/removePurchaseRuleFromStore/{ruleNum}/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(ruleNum, userId, storeId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.DELETE,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String removeDiscountRuleFromStore(int ruleIndex, String userId, String storeId){
        try {
            String url = "http://localhost:8080/api/market/removeDiscountRuleFromStore/{ruleIndex}/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(ruleIndex, userId, storeId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.DELETE,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getCategories(){
        try {
            String url = "http://localhost:8080/api/market/getCategories";  // Absolute URL

            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getNotifications(String memberID){
        try {
            String url = "http://localhost:8080/api/market/getUserNotifications/{memberId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(memberID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<ProductDTO> getStoreProducts(String storeID){
        try {
            String url = "http://localhost:8080/api/market/getStoreProducts/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            List<ProductDTO> data = new LinkedList<ProductDTO>();
            for(int i=0 ; i<responseBody.getData().size() ; i++){
                data.add(mapper.readValue(responseBody.getData().get(i), ProductDTO.class));
            }
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getStoreOwners(String storeID){
        try {
            String url = "http://localhost:8080/api/market/getStoreOwners/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getStoreMangers(String storeID){
        try {
            String url = "http://localhost:8080/api/market/getStoreManagers/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getExternalSupplyServices(){
        try {
            String url = "http://localhost:8080/api/market/getExternalSupplyServices";  // Absolute URL

            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> getExternalPaymentServices(){
        try {
            String url = "http://localhost:8080/api/market/getExternalPaymentServices";  // Absolute URL

            URI uri = new URI(url);

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            return responseBody.getData();
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static StoreDTO getStore(String storeID){
        try {
            String url = "http://localhost:8080/api/store/getStore/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            StoreDTO data = mapper.readValue(responseBody.getData(), StoreDTO.class);
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static UserDTO getUser(String userID){
        try {
            String url = "http://localhost:8080/api/user/getUser/{userId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            UserDTO data = mapper.readValue(responseBody.getData(), UserDTO.class);
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String register(UserDTO userDTO, String password) {
        try {
            String url = "http://localhost:8080/api/market/register";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url).queryParam("userDTO", mapper.writeValueAsString(userDTO))
                    .queryParam("password", password).build().toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        } catch (HttpClientErrorException e) {
            // Extract error message from the response body
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return "register An error occurred.";
        }
    }

    public static Map<String, List<ProductDTO>> generalProductSearch(String userID, String productName, String categoryStr, List<String> keywords){
        try {
            String url = "http://localhost:8080/api/market/generalProductSearch/{userId}/{productName}/{categoryStr}/{keywords}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, productName, categoryStr, keywords)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<Map<String, List<String>>>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<Map<String, List<String>>>>() {
                    });
            APIResponse<Map<String, List<String>>> responseBody = response.getBody();
            Map<String,List<ProductDTO>> data = new HashMap<>(); //<storeid, list<product>>
            for (Map.Entry<String, List<String>> entry : responseBody.getData().entrySet()) {
                List<ProductDTO> productsDTOs = new ArrayList<>();
                for (String productJson : entry.getValue()) {
                    ProductDTO productDTO = mapper.readValue(productJson, ProductDTO.class);
                    productsDTOs.add(productDTO);
                }
                data.put(entry.getKey(), productsDTOs);
            }
            return data;
        } catch (HttpClientErrorException e) {
            throw new APIException(extractErrorMessageFromJson(e.getResponseBodyAsString()));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<ProductDTO> inStoreProductSearch(String userID, String productName, String categoryStr, List<String> keywords, String storeId){
        try {
            String url = "http://localhost:8080/api/market/inStoreProductSearch/{userId}/{productName}/{categoryStr}/{keywords}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, productName, categoryStr, keywords, storeId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            List<ProductDTO> data = new LinkedList<ProductDTO>();
            for(int i=0 ; i<responseBody.getData().size() ; i++){
                data.add(mapper.readValue(responseBody.getData().get(i), ProductDTO.class));
            }
            return data;
        }
        catch (HttpClientErrorException e) {
            throw new APIException(extractErrorMessageFromJson(e.getResponseBodyAsString()));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isStoreOwner(String memberID, String storeID){
        try {
            String url = "http://localhost:8080/api/member/isStoreOwner/{memberId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(memberID, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<Boolean>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<Boolean>>() {
                    });
            APIResponse<Boolean> responseBody = response.getBody();
            Boolean data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isStoreManager(String memberID, String storeID){
        try {
            String url = "http://localhost:8080/api/member/isStoreManager/{memberId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(memberID, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<Boolean>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<Boolean>>() {
                    });
            APIResponse<Boolean> responseBody = response.getBody();
            Boolean data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasInventoryPermission(String memberID, String storeID){
        try {
            String url = "http://localhost:8080/api/member/hasInventoryPermission/{memberId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(memberID, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<Boolean>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<Boolean>>() {
                    });
            APIResponse<Boolean> responseBody = response.getBody();
            Boolean data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasPurchasePermission(String memberID, String storeID){
        try {
            String url = "http://localhost:8080/api/member/hasPurchasePermission/{memberId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(memberID, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<Boolean>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<Boolean>>() {
                    });
            APIResponse<Boolean> responseBody = response.getBody();
            Boolean data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String logout(String userID){
        try {
            String url = "http://localhost:8080/api/market/logout/{userId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String login(String userID, String username, String password){
        try {
            String url = "http://localhost:8080/api/market/login/{userId}/{userName}/{password}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, username, password)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String closeStore(String userID, String storeID){
        try {
            String url = "http://localhost:8080/api/market/closeStore/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String reopenStore(String userID, String storeID){
        try {
            String url = "http://localhost:8080/api/market/reopenStore/{userId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isStoreOpen(String storeID){
        try {
            String url = "http://localhost:8080/api/store/isStoreOpen/{storeID}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<Boolean>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<Boolean>>() {
                    });
            APIResponse<Boolean> responseBody = response.getBody();
            Boolean data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static String openStore(String userID, String storeName, String storeDes){
        try {
            String url = "http://localhost:8080/api/market/openStore/{userId}/{storeName}/{storeDes}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, storeName, storeDes)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String addProductToBasket(String productName, int quantity, String storeID, String userID){
        try {
            String url = "http://localhost:8080/api/market/addProductToBasket/{productName}/{quantity}/{storeId}/{userId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(productName, quantity, storeID, userID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String addProductToStore(String userID, String storeID, ProductDTO productDTO){
        try {
            String url = "http://localhost:8080/api/market/addProductToStore";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .queryParam("userId", userID)
                    .queryParam("storeId",storeID)
                    .queryParam("productDTO",mapper.writeValueAsString(productDTO))
                    .build().toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String removeProductFromStore(String userID, String storeID, String productName){
        try {
            String url = "http://localhost:8080/api/market/removeProductFromStore/{userId}/{storeId}/{productId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, storeID, productName)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.DELETE,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String updateProductInStore(String userID, String storeID, ProductDTO productDTO){
        try {
            String url = "http://localhost:8080/api/market/updateProductInStore";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .queryParam("userId", userID)
                    .queryParam("storeId",storeID)
                    .queryParam("productDTO",mapper.writeValueAsString(productDTO))
                    .build().toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String appointStoreOwner(String userID, String appointedUsername, String storeID){
        try {
            String url = "http://localhost:8080/api/market/appointStoreOwner/{userId}/{appointedId}/{storeId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, appointedUsername, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String appointStoreManager(String userID, String appointedUsername, String storeID, boolean invPer, boolean purPer){
        try {
            String url = "http://localhost:8080/api/market/appointStoreManager/{userId}/{appointedId}/{storeId}/{invPer}/{purPer}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, appointedUsername, storeID, invPer, purPer)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String fireStoreOwner(String userID, String appointedUsername, String storeID){
        try {
            String url = "http://localhost:8080/api/market/fireStoreOwner/{nominatorUserId}/{nominatedUsername}/{storeID}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, appointedUsername, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String fireStoreManager(String userID, String appointedUsername, String storeID){
        try {
            String url = "http://localhost:8080/api/market/fireStoreManager/{nominatorUserId}/{nominatedUsername}/{storeID}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, appointedUsername, storeID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String updateStoreManagerPermissions(String userID, String appointedUsername, String storeID, boolean invPer, boolean purPer){
        try {
            String url = "http://localhost:8080/api/market/updateStoreManagerPermissions/{userId}/{appointedId}/{storeId}/{invPer}/{purPer}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, appointedUsername, storeID, invPer, purPer)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String addExternalPaymentService(String systemMangerUserId, PaymentServiceDTO paymentServiceDTO){
        try {
            String url = "http://localhost:8080/api/market/addExternalPaymentService";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .queryParam("systemMangerUserId",systemMangerUserId)
                    .queryParam("paymentServiceDTO",mapper.writeValueAsString(paymentServiceDTO))
                    .build().toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String addExternalSupplyService(String systemMangerUserId, SupplyServiceDTO supplyServiceDTO){
        try {
            String url = "http://localhost:8080/api/market/addExternalSupplyService";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .queryParam("systemMangerUserId", systemMangerUserId)
                    .queryParam("supplyServiceDTO",mapper.writeValueAsString(supplyServiceDTO))
                    .build().toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String removeExternalPaymentService(String urlInput, String systemMangerUserId){
        try {
            String url = "http://localhost:8080/api/market/removeExternalPaymentService";  // Absolute URL

            String encodedUrl = URLEncoder.encode(urlInput, "UTF-8");

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .queryParam("url", encodedUrl)
                    .queryParam("systemMangerUserId", systemMangerUserId)
                    .build().toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.DELETE,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String removeExternalSupplyService(String urlInput, String systemMangerUserId){
        try {
            String url = "http://localhost:8080/api/market/removeExternalSupplyService";  // Absolute URL

            String encodedUrl = URLEncoder.encode(urlInput, "UTF-8");

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .queryParam("url", encodedUrl)
                    .queryParam("systemMangerUserId", systemMangerUserId)
                    .build().toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.DELETE,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (Exception e){
            e.printStackTrace();
            return e.getMessage();
        }
    }

    public static String removeProductFromBasket(String productName, String storeId,String userId){
        try {
            String url = "http://localhost:8080/api/market/removeProductFromBasket/{productName}/{storeId}/{userId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(productName, storeId, userId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.DELETE,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String modifyShoppingCart(String productName, int quantity,String storeId,String userId){
        try {
            String url = "http://localhost:8080/api/market/modifyShoppingCart/{productName}/{quantity}/{storeId}/{userId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(productName, quantity,storeId, userId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String setUserConfirmationPurchase(String userId){
        try {
            String url = "http://localhost:8080/api/market/setUserConfirmationPurchase/{userId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String answerJobProposal(String userId, String storeId, boolean manager_proposal, boolean answer){
        try {
            String url = "http://localhost:8080/api/market/answerJobProposal/{userId}/{storeID}/{manager_proposal}/{answer}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userId, storeId, manager_proposal, answer)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            String data = responseBody.getData();
            return data;
        }
        catch (HttpClientErrorException e){
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static CartDTO getCartAfterValidation(String userID, UserDTO userDTO){
        try {
            String url = "http://localhost:8080/api/market/getCartAfterValidation";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .queryParam("userID", userID)
                    .queryParam("userDTO",mapper.writeValueAsString(userDTO))
                    .build().toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(
                    uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {
                    });
            APIResponse<String> responseBody = response.getBody();
            CartDTO data = mapper.readValue(responseBody.getData(), CartDTO.class);
            return data;
        }
         catch (HttpClientErrorException e) {
            throw new APIException(extractErrorMessageFromJson(e.getResponseBodyAsString()));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static String purchase(UserDTO userDTO, PaymentDTO paymentDTO, CartDTO cartDTO) {
        try {
            String url = "http://localhost:8080/api/market/purchase";

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("userDTO", mapper.writeValueAsString(userDTO));
            requestBody.put("paymentDTO", mapper.writeValueAsString(paymentDTO));
            requestBody.put("cartDTO", mapper.writeValueAsString(cartDTO));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("accept", "*/*");

            HttpEntity<String> entity = new HttpEntity<>(mapper.writeValueAsString(requestBody), headers);

            ResponseEntity<APIResponse<String>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<APIResponse<String>>() {}
            );

            APIResponse<String> responseBody = response.getBody();
            return responseBody.getData();
        } catch (HttpClientErrorException e) {
            return extractErrorMessageFromJson(e.getResponseBodyAsString());
        } catch (Exception e) {
            e.printStackTrace();
            return "purchase An error occurred.";
        }
    }

    public static List<AcquisitionDTO> getUserAcquisitionsHistory(String userID){
        try {
            String url = "http://localhost:8080/api/market/getUserAcquisitionsHistory/{userId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            List<AcquisitionDTO> data = new LinkedList<AcquisitionDTO>();
            for(int i=0 ; i<responseBody.getData().size() ; i++){
                data.add(mapper.readValue(responseBody.getData().get(i), AcquisitionDTO.class));
            }
            return data;
        }
        catch (HttpClientErrorException e) {
            throw new APIException(extractErrorMessageFromJson(e.getResponseBodyAsString()));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<AcquisitionDTO> getAllAcquisitions(String systemManagerUserId){
        try {
            String url = "http://localhost:8080/api/market/getAllAcquisitions/{systemManagerUserId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(systemManagerUserId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            List<AcquisitionDTO> data = new LinkedList<AcquisitionDTO>();
            for(int i=0 ; i<responseBody.getData().size() ; i++){
                data.add(mapper.readValue(responseBody.getData().get(i), AcquisitionDTO.class));
            }
            return data;
        }
        catch (HttpClientErrorException e) {
            throw new APIException(extractErrorMessageFromJson(e.getResponseBodyAsString()));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<ShippingDTO> getAllSupplyByUser(String userID){
        try {
            String url = "http://localhost:8080/api/market/getAllSupplyByUser/{userId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            List<ShippingDTO> data = new LinkedList<ShippingDTO>();
            for(int i=0 ; i<responseBody.getData().size() ; i++){
                data.add(mapper.readValue(responseBody.getData().get(i), ShippingDTO.class));
            }
            return data;
        }
        catch (HttpClientErrorException e) {
            throw new APIException(extractErrorMessageFromJson(e.getResponseBodyAsString()));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<ShippingDTO> getSupplySystemHistory(String systemManagerUserId){
        try {
            String url = "http://localhost:8080/api/market/getSupplySystemHistory/{systemManagerUserId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(systemManagerUserId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            List<ShippingDTO> data = new LinkedList<ShippingDTO>();
            for(int i=0 ; i<responseBody.getData().size() ; i++){
                data.add(mapper.readValue(responseBody.getData().get(i), ShippingDTO.class));
            }
            return data;
        }
        catch (HttpClientErrorException e) {
            throw new APIException(extractErrorMessageFromJson(e.getResponseBodyAsString()));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<StoreOwnerDTO> getOwnerJobProposal(String userID){
        try {
            String url = "http://localhost:8080/api/market/getOwnerJobProposal/{userId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            List<StoreOwnerDTO> data = new LinkedList<StoreOwnerDTO>();
            for(int i=0 ; i<responseBody.getData().size() ; i++){
                data.add(mapper.readValue(responseBody.getData().get(i), StoreOwnerDTO.class));
            }
            return data;
        }
        catch (HttpClientErrorException e) {
            throw new APIException(extractErrorMessageFromJson(e.getResponseBodyAsString()));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<StoreManagerDTO> getManagerJobProposal(String userID){
        try {
            String url = "http://localhost:8080/api/market/getManagerJobProposal/{userId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<List<String>>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<List<String>>>() {
                    });
            APIResponse<List<String>> responseBody = response.getBody();
            List<StoreManagerDTO> data = new LinkedList<StoreManagerDTO>();
            for(int i=0 ; i<responseBody.getData().size() ; i++){
                data.add(mapper.readValue(responseBody.getData().get(i), StoreManagerDTO.class));
            }
            return data;
        }
        catch (HttpClientErrorException e) {
            throw new APIException(extractErrorMessageFromJson(e.getResponseBodyAsString()));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String,ReceiptDTO> getUserReceiptsByAcquisition(String userID, String acquisitionId){
        try {
            String url = "http://localhost:8080/api/market/getUserReceiptsByAcquisition/{userId}/{acquisitionId}";  // Absolute URL

            URI uri = UriComponentsBuilder.fromUriString(url)
                    .buildAndExpand(userID, acquisitionId)
                    .toUri();

            HttpHeaders headers = new HttpHeaders();
            headers.add("accept", "*/*");
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity<APIResponse<Map<String,String>>> response = restTemplate.exchange(uri,  // Use the URI object here
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<APIResponse<Map<String,String>>>() {
                    });
            APIResponse<Map<String, String>> responseBody = response.getBody();
            Map<String,ReceiptDTO> data = new HashMap<>();
            for (Map.Entry<String, String> entry : responseBody.getData().entrySet()) {
                ReceiptDTO receiptDTO = mapper.readValue(entry.getValue(), ReceiptDTO.class);
                data.put(entry.getKey(), receiptDTO);
            }
            return data;
        }
        catch (HttpClientErrorException e) {
            throw new APIException(extractErrorMessageFromJson(e.getResponseBodyAsString()));
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static String extractErrorMessageFromJson(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);
            JsonNode errorMessageNode = rootNode.path("errorMassage");
            return errorMessageNode.asText("An unknown error occurred.");
        } catch (Exception e) {
            e.printStackTrace();
            return "extractErrorMessageFromJson An unknown error occurred.";
        }
    }
}
