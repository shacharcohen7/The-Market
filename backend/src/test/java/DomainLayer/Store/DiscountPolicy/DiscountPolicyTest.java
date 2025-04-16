package DomainLayer.Store.DiscountPolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.StoreDiscountPolicy.CondDiscount;
import DomainLayer.Store.StoreDiscountPolicy.Discount;
import DomainLayer.Store.StoreDiscountPolicy.DiscountPolicy;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DiscountPolicyTest {

    @Mock
    private Discount discount;

    @Mock
    private CondDiscount condDiscount;

    @Mock
    private Rule rule;

    @Mock
    private DiscountValue discountValue;

    private DiscountPolicy discountPolicy;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        discountPolicy = new DiscountPolicy();
    }

    @Test
    public void testCalcDiscountPolicy() {
        // Arrange
        UserDTO userDTO = new UserDTO(); // Assume UserDTO has a default constructor
        ProductDTO product1 = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        ProductDTO product2 = new ProductDTO("Product 2", 200, 1, "Description 2", "Category 2");
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);

        // Mock the behavior of the discount objects
        when(discount.calcDiscount(basketProducts, userDTO)).thenReturn(50);
        when(condDiscount.calcDiscount(basketProducts, userDTO)).thenReturn(30);

        // Directly inject the mocks into the discountRules list
        discountPolicy.addRule(discount);
        discountPolicy.addRule(condDiscount);

        // Act
        int totalDiscount = discountPolicy.calcDiscountPolicy(userDTO, basketProducts);

        // Assert
        assertEquals(80, totalDiscount);
    }

    @Test
    public void testAddCondRule() {
        // Arrange
        List<Rule> rules = Arrays.asList(rule);
        List<String> logicalOperators = Arrays.asList();
        List<DiscountValue> discDetails = Arrays.asList(discountValue);
        List<String> numericalOperators = Arrays.asList();

        // Act
        discountPolicy.addCondRule(rules, logicalOperators, discDetails, numericalOperators);

        // Assert
        assertEquals(1, discountPolicy.getDiscountRules().size());
        assertEquals(CondDiscount.class, discountPolicy.getDiscountRules().get(0).getClass());
    }

    @Test
    public void testAddSimple() {
        // Arrange
        List<DiscountValue> discDetails = Arrays.asList(discountValue);
        List<String> discountValueOperators = Arrays.asList();

        // Act
        discountPolicy.addSimple(discDetails, discountValueOperators);

        // Assert
        assertEquals(1, discountPolicy.getDiscountRules().size());
        assertEquals(Discount.class, discountPolicy.getDiscountRules().get(0).getClass());
    }

    @Test
    public void testRemoveRule() {
        // Arrange
        List<DiscountValue> discDetails = Arrays.asList(discountValue);
        List<String> discountValueOperators = Arrays.asList();
        discountPolicy.addSimple(discDetails, discountValueOperators);

        // Act
        discountPolicy.removeRule(0);

        // Assert
        assertEquals(0, discountPolicy.getDiscountRules().size());
    }

    @Test
    public void testComposeCurrentCondDiscountRules() {
        // Arrange
        List<Rule> rules1 = Arrays.asList(rule);
        List<String> logicalOperators1 = Arrays.asList();
        List<DiscountValue> discDetails1 = Arrays.asList(discountValue);
        List<String> numericalOperators1 = Arrays.asList();

        CondDiscount condDiscount1 = new CondDiscount(discDetails1, numericalOperators1, rules1, logicalOperators1);
        discountPolicy.addRule(condDiscount1);

        List<Rule> rules2 = Arrays.asList(rule);
        List<String> logicalOperators2 = Arrays.asList();
        List<DiscountValue> discDetails2 = Arrays.asList(discountValue);
        List<String> numericalOperators2 = Arrays.asList();

        CondDiscount condDiscount2 = new CondDiscount(discDetails2, numericalOperators2, rules2, logicalOperators2);
        discountPolicy.addRule(condDiscount2);

        List<String> logicalOperators = Arrays.asList("AND");
        List<String> numericalOperators = Arrays.asList("ADDITION");

        // Act
        discountPolicy.composeCurrentCondDiscountRules(0, 1, logicalOperators, numericalOperators);

        // Assert
        assertEquals(1, discountPolicy.getDiscountRules().size());
        assertEquals(CondDiscount.class, discountPolicy.getDiscountRules().get(0).getClass());
    }

    @Test
    public void testComposeCurrentCondDiscountRulesInvalidIndex() {
        // Arrange
        List<String> logicalOperators = Arrays.asList("AND");
        List<String> numericalOperators = Arrays.asList("ADDITION");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> discountPolicy.composeCurrentCondDiscountRules(0, 1, logicalOperators, numericalOperators));
    }

    @Test
    public void testComposeCurrentSimpleDiscountRules() {
        // Arrange
        List<DiscountValue> discDetails1 = Arrays.asList(discountValue);
        List<String> discountValueOperators1 = Arrays.asList();

        Discount discount1 = new Discount(discDetails1, discountValueOperators1);
        discountPolicy.addRule(discount1);

        List<DiscountValue> discDetails2 = Arrays.asList(discountValue);
        List<String> discountValueOperators2 = Arrays.asList();

        Discount discount2 = new Discount(discDetails2, discountValueOperators2);
        discountPolicy.addRule(discount2);

        List<String> discountValueOperators = Arrays.asList("ADDITION");

        // Act
        discountPolicy.composeCurrentSimpleDiscountRules(0, 1, discountValueOperators);

        // Assert
        assertEquals(1, discountPolicy.getDiscountRules().size());
        assertEquals(Discount.class, discountPolicy.getDiscountRules().get(0).getClass());
    }

    @Test
    public void testComposeCurrentSimpleDiscountRulesInvalidIndex() {
        // Arrange
        List<String> discountValueOperators = Arrays.asList("ADDITION");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> discountPolicy.composeCurrentSimpleDiscountRules(0, 1, discountValueOperators));
    }
}
