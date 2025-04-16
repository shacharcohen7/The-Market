package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.Entity;

import java.util.List;
import java.util.function.Predicate;

@Entity
public class SimpleDiscountValue extends DiscountValue {

    private final int percentage;
    private final Category category;
    private final boolean isStoreDiscount;
    private final List<String> productsNames;


    public SimpleDiscountValue(int percentage, Category category, boolean isStoreDiscount, List<String> productsNames) {
        super(generateDescription(percentage, category, isStoreDiscount, productsNames));
        this.percentage = percentage;
        this.category = category;
        this.isStoreDiscount = isStoreDiscount;
        this.productsNames = productsNames;
    }

    public SimpleDiscountValue() {
        this(0, null, false, null);
    }

    public int calcDiscount(List<ProductDTO> basketProducts) {
        int totalPrice = 0;

        if (isCategoryDiscount()) {
            totalPrice = calculateTotalPriceByCriterion(basketProducts, product -> product.getCategoryStr().equals(getCategory().toString()));
        } else if (isProductsDiscount()) {
            totalPrice = calculateTotalPriceByCriterion(basketProducts, product -> getProductsNames().contains(product.getName()));
        } else {
            totalPrice = calculateTotalPriceByCriterion(basketProducts, product -> true);
        }

        return applyDiscount(totalPrice, getPercentage());
    }

    private int calculateTotalPriceByCriterion(List<ProductDTO> basketProducts, Predicate<ProductDTO> criterion) {
        return basketProducts.stream()
                .filter(criterion)
                .mapToInt(ProductDTO::getPrice)
                .sum();
    }

    private int applyDiscount(int totalPrice, int percentage) {
        return totalPrice * percentage / 100;
    }

    private static String generateDescription(int percentage, Category category, boolean isStoreDiscount, List<String> productsNames) {
        if (category != null) {
            return "Discount of " + percentage + "% on " + category.toString();
        } else if (productsNames != null) {
            return "Discount of " + percentage + "% on " + productsNames.toString();
        } else {
            return "Discount of " + percentage + "% on all store";
        }
    }

    public int getPercentage() {
        return percentage;
    }

    public boolean isStoreDiscount() {
        return isStoreDiscount;
    }

    private Category getCategory() {
        return category;
    }

    private List<String> getProductsNames() {
        return productsNames;
    }

    public boolean isCategoryDiscount() {
        return category != null;
    }

    public boolean isProductsDiscount() {
        return productsNames != null;
    }
}
