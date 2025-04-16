package DomainLayer.Store.PurchasePolicy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.StorePurchasePolicy.PurchasePolicy;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class PurchasePolicyTest {

    private PurchasePolicy purchasePolicy;
    private Rule rule1;
    private Rule rule2;
    private Rule rule3;

    @BeforeEach
    void setUp() {
        purchasePolicy = new PurchasePolicy();
        rule1 = mock(Rule.class);
        rule2 = mock(Rule.class);
        rule3 = mock(Rule.class);
    }

    @Test
    void checkPurchasePolicy_AllRulesPass_ReturnsTrue() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(true);
        purchasePolicy.addRule(List.of(rule1, rule2), List.of("AND"));

        // Act
        boolean result = purchasePolicy.checkPurchasePolicy(new UserDTO("userId12345", "BestUser", "12/12/12", "Israel", "Bash", "Mesada", "Toy"), new ArrayList<>());

        // Assert
        assertTrue(result);
    }

    @Test
    void checkPurchasePolicy_AnyRuleFails_ReturnsFalse() {
        // Arrange
        when(rule1.checkRule(any(), any())).thenReturn(true);
        when(rule2.checkRule(any(), any())).thenReturn(false);
        purchasePolicy.addRule(List.of(rule1, rule2), List.of("AND"));

        // Act
        boolean result = purchasePolicy.checkPurchasePolicy(new UserDTO("userId12345", "BestUser", "12/12/12", "Israel", "Bash", "Mesada", "Toy"), new ArrayList<>());

        // Assert
        assertFalse(result);
    }

    @Test
    void addRule_WithAndCondition() {
        // Arrange
        purchasePolicy.addRule(List.of(rule1, rule2), List.of("AND"));

        // Act
        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        // Assert
        assertEquals(1, descriptions.size());
    }

    @Test
    void addRule_WithOrCondition() {
        // Arrange
        purchasePolicy.addRule(List.of(rule1, rule2), List.of("OR"));

        // Act
        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        // Assert
        assertEquals(1, descriptions.size());
    }

    @Test
    void addRule_WithCondCondition() {
        // Arrange
        purchasePolicy.addRule(List.of(rule1, rule2), List.of("ONLY IF"));

        // Act
        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        // Assert
        assertEquals(1, descriptions.size());
    }

    @Test
    void getRulesDescriptions_ReturnsDescriptions() {
        // Arrange
        when(rule1.getDescription()).thenReturn("Rule 1");
        when(rule2.getDescription()).thenReturn("Rule 2");
        purchasePolicy.addRule(List.of(rule1, rule2), List.of("AND"));

        // Act
        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        // Assert
        assertEquals(1, descriptions.size());
        assertEquals(" (Rule 1 and Rule 2) ", descriptions.get(0));
    }

    @Test
    void removeRule_RemovesCorrectRule() {
        // Arrange
        purchasePolicy.addRule(List.of(rule1), new ArrayList<>());
        purchasePolicy.addRule(List.of(rule2), new ArrayList<>());

        // Act
        purchasePolicy.removeRule(0);
        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        // Assert
        assertEquals(1, descriptions.size());
        assertNotEquals("Rule 1", descriptions.get(0)); // Ensure the first rule was removed
    }

    @Test
    void composeCurrentStoreRules_AndCondition() {
        // Arrange
        purchasePolicy.addRule(List.of(rule1), new ArrayList<>());
        purchasePolicy.addRule(List.of(rule2), new ArrayList<>());
        when(rule1.getDescription()).thenReturn("Rule 1");
        when(rule2.getDescription()).thenReturn("Rule 2");

        // Act
        purchasePolicy.composeCurrentStoreRules(0, 1, "AND");
        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        // Assert
        assertEquals(1, descriptions.size());
        assertTrue(descriptions.get(0).contains("(Rule 1 and Rule 2)"));
    }

    @Test
    void composeCurrentStoreRules_OrCondition() {
        // Arrange
        purchasePolicy.addRule(List.of(rule1), new ArrayList<>());
        purchasePolicy.addRule(List.of(rule2), new ArrayList<>());
        when(rule1.getDescription()).thenReturn("Rule 1");
        when(rule2.getDescription()).thenReturn("Rule 2");

        // Act
        purchasePolicy.composeCurrentStoreRules(0, 1, "OR");
        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        // Assert
        assertEquals(1, descriptions.size());
        assertTrue(descriptions.get(0).contains("(Rule 1 or Rule 2)"));
    }

    @Test
    void composeCurrentStoreRules_CondCondition() {
        // Arrange
        purchasePolicy.addRule(List.of(rule1), new ArrayList<>());
        purchasePolicy.addRule(List.of(rule2), new ArrayList<>());

        when(rule1.getDescription()).thenReturn("Rule 1");
        when(rule2.getDescription()).thenReturn("Rule 2");

        // Act
        purchasePolicy.composeCurrentStoreRules(0, 1, "ONLY IF");
        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        // Assert
        assertEquals(1, descriptions.size());
        assertTrue(descriptions.get(0).contains("(Rule 1 only if Rule 2)"));
    }

    @Test
    void composeCurrentStoreRules_InvalidIndex() {
        // Arrange

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> purchasePolicy.composeCurrentStoreRules(0, 1, "INVALID"));
    }
}
