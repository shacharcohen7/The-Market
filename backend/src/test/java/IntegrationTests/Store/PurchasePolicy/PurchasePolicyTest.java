package IntegrationTests.Store.PurchasePolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.*;
import DomainLayer.Store.StorePurchasePolicy.PurchasePolicy;
import Util.ProductDTO;
import Util.TestRuleDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PurchasePolicyTest {
    private PurchasePolicy purchasePolicy;
    private TestRuleDTO testRule1;
    private TestRuleDTO testRule2;
    private int product1Price;
    @BeforeEach
    public void setUp() {
        purchasePolicy = new PurchasePolicy();
        product1Price = 10;

        //initialize testRule1 and testRule2
        testRule1 = new TestRuleDTO("Amount", "Below", null, "tomato", "basket contains less than 5 tomatoes", true, null, 5, null, null, null);
        testRule2 = new TestRuleDTO("Amount", "Above", null, "corn", "basket contains at least 2 corns", true, null, 2, null, null, null);
    }

    @Test
    public void testAddAndCheckRule() {
        //initialize testRule1 "basket contains less than 5 tomatoes"
        // Creating a simple rule that always returns true
        Rule trueRule = new SimpleRule(testRule1); //basket contains less than 5 tomatoes
        List<Rule> rules = Collections.singletonList(trueRule);
        purchasePolicy.addRule(rules, Collections.emptyList());

        UserDTO user = new UserDTO("User1", "user1@gmail.com", "12/3/45", "Israel", "Ashqelon", "rabin", "moshe");
        ProductDTO product = new ProductDTO("Product1", product1Price, 5, "A product", "TOYS");
        List<ProductDTO> products = Collections.singletonList(product);

        assertTrue(purchasePolicy.checkPurchasePolicy(user, products));
    }

    @Test
    public void testAddMultipleRulesWithAndOperator() {
        // Creating simple rules
        Rule trueRule = new SimpleRule(testRule1); //basket contains less than 5 tomatoes
        Rule falseRule = new SimpleRule(testRule2); //basket contains at least 2 corns

        List<Rule> rules = Arrays.asList(trueRule, falseRule);
        List<String> operators = Collections.singletonList("AND");
        purchasePolicy.addRule(rules, operators);

        UserDTO user = new UserDTO("User1", "user1@gmail.com", "12/3/45", "Israel", "Ashqelon", "rabin", "moshe");
        ProductDTO product = new ProductDTO("Product1", product1Price, 5, "A product", "TOYS");
        List<ProductDTO> products = Collections.singletonList(product);

        assertFalse(purchasePolicy.checkPurchasePolicy(user, products));
    }

    @Test
    public void testAddMultipleRulesWithOrOperator() {
        // Creating simple rules
        Rule trueRule = new SimpleRule(testRule1); //basket contains less than 5 tomatoes
        Rule falseRule = new SimpleRule(testRule2); //basket contains at least 2 corns


        List<Rule> rules = Arrays.asList(falseRule, trueRule);
        List<String> operators = Collections.singletonList("OR");
        purchasePolicy.addRule(rules, operators);

        UserDTO user = new UserDTO("User1", "user1@gmail.com", "12/3/45", "Israel", "Ashqelon", "rabin", "moshe");
        ProductDTO product = new ProductDTO("Product1", product1Price, 5, "A product", "TOYS");
        List<ProductDTO> products = Collections.singletonList(product);

        assertTrue(purchasePolicy.checkPurchasePolicy(user, products));
    }

    @Test
    public void testAddCondRule() {
        // Creating simple rules
        Rule trueRule = new SimpleRule(testRule1); //basket contains less than 5 tomatoes
        Rule falseRule = new SimpleRule(testRule2); //basket contains at least 2 corns

        List<Rule> rules = Arrays.asList(trueRule, falseRule);
        List<String> operators = Collections.singletonList("ONLY IF");
        purchasePolicy.addRule(rules, operators);

        UserDTO user = new UserDTO("User1", "user1@gmail.com", "12/3/45", "Israel", "Ashqelon", "rabin", "moshe");
        ProductDTO product = new ProductDTO("Product1", product1Price, 5, "A product", "TOYS");
        List<ProductDTO> products = Collections.singletonList(product);

        assertFalse(purchasePolicy.checkPurchasePolicy(user, products));
    }

    @Test
    public void testGetRulesDescriptions() {
        // Creating a simple rule
        Rule trueRule = new SimpleRule(testRule1); //basket contains less than 5 tomatoes

        List<Rule> rules = Collections.singletonList(trueRule);
        purchasePolicy.addRule(rules, Collections.emptyList());

        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        assertEquals(1, descriptions.size());
        assertEquals(trueRule.getDescription(), descriptions.get(0));
    }

    @Test
    public void testRemoveRule() {
        // Creating a simple rule
        Rule trueRule = new SimpleRule(testRule1); //basket contains less than 5 tomatoes
        List<Rule> rules = Collections.singletonList(trueRule);
        purchasePolicy.addRule(rules, Collections.emptyList());

        purchasePolicy.removeRule(0);
        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        assertEquals(0, descriptions.size());
    }

    @Test
    public void testComposeCurrentStoreRules() {
        // Creating simple rules
        Rule trueRule = new SimpleRule(testRule1); //basket contains less than 5 tomatoes
        Rule falseRule = new SimpleRule(testRule2); //basket contains at least 2 corns
        purchasePolicy.addRule(Collections.singletonList(trueRule), Collections.emptyList());
        purchasePolicy.addRule(Collections.singletonList(falseRule), Collections.emptyList());

        //compose the two first rules
        purchasePolicy.composeCurrentStoreRules(0, 1, "OR");
        List<String> descriptions = purchasePolicy.getRulesDescriptions();

        UserDTO user = new UserDTO("User1", "user1@gmail.com", "12/3/45", "Israel", "Ashqelon", "rabin", "moshe");
        ProductDTO product = new ProductDTO("Product1", product1Price, 5, "A product", "TOYS");
        List<ProductDTO> products = Collections.singletonList(product);

        assertEquals(1, descriptions.size());
        assertTrue(purchasePolicy.checkPurchasePolicy(user, products));
    }
}
