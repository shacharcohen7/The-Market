package DomainLayer.Store.LogicalConditions;

import DomainLayer.Store.PoliciesRulesLogicalConditions.*;
import DomainLayer.Store.StorePurchasePolicy.*;
import Util.ProductDTO;
import Util.TestRuleDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class SimpleRuleTest {

    private UserDTO userDTO;
    private List<ProductDTO> products;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("userId12345", "BestUser", "12/12/12", "Israel", "Bash", "Mesada", "Toy");
        products = new ArrayList<>();
    }

    @Test
    void testAlcoholRestrictionBelowAge18() {
        TestRuleDTO rule1 = new TestRuleDTO("Age", "Below", "ALCOHOL", null, "Alcohol cannot be sold to users below the age of 18", false, 18, null, null, null, null);
        SimpleRule simpleRule = new SimpleRule(rule1);

        userDTO.setBirthday("15/06/2010"); // User is 13 years old
        ProductDTO product = new ProductDTO("Beer", 10, 1, "Alcohol", "ALCOHOL");
        products.add(product);

        assertFalse(simpleRule.checkRule(userDTO, products));

        userDTO.setBirthday("15/06/2000"); // User is 23 years old
        assertTrue(simpleRule.checkRule(userDTO, products));
    }

    @Test
    void testAlcoholRestrictionAfter2300() {
        Clock after2300Clock = Clock.fixed(Instant.parse("2024-06-01T23:30:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(after2300Clock);
        TestRuleDTO testRule = new TestRuleDTO("Time", "Above", "ALCOHOL", null, "Alcohol cannot be sold after 23:00", false, null, null, null, null, LocalTime.of(23, 0));
        SimpleRule rule = new SimpleRule(testRule);

        ProductDTO product = new ProductDTO("Beer", 10, 1, "Alcohol", "ALCOHOL");
        products.add(product);

        assertFalse(rule.checkRule(userDTO, products));

        Clock before2300Clock = Clock.fixed(Instant.parse("2024-06-01T22:30:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(before2300Clock);
        rule = new SimpleRule(testRule);
        assertTrue(rule.checkRule(userDTO, products));
    }

    @Test
    void testBasketContainsLessThan5KgTomatoes() {
        TestRuleDTO rule3 = new TestRuleDTO("Amount", "Below", null, "Tomato", "Basket must contain less than 5kg of tomatoes", true, null, 5, null, null, null);
        SimpleRule simpleRule = new SimpleRule(rule3);

        ProductDTO product = new ProductDTO("Tomato", 5, 4, "Vegetable", "FOOD");
        products.add(product);
        assertTrue(simpleRule.checkRule(userDTO, products));

        product.setQuantity(5);
        assertFalse(simpleRule.checkRule(userDTO, products));
    }

    @Test
    void testBasketContainsAtLeast2Corns() {
        TestRuleDTO testRule = new TestRuleDTO("Amount", "Above", null, "corn", "basket contains at least 2 corns", true, null, 1, null, null, null);
        SimpleRule rule = new SimpleRule(testRule);

        ProductDTO product = new ProductDTO("corn", 7, 1, "Vegetable", "FOOD");
        products.add(product);
        assertFalse(rule.checkRule(userDTO, products));

        product.setQuantity(2);
        assertTrue(rule.checkRule(userDTO, products));
    }

    @Test
    void testBasketContainsEggplants() {
        TestRuleDTO testRule = new TestRuleDTO("Amount", "Above", null, "eggplant", "Basket must contain eggplants", true, null, 0, null, null, null);
        SimpleRule rule = new SimpleRule(testRule);

        assertFalse(rule.checkRule(userDTO, products));

        ProductDTO product = new ProductDTO("eggplant", 10, 1, "Vegetable", "FOOD");
        products.add(product);
        assertTrue(rule.checkRule(userDTO, products));
    }

    @Test
    void testIsHolidayEvening() {
        int year = LocalDate.now().getYear();

        TestRuleDTO testRule = new TestRuleDTO("Date", "Exact", null, null, "The day must be Christmas Holiday", true, null, null, LocalDate.of(year, 12, 25), null, null);

        SimpleRule rule = new SimpleRule(testRule);

        // Test for the evening before Christmas
        Clock christmasEveClock = Clock.fixed(Instant.parse("2024-12-25T10:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(christmasEveClock);
        assertTrue(rule.checkRule(userDTO, products));

        // Test for a regular day
        Clock regularDayClock = Clock.fixed(Instant.parse("2024-12-26T10:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(regularDayClock);
        assertFalse(rule.checkRule(userDTO, products));
    }
}
