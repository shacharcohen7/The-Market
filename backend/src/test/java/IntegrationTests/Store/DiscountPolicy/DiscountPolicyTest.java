package IntegrationTests.Store.DiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.SimpleRule;
import DomainLayer.Store.StoreDiscountPolicy.DiscountPolicy;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.SimpleDiscountValue;
import Util.ProductDTO;
import Util.TestRuleDTO;
import Util.UserDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DiscountPolicyTest {

    private DiscountPolicy discountPolicy;
    int percentage1;
    int percentage2;
    int percentage3;
    int product1Price;
    int product2Price;
    int product3Price;
    TestRuleDTO testRule1;
    TestRuleDTO testRule2;

    @BeforeEach
    public void setUp() {
        discountPolicy = new DiscountPolicy();
        percentage1 = 50;
        percentage2 = 30;
        percentage3 = 10;
        product1Price = 100;
        product2Price = 50;
        product3Price = 200;

        // Add some sample rules for testing
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(percentage1, null, true, null));
        List<String> discountValueOperators = new ArrayList<>();
        discountPolicy.addSimple(discDetails, discountValueOperators);

        //initialize testRule1 and testRule2
        testRule1 = new TestRuleDTO("Amount", "Below", null, "tomato", "basket contains less than 5 tomatoes", true, null, 5, null, null, null);
        testRule2 = new TestRuleDTO("Amount", "Above", "TOYS", null, "basket contains more than 2 TOYS", true, null, 2, null, null, null);

    }

    @Test
    public void testAddRule() {
        assertEquals(1, discountPolicy.getDiscountRules().size());

        // Add another rule
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(percentage2, null, true, null));
        discDetails.add(new SimpleDiscountValue(percentage3, Category.FOOD, false, null));

        List<String> discountValueOperators = new ArrayList<>();
        discountValueOperators.add("AND");
        discountPolicy.addSimple(discDetails, discountValueOperators);

        assertEquals(2, discountPolicy.getDiscountRules().size());
    }

    @Test
    public void testRemoveRule() {
        assertEquals(1, discountPolicy.getDiscountRules().size());

        // Remove the existing rule
        discountPolicy.removeRule(0);

        assertEquals(0, discountPolicy.getDiscountRules().size());
    }

    @Test
    public void testCalcDiscountPolicy() {
        // Create a UserDTO
        UserDTO user = new UserDTO();
        user.setUserId("123");

        // Create some ProductDTOs
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO("product1", product1Price, 2, "desc1", "TOYS"));
        products.add(new ProductDTO("product2", product2Price, 5, "desc2", "TOYS"));

        // Calculate discount
        int totalDiscount = discountPolicy.calcDiscountPolicy(user, products);

        int expectedDiscount = (product1Price + product2Price) * percentage1 / 100;
        assertEquals(expectedDiscount, totalDiscount); //50% discount on all store products

        // Add another rule that matches the products
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(percentage3, Category.TOYS, false, null));
        discountPolicy.addSimple(discDetails, new ArrayList<>());

        // Calculate discount again
        totalDiscount = discountPolicy.calcDiscountPolicy(user, products);

        int expectedDiscount2 = (product1Price + product2Price) * percentage1 / 100 + (product1Price + product2Price) * percentage3 / 100;
        assertEquals(expectedDiscount2, totalDiscount); //50% discount on all store products + 10% discount on TOYS category
    }

    @Test
    public void testComposeCurrentSimpleDiscountRules() {
        // Create a UserDTO
        UserDTO user = new UserDTO();
        user.setUserId("123");

        // Create some ProductDTOs
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO("product1", product1Price, 2, "desc1", "TOYS"));
        products.add(new ProductDTO("product2", product2Price, 5, "desc2", "TOYS"));

        // Add another rule
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(percentage2, null, true, null));
        List<String> discountValueOperators = new ArrayList<>();
        discountPolicy.addSimple(discDetails, discountValueOperators);

        List<String> composedRuleDiscountValueOperators = new ArrayList<>();
        composedRuleDiscountValueOperators.add("MAX");
        // Compose the first two rules
        discountPolicy.composeCurrentSimpleDiscountRules(0, 1, composedRuleDiscountValueOperators);


        // Calculate discount
        int totalDiscount = discountPolicy.calcDiscountPolicy(user, products);

        int expectedDiscount = Math.max((product1Price + product2Price) * percentage1 / 100, (product1Price + product2Price) * percentage2 / 100);

        assertEquals(1, discountPolicy.getDiscountRules().size());
        assertEquals(expectedDiscount, totalDiscount);
    }

    @Test
    public void testComposeCurrentCondDiscountRules() {
        // Create a UserDTO
        UserDTO user = new UserDTO();
        user.setUserId("123");

        // Create some ProductDTOs
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO("product1", product1Price, 2, "desc1", "TOYS"));
        products.add(new ProductDTO("product2", product2Price, 5, "desc2", "TOYS"));
        products.add(new ProductDTO("product3", product3Price, 5, "desc2", "TOYS"));

        //remove the first rule
        discountPolicy.removeRule(0);

        // Add rule 1
        List<DiscountValue> discDetails1 = new ArrayList<>();
        discDetails1.add(new SimpleDiscountValue(percentage2, null, true, null));
        // Creating simple rules
        Rule rule1 = new SimpleRule(testRule1); //basket contains less than 5 tomatoes

        discountPolicy.addCondRule(List.of(rule1), new ArrayList<>(), discDetails1, new ArrayList<>());

        // Add rule 2
        List<DiscountValue> discDetails2 = new ArrayList<>();
        discDetails2.add(new SimpleDiscountValue(percentage3, null, true, null));
        // Creating simple rules
        Rule rule2 = new SimpleRule(testRule2); //basket contains more than 2 TOYS

        discountPolicy.addCondRule(List.of(rule2), new ArrayList<>(), discDetails2, new ArrayList<>());

        // Compose the two new rules
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        discountPolicy.composeCurrentCondDiscountRules(0, 1, logicalOperators, numericalOperators);

        // Calculate discount
        int totalDiscount = discountPolicy.calcDiscountPolicy(user, products);
        int expectedDiscount = Math.max((product1Price + product2Price + product3Price) * percentage2 / 100, (product1Price + product2Price + product3Price) * percentage3 / 100);

        assertEquals(1, discountPolicy.getDiscountRules().size());
        assertEquals(expectedDiscount, totalDiscount);
    }
}
