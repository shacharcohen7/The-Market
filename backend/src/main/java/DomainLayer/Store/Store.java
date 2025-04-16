package DomainLayer.Store;

import DomainLayer.Store.StoreDiscountPolicy.DiscountPolicy;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StorePurchasePolicy.PurchasePolicy;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
//import Util.DiscountValueDTO;
import Util.DiscountValueDTO;
import Util.ExceptionsEnum;
import Util.ProductDTO;

import java.util.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
//import  DomainLayer.Notifications.Notification;
import Util.UserDTO;

import jakarta.persistence.*;

@Entity
@Table(name = "store", schema = "themarketdb")
public class Store {
    @Id
  //  @Column(name = "store_id")
    private String store_ID;



    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @MapKey(name = "productName")
    @JoinColumn(name = "store_id")
    private Map<String, Product> storeProducts = new HashMap<String, Product>();

    @Column(nullable = false)
    private boolean isOpened;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "discount_policy_id")
//    @Transient
    private DiscountPolicy discountPolicy;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "purchase_policy_id")
//    @Transient
    private PurchasePolicy purchasePolicy;

    @Column(name = "rating")
    private double rating;

    @Column(name = "num_of_ratings")
    private int numOfRatings;

//    @ElementCollection
//    @CollectionTable(name = "receipts", joinColumns = @JoinColumn(name = "store_id"))
//    @MapKeyColumn(name = "receipt_id")
//    @Column(name = "user_id")
//    @Transient
//    private Map<String, String> receiptsIdsUserIds; //<receiptId, userId>

    @Column(name = "store_name")
    private String storeName;

    @Column(name = "description")
    private String description;

    @Transient
    private final Object storeProductLock;

    @Transient
    private final Object storeIdLock;

    @Transient
    private final Object isOpenedLock;

    @Transient
    private final Object receiptsLock;

    public Store(String store_ID, String storeName, String description)
    {
        this.store_ID = store_ID;
        this.isOpened = true;
        storeIdLock= new Object();
        discountPolicy= new DiscountPolicy();
        purchasePolicy = new PurchasePolicy();
        storeProductLock= new Object();
        isOpenedLock = new Object();
//        this.receiptsIdsUserIds = new HashMap<>();
        this.rating = 0;
        this.numOfRatings = 0;
        this.storeName = storeName;
        this.description = description;
        receiptsLock = new Object();
    }

    // No-argument constructor
    public Store() {
        this.storeProductLock = new Object();
        this.storeIdLock = new Object();
        this.isOpenedLock = new Object();
        this.receiptsLock = new Object();
    }

    public String getStoreName(){
        return storeName;
    }

    public void returnProductToStore(Map<String, List<Integer>> products){
        synchronized (storeProductLock) {
            for (String product : products.keySet()) {
                storeProducts.get(product).addToStock(products.get(product).get(0));
            }
        }
    }

    public Map<String, Product> getStoreProducts() {
        synchronized (storeProductLock) {
            return storeProducts;
        }
    }
    public String getStoreID()
    {
        synchronized (storeIdLock) {
            return store_ID;
        }
    }

    public void addProduct(ProductDTO product) {
        Category category = Category.fromString(product.getCategoryStr());
        synchronized (storeProductLock) {
            storeProducts.put(product.getName(), new Product(product, category));
        }
    }

    public void addProduct(Product product) {
        synchronized (storeProductLock) {
            storeProducts.put(product.getProductName(),product);
        }
    }

    public Product getProductByName(String productName)
    {
        synchronized (storeProductLock) {
            return storeProducts.get(productName);
        }
    }

    public boolean checkProductExists(String productName)
    {
        synchronized (storeProductLock) {
            return storeProducts.containsKey(productName);
        }
    }


    public List<ProductDTO> getProductsDTO(){
        List<ProductDTO> productDTOList = new ArrayList<>();
        synchronized (storeProductLock){
            for (Product product: storeProducts.values()){
                productDTOList.add(new ProductDTO(product));
            }
        }
        return productDTOList;
    }

    public ProductDTO getProductDTOByName(String productName, int quantity) throws Exception {
        /*this function receives product from a user basket, with the quantity of this product in the basket and it's total price*/
        synchronized (storeProductLock) {
            Product product = storeProducts.get(productName);
            if(product == null)
                throw new Exception(ExceptionsEnum.productNotExistInStore.toString());
            return new ProductDTO(productName, product.getPrice() * quantity, quantity, product.getDescription(), product.getCategoryName());
        }
    }

    public boolean checkProductQuantity(String productName, int quantity)
    {
        Product productToCheck = getProductByName(productName);
        return productToCheck.getQuantity() >= quantity; //true if the quantity in the store is bigger than the quantity a user want to add
    }

    public int calcPriceInStore(String productName, int quantity, String userId)
    {
        Product productToCalc = getProductByName(productName);
        return productToCalc.getPrice() * quantity;
    }

    public void removeProduct(String productName){
        synchronized (storeProductLock) {
            storeProducts.remove(productName);
        }
    }

    public void updateProduct(ProductDTO product){
        synchronized (storeProductLock) {
            storeProducts.get(product.getName()).setPrice(product.getPrice());
            storeProducts.get(product.getName()).setQuantity(product.getQuantity());
            storeProducts.get(product.getName()).setDescription(product.getDescription());
            Category category = Category.fromString(product.getCategoryStr());
            storeProducts.get(product.getName()).setCategory(category);
        }
    }

    public void removeProductQuantity(String productName,int quantity){
        synchronized (storeProductLock) {
            Product product = storeProducts.get(productName);
            if (product.getQuantity() >= quantity){
                product.setQuantity(product.getQuantity()-quantity);
            }
        }
    }

    public void closeStore()
    {
        synchronized (isOpenedLock) {
            this.isOpened = false;
        }


    }

    public void reopenStore()
    {
        synchronized (isOpenedLock) {
            this.isOpened = true;
        }


    }

    //  public void sendMessageToStaffOfStore(Notification notification) {
//        founder.notifyObserver(notification);
//        for (User u : getOwnersOfStore())
//            u.notifyObserver(notification);
//        for (User u : getManagersOfStore())
//            u.notifyObserver(notification);
    //  }


    public boolean getIsOpened()
    {
        synchronized (isOpenedLock) {
            return this.isOpened;
        }
    }

    public List<String> getProducts()
    {
        List<String> productsList;

        synchronized (storeProductLock) {
            Set<String> productsSet = storeProducts.keySet();
            productsList = new ArrayList<>(productsSet);
            return productsList;
        }
    }

    public int calcDiscountPolicy(UserDTO userDTO, List<ProductDTO> products)
    {
        return this.discountPolicy.calcDiscountPolicy(userDTO, products);
    }

    public boolean checkPurchasePolicy(UserDTO userDTO, List<ProductDTO> products)
    {
        return this.purchasePolicy.checkPurchasePolicy(userDTO, products);
    }

    public List<String> matchProducts(String productName, String categoryStr, List<String> keywords)
    {
        synchronized (storeProductLock) {
            List<Product> products = storeProducts.values().stream().toList();
            return products.stream()
                    .filter(product -> productName == null || product.getProductName().toLowerCase().contains(productName.toLowerCase()))
                    .filter(product -> categoryStr == null || product.getCategoryName().equalsIgnoreCase(categoryStr))
                    .filter(product -> keywords == null || keywords.stream().anyMatch(keyword -> product.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                    .map(Product::getProductName)
                    .collect(Collectors.toList());
        }
    }


    public List<ProductDTO> matchProductsDTO(String productName, String categoryStr, List<String> keywords)
    {
        //if keyword looks like ["food"]
        if (keywords.get(0).contains("[")) {
            keywords.set(0, keywords.get(0).replace("[", ""));
        }
        if (keywords.get(keywords.size() - 1).contains("]")) {
            keywords.set(keywords.size() - 1, keywords.get(keywords.size() - 1).replace("]", ""));
        }

        synchronized (storeProductLock) {
            List<Product> products = storeProducts.values().stream().toList();
            return products.stream()
                    .filter(product -> productName == null || product.getProductName().toLowerCase().contains(productName.toLowerCase()))
                    .filter(product -> categoryStr == null || product.getCategoryName().equalsIgnoreCase(categoryStr))
                    .filter(product -> keywords == null || keywords.stream().anyMatch(keyword -> product.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                    .map(Product::getProductDTO)
                    .collect(Collectors.toList());
        }
    }

    public List<String> filterProducts(String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double minRating, List<String> productsFromSearch, Double storeMinRating)
    {
        List<Product> products = new ArrayList<>();

        for (String productName : productsFromSearch) {
            synchronized (storeProductLock) {
                products.add(storeProducts.get(productName));
            }
        }
        return products.stream()
                .filter(product -> categoryStr == null || product.getCategoryName().equals(categoryStr.toUpperCase()))
                .filter(product -> keywords == null || keywords.stream().anyMatch(keyword -> product.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                .filter(product -> minPrice == null || product.getPrice() >= minPrice)
                .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
                .filter(product -> minRating == null || product.getRating() >= minRating)
                .filter(product -> storeMinRating == null || this.rating >= storeMinRating)
                .map(Product::getProductName)
                .collect(Collectors.toList());

    }

//    public void addReceipt(String receiptId, String userId)
//    {
//        synchronized (receiptId) {
//            receiptsIdsUserIds.put(receiptId, userId);
//        }
//    }


    public String getStore_ID() {
        return store_ID;
    }

    public double getRating() {
        return rating;
    }

    public int getNumOfRatings() {
        return numOfRatings;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }

    public String getDescription(){
        return description;
    }

    public void addPurchaseRule(List<Rule> rules, List<String> operators) {
        purchasePolicy.addRule(rules, operators);
    }

    public void removePurchaseRule(int ruleNum) {
        purchasePolicy.removeRule(ruleNum);
    }

    public void addDiscountCondRule(List<Rule> rules, List<String> logicalOperators, List<DiscountValue> discDetails, List<String> numericalOperators) {
        discountPolicy.addCondRule(rules, logicalOperators, discDetails, numericalOperators);
    }

    public void addDiscountSimple(List<DiscountValue> discDetails, List<String> numericalOperators) {
        discountPolicy.addSimple(discDetails, numericalOperators);
    }

    public void removeDiscountRule(int ruleNum) {
        discountPolicy.removeRule(ruleNum);
    }

    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public void setPurchasePolicy(PurchasePolicy purchasePolicy) {
        this.purchasePolicy = purchasePolicy;
    }

    public List<String> getStoreCurrentPurchaseRules() {
        return purchasePolicy.getRulesDescriptions();
    }

    public List<String> getStoreCurrentDiscountRules() {
        return discountPolicy.getRulesDescriptions();
    }

    public void composeCurrentPurchaseRules(int ruleIndex1, int ruleIndex2, String operator) {
        purchasePolicy.composeCurrentStoreRules(ruleIndex1, ruleIndex2, operator);
    }

    public void composeCurrentSimpleDiscountRules(int ruleIndex1, int ruleIndex2, String numericalOperator) {
        discountPolicy.composeCurrentSimpleDiscountRules(ruleIndex1, ruleIndex2, List.of(numericalOperator));
    }

    public void composeCurrentCondDiscountRules(int ruleIndex1, int ruleIndex2, String logicalOperator, String numericalOperator) {
        discountPolicy.composeCurrentCondDiscountRules(ruleIndex1, ruleIndex2, List.of(logicalOperator), List.of(numericalOperator));
    }

    public List<String> getStoreCurrentSimpleDiscountRules() {
        return discountPolicy.getSimpleDiscountRulesDescriptions();
    }

    public List<String> getStoreCurrentCondDiscountRules() {
        return discountPolicy.getCondDiscountRulesDescriptions();
    }
}
