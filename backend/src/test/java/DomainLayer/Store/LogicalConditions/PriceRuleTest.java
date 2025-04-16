package DomainLayer.Store.LogicalConditions;

import DomainLayer.Store.Category;
import DomainLayer.Store.PoliciesRulesLogicalConditions.PriceRule;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PriceRuleTest {
    private List<ProductDTO> products;

    @BeforeEach
    public void setUp() {
        products = new ArrayList<>();

        // Set up products with specific prices
        products.add(new ProductDTO("Beer", 10, 5, "Alcohol", "ALCOHOL"));
        products.add(new ProductDTO("Wine", 20, 3, "Alcohol", "ALCOHOL"));
    }

    @Test
    public void testPriceAbove() {
        int price = 25;
        PriceRule rule = new PriceRule(price, "Above", Category.ALCOHOL, null, "Total price must be above 25", true);
        assertTrue(rule.test(null, products));
    }

    @Test
    public void testPriceBelow() {
        int price = 50;
        PriceRule rule = new PriceRule(price, "Below", Category.ALCOHOL, null, "Total price must be below 50", true);
        assertTrue(rule.test(null, products));
    }

    @Test
    public void testPriceExact() {
        int price = 30;
        PriceRule rule = new PriceRule(price, "Exact", Category.ALCOHOL, null, "Total price must be exactly 30", true);
        assertTrue(rule.test(null, products));
    }

    @Test
    public void testPriceAboveFail() {
        int price = 50;
        PriceRule rule = new PriceRule(price, "Above", Category.ALCOHOL, null, "Total price must be above 50", true);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testPriceBelowFail() {
        int price = 25;
        PriceRule rule = new PriceRule(price, "Below", Category.ALCOHOL, null, "Total price must be below 25", true);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testPriceExactFail() {
        int price = 25;
        PriceRule rule = new PriceRule(price, "Exact", Category.ALCOHOL, null, "Total price must be exactly 25", true);
        assertFalse(rule.test(null, products));
    }
}

