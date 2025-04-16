package DomainLayer.Store.LogicalConditions;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DomainLayer.Store.PoliciesRulesLogicalConditions.CondRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class CondRuleTest {

    private Rule rule1;
    private Rule rule2;
    private CondRule condRule;
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

        condRule = new CondRule(rule1, rule2);
    }

    @Test
    void checkRule_BothRulesTrue_ReturnsTrue() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(true);

        // Act
        ProductDTO productDTO = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        UserDTO userDTO = new UserDTO();
        List<ProductDTO> basketProducts = List.of(productDTO);
        boolean result = condRule.checkRule(userDTO, basketProducts);

        // Assert
        assertTrue(result);
    }

    @Test
    void checkRule_FirstRuleFalse_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(false);

        // Act
        ProductDTO productDTO = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        UserDTO userDTO = new UserDTO();
        List<ProductDTO> basketProducts = List.of(productDTO);
        boolean result = condRule.checkRule(userDTO, basketProducts);

        // Assert
        assertTrue(result);
        // Verify that rule2 is not checked if rule1 is false
        verify(rule2, never()).checkRule(any(), any());
    }

    @Test
    void checkRule_FirstRuleTrueSecondRuleFalse_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(false);

        // Act
        ProductDTO productDTO = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        UserDTO userDTO = new UserDTO();
        List<ProductDTO> basketProducts = List.of(productDTO);
        boolean result = condRule.checkRule(userDTO, basketProducts);

        // Assert
        assertFalse(result);
    }

    @Test
    void getDescription_ReturnsCombinedDescription() {
        // Act
        String description = condRule.getDescription();

        // Assert
        assertEquals(" ("+desc1 +" only if "+ desc2+") ", description);
    }
}
