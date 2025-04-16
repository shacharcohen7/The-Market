package DomainLayer.Store.LogicalConditions;

import DomainLayer.Store.Category;
import DomainLayer.Store.PoliciesRulesLogicalConditions.AmountRule;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AmountRuleTest {
    private List<ProductDTO> products;

    @BeforeEach
    public void setUp() {
        products = new ArrayList<>();

        // Set up products
        products.add(new ProductDTO("Beer", 10, 5, "Alcohol", "ALCOHOL"));
        products.add(new ProductDTO("Wine", 20, 3, "Alcohol", "ALCOHOL"));
    }

    @Test
    public void testQuantityAboveContainsTrue() {
        AmountRule rule = new AmountRule(7, "Above", Category.ALCOHOL, null, "Quantity must be above 7", true);
        assertTrue(rule.test(null, products));
    }

    @Test
    public void testQuantityBelowContainsTrue() {
        AmountRule rule = new AmountRule(10, "Below", Category.ALCOHOL, null, "Quantity must be below 10", true);
        assertTrue(rule.test(null, products));
    }

    @Test
    public void testQuantityExactContainsTrue() {
        AmountRule rule = new AmountRule(8, "Exact", Category.ALCOHOL, null, "Quantity must be exactly 8", true);
        assertTrue(rule.test(null, products));
    }

    @Test
    public void testQuantityAboveContainsFalse() {
        AmountRule rule = new AmountRule(15, "Above", Category.ALCOHOL, null, "Quantity must be above 15", true);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testQuantityBelowContainsFalse() {
        AmountRule rule = new AmountRule(5, "Below", Category.ALCOHOL, null, "Quantity must be below 5", true);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testQuantityExactContainsFalse() {
        AmountRule rule = new AmountRule(6, "Exact", Category.ALCOHOL, null, "Quantity must be exactly 6", true);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testQuantityAboveContainsFalseCondition() {
        AmountRule rule = new AmountRule(7, "Above", Category.ALCOHOL, null, "Quantity must not be above 7", false);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testQuantityBelowContainsFalseCondition() {
        AmountRule rule = new AmountRule(10, "Below", Category.ALCOHOL, null, "Quantity must not be below 10", false);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testQuantityExactContainsFalseCondition() {
        AmountRule rule = new AmountRule(8, "Exact", Category.ALCOHOL, null, "Quantity must not be exactly 8", false);
        assertFalse(rule.test(null, products));
    }
}