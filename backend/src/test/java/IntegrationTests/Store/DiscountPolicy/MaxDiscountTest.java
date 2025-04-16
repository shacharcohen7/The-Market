package IntegrationTests.Store.DiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.MaxDiscount;
import DomainLayer.Store.StoreDiscountPolicy.SimpleDiscountValue;
import Util.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MaxDiscountTest {

    private List<ProductDTO> products;
    private int percentage1;
    private int percentage2;
    private int product1Price;
    private int product2Price;
    private int product3Price;

    @BeforeEach
    public void setUp() {
        //initialize percentage and product prices
        percentage1 = 50;
        percentage2 = 20;
        product1Price = 100;
        product2Price = 200;
        product3Price = 300;

        // Initialize products for testing
        products = new ArrayList<>();
        products.add(new ProductDTO("product1", product1Price, 2, "desc1", "TOYS"));
        products.add(new ProductDTO("product2", product2Price, 3, "desc2", "BOOKS"));
        products.add(new ProductDTO("product3", product3Price, 4, "desc3", "TOYS"));
    }

    @Test
    public void testCalcDiscount() {
        // Create a MaxDiscount with specific discount values
        DiscountValue discountValue1 = new SimpleDiscountValue(percentage1, Category.TOYS, false, null);
        DiscountValue discountValue2 = new SimpleDiscountValue(percentage2, Category.BOOKS, false, null);
        MaxDiscount maxDiscount = new MaxDiscount(discountValue1, discountValue2);

        // Calculate discount
        int totalDiscount = maxDiscount.calcDiscount(products);

        // Expected discount calculation:
        // TOYS category discount: (100 + 300) * 0.5 = 200
        int expectedToyDiscount = (product1Price + product3Price) * percentage1 / 100;
        // BOOKS category discount: 200 * 0.2 = 40
        int expectedBookDiscount = product2Price * percentage2 / 100;
        // Maximum discount: max(200, 40) = 200
        int maxDiscountValue = Math.max(expectedToyDiscount, expectedBookDiscount);
        assertEquals(maxDiscountValue, totalDiscount);
    }

    @Test
    public void testCalcDiscountNoMatchingProducts() {
        // Create a MaxDiscount with discount values that won't match any products
        DiscountValue discountValue1 = new SimpleDiscountValue(percentage1, Category.ELECTRONICS, false, null);
        DiscountValue discountValue2 = new SimpleDiscountValue(percentage2, Category.FOOD, false, null);
        MaxDiscount maxDiscount = new MaxDiscount(discountValue1, discountValue2);

        // Calculate discount
        int totalDiscount = maxDiscount.calcDiscount(products);

        // Expected discount calculation:
        // No products match 'ELECTRONICS' or 'FOOD', so total discount should be 0
        assertEquals(0, totalDiscount);
    }
}
