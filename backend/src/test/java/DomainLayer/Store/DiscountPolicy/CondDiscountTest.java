package DomainLayer.Store.DiscountPolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.AndRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.CondRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.OrRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.StoreDiscountPolicy.CondDiscount;
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
import static org.mockito.Mockito.*;

public class CondDiscountTest {

    @Mock
    private Rule mockRule1;

    @Mock
    private Rule mockRule2;

    @Mock
    private DiscountValue discountValue1;

    @Mock
    private DiscountValue discountValue2;

    private CondDiscount condDiscount;

    private String rule1Description;
    private String rule2Description;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        List<DiscountValue> discountValues = Arrays.asList(discountValue1, discountValue2);
        List<String> discountValueOperators = Arrays.asList("ADDITION");
        List<Rule> discountRules = Arrays.asList(mockRule1, mockRule2);
        List<String> discountRuleOperators = Arrays.asList("AND");
        // Arrange
        rule1Description = "Rule1 Description";
        rule2Description = "Rule2 Description";

        when(mockRule1.getDescription()).thenReturn(rule1Description);
        when(mockRule2.getDescription()).thenReturn(rule2Description);

        condDiscount = new CondDiscount(discountValues, discountValueOperators, discountRules, discountRuleOperators);
    }

    @Test
    public void testCalcDiscount_RuleSatisfied() {
        // Arrange
        UserDTO userDTO = new UserDTO(); // Assume UserDTO has a default constructor
        ProductDTO product1 = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        ProductDTO product2 = new ProductDTO("Product 2", 200, 1, "Description 2", "Category 2");
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);

        when(mockRule1.checkRule(userDTO, basketProducts)).thenReturn(true);
        when(mockRule2.checkRule(userDTO, basketProducts)).thenReturn(true);
        when(discountValue1.calcDiscount(basketProducts)).thenReturn(50);
        when(discountValue2.calcDiscount(basketProducts)).thenReturn(30);

        // Act
        int totalDiscount = condDiscount.calcDiscount(basketProducts, userDTO);

        // Assert
        assertEquals(80, totalDiscount);

        // Verify that the checkRule methods were called
        verify(mockRule1).checkRule(userDTO, basketProducts);
        verify(mockRule2).checkRule(userDTO, basketProducts);
    }

    @Test
    public void testCalcDiscount_RuleNotSatisfied() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        ProductDTO product1 = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        ProductDTO product2 = new ProductDTO("Product 2", 200, 1, "Description 2", "Category 2");
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);

        when(mockRule1.checkRule(userDTO, basketProducts)).thenReturn(true);
        when(mockRule2.checkRule(userDTO, basketProducts)).thenReturn(false);

        // Act
        int totalDiscount = condDiscount.calcDiscount(basketProducts, userDTO);

        // Assert
        assertEquals(0, totalDiscount);

        // Verify that the checkRule methods were called
        verify(mockRule1).checkRule(userDTO, basketProducts);
        verify(mockRule2).checkRule(userDTO, basketProducts);
    }

    @Test
    public void testGetDiscountRulesDescriptions() {
        // Act
        String description = condDiscount.getDiscountRulesDescriptions();

        // Assert
        assertEquals(" (" + rule1Description + " and " + rule2Description + ") ", description);
    }
}
