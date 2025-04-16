package IntegrationTests.Store.DiscountPolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.SimpleRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.TestRule;
import DomainLayer.Store.StoreDiscountPolicy.CondDiscount;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.SimpleDiscountValue;
import Util.ProductDTO;
import Util.TestRuleDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CondDiscountTest {

    private List<ProductDTO> products;
    private UserDTO user;
    private TestRuleDTO testRule1;
    private TestRuleDTO testRule2;
    private TestRuleDTO testRule3;
    private int percentage1;
    private int percentage2;
    private int percentage3;
    private int product1Price;
    private int bunPrice;
    private int pastaPrice;
    private int breadPrice;

    private int totalPrice;

    @BeforeEach
    public void setUp() {
        //initialize percentage and product prices
        percentage1 = 50;
        percentage2 = 20;
        percentage3 = 30;
        product1Price = 100;
        bunPrice = 10;
        pastaPrice = 10;
        breadPrice = 20;
        totalPrice = product1Price + bunPrice + pastaPrice + breadPrice;

        // Initialize products for testing
        products = new ArrayList<>();
        products.add(new ProductDTO("product1", product1Price, 2, "desc1", "TOYS"));
        products.add(new ProductDTO("bun", bunPrice, 6, "desc2", "FOOD"));
        products.add(new ProductDTO("pasta", pastaPrice, 4, "desc3", "FOOD"));
        products.add(new ProductDTO("bread", breadPrice, 3, "desc4", "FOOD"));

        // Initialize user for testing
        user = new UserDTO();

        //initialize testRule
        testRule1 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
        testRule2 = new TestRuleDTO("Amount", "Above", null, "pasta", "Basket must contain more than 3 pasta", true, null, 3, null, null, null);
        testRule3 = new TestRuleDTO("Time", "Above", null, null, "Discount after 23:00", true, null, null, null, null, LocalTime.of(23, 0));
    }

    @Test
    public void testCalcDiscountWithValidRule() {
        // Arrange
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(percentage1, null, true, null));

        List<Rule> rules = new ArrayList<>();
        rules.add(new SimpleRule(testRule1));

        List<String> discountValueOperators = new ArrayList<>();
        List<String> discountRuleOperators = new ArrayList<>();

        // Create CondDiscount
        CondDiscount condDiscount = new CondDiscount(discDetails, discountValueOperators, rules, discountRuleOperators);

        // Act
        int totalDiscount = condDiscount.calcDiscount(products, user);

        // Assert
        // Expected discount calculation:
        // Total price of products: 100 + 10 + 10 + 20 = 140
        // 50% discount: 140 * 0.5 = 70

        int expectedDiscount = (int) Math.round(totalPrice * percentage1 / 100.0);
        assertEquals(expectedDiscount, totalDiscount);
    }

    @Test
    public void testCalcDiscountWithInvalidRule() {
        // Arrange
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(percentage1, null, true, null));

        List<Rule> rules = new ArrayList<>();
        rules.add(new SimpleRule(testRule1));

        List<String> discountValueOperators = new ArrayList<>();
        List<String> discountRuleOperators = new ArrayList<>();

        // Create CondDiscount
        CondDiscount condDiscount = new CondDiscount(discDetails, discountValueOperators, rules, discountRuleOperators);

        // Modify products to invalidate the rule
        products.get(1).setQuantity(4); // Reduce buns to less than 5

        // Act
        int totalDiscount = condDiscount.calcDiscount(products, user);

        // Assert
        // No discount should be applied because the rule is not met
        assertEquals(0, totalDiscount);
    }

    @Test
    public void testCalcDiscountWithMultipleRules() {
        // Arrange
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(percentage2, null, true, null));

        List<Rule> rules = new ArrayList<>();
        rules.add(new SimpleRule(testRule1));
        rules.add(new SimpleRule(testRule2));

        List<String> discountValueOperators = new ArrayList<>();
        List<String> discountRuleOperators = new ArrayList<>();
        discountRuleOperators.add("AND");

        // Create CondDiscount
        CondDiscount condDiscount = new CondDiscount(discDetails, discountValueOperators, rules, discountRuleOperators);

        // Act
        int totalDiscount = condDiscount.calcDiscount(products, user);

        // Assert
        // Expected discount calculation:
        // Total price of products: 100 + 10 + 10 + 20 = 140
        // 20% discount: 140 * 0.2 = 28
        int expectedDiscount = (int) Math.round(totalPrice * percentage2 / 100.0);
        assertEquals(expectedDiscount, totalDiscount);
    }

    @Test
    public void testCalcDiscountWithTimeBasedRule() {
        // Arrange
        // Set the clock to a specific time after 23:00
        Clock fixedClock = Clock.fixed(LocalDateTime.of(2023, 6, 15, 23, 30).atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(30, null, true, null));

        List<Rule> rules = new ArrayList<>();
        TestRule.setClock(fixedClock);
        rules.add(new SimpleRule(testRule3));

        List<String> discountValueOperators = new ArrayList<>();
        List<String> discountRuleOperators = new ArrayList<>();

        // Create CondDiscount
        CondDiscount condDiscount = new CondDiscount(discDetails, discountValueOperators, rules, discountRuleOperators);

        // Act
        int totalDiscount = condDiscount.calcDiscount(products, user);

        // Assert
        // Expected discount calculation:
        // Total price of products: 100 + 10 + 10 + 20 = 140
        // 30% discount: 140 * 0.3 = 42

        int expectedDiscount = (int) Math.round(totalPrice * percentage3 / 100.0);
        assertEquals(expectedDiscount, totalDiscount);
    }

    @Test
    public void testCalcDiscountWithTimeBasedRuleBeforeTime() {
        // Arrange
        // Set the clock to a specific time before 23:00
        Clock fixedClock = Clock.fixed(LocalDateTime.of(2023, 6, 15, 22, 30).atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());

        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(percentage3, null, true, null));

        List<Rule> rules = new ArrayList<>();
        rules.add(new SimpleRule(testRule3));
        TestRule.setClock(fixedClock);

        List<String> discountValueOperators = new ArrayList<>();
        List<String> discountRuleOperators = new ArrayList<>();

        // Create CondDiscount
        CondDiscount condDiscount = new CondDiscount(discDetails, discountValueOperators, rules, discountRuleOperators);

        // Act
        int totalDiscount = condDiscount.calcDiscount(products, user);

        // Assert
        // No discount should be applied because the rule is not met (before 23:00)
        assertEquals(0, totalDiscount);
    }
}
