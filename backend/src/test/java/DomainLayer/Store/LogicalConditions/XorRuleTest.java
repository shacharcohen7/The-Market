package DomainLayer.Store.LogicalConditions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.XorRule;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class XorRuleTest {

    private Rule rule1;
    private Rule rule2;
    private XorRule xorRule;
    private String desc1;
    private String desc2;
    @BeforeEach
    void setUp() {
        rule1 = mock(Rule.class);
        rule2 = mock(Rule.class);
        desc1 = "Rule 1";
        desc2 = "Rule 2";

        when(rule1.getDescription()).thenReturn("Rule 1");
        when(rule2.getDescription()).thenReturn("Rule 2");

        xorRule = new XorRule(rule1, rule2);
    }

    @Test
    void checkRule_BothRulesTrue_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(true);

        // Act
        ProductDTO productDTO = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        UserDTO userDTO = new UserDTO();
        List<ProductDTO> basketProducts = List.of(productDTO);
        boolean result = xorRule.checkRule(userDTO, basketProducts);

        // Assert
        assertFalse(result);
    }

    @Test
    void checkRule_FirstRuleTrueSecondRuleFalse_ReturnsTrue() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(false);

        // Act
        ProductDTO productDTO = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        UserDTO userDTO = new UserDTO();
        List<ProductDTO> basketProducts = List.of(productDTO);
        boolean result = xorRule.checkRule(userDTO, basketProducts);

        // Assert
        assertTrue(result);
    }

    @Test
    void checkRule_FirstRuleFalseSecondRuleTrue_ReturnsTrue() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(false);
        when(rule2.checkRule(any(), any())).thenReturn(true);

        // Act
        ProductDTO productDTO = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        UserDTO userDTO = new UserDTO();
        List<ProductDTO> basketProducts = List.of(productDTO);
        boolean result = xorRule.checkRule(userDTO, basketProducts);

        // Assert
        assertTrue(result);
    }

    @Test
    void checkRule_BothRulesFalse_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(false);
        when(rule2.checkRule(any(), any())).thenReturn(false);

        // Act
        ProductDTO productDTO = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        UserDTO userDTO = new UserDTO();
        List<ProductDTO> basketProducts = List.of(productDTO);
        boolean result = xorRule.checkRule(userDTO, basketProducts);

        // Assert
        assertFalse(result);
    }

    @Test
    void getDescription_ReturnsCombinedDescription() {
        // Act
        String description = xorRule.getDescription();

        // Assert
        assertEquals(" ("+desc1+" xor "+desc2+") ", description);

    }
}

