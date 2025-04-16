package DomainLayer.Store.LogicalConditions;

import DomainLayer.Store.Category;
import DomainLayer.Store.PoliciesRulesLogicalConditions.AgeRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.TestRule;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AgeRuleTest {
    private UserDTO user;
    private List<ProductDTO> products;

    @BeforeEach
    public void setUp() {
        user = new UserDTO();
        products = new ArrayList<>();

        // Set up a test user
        user.setBirthday("01/01/2000");

        // Set up a product
        ProductDTO product = new ProductDTO("Beer", 10, 1, "Alcohol", "ALCOHOL");
        products.add(product);
    }

    @Test
    public void testAgeAbove() {
        // Set clock to current date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-07-01T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        AgeRule ageRule = new AgeRule(21, "Above", Category.ALCOHOL, null, "Age must be above 21", true);
        assertTrue(ageRule.test(user, products));
    }

    @Test
    public void testAgeBelow() {
        // Set clock to current date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-07-01T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        AgeRule ageRule = new AgeRule(30, "Below", Category.ALCOHOL, null, "Age must be below 30", true);
        assertTrue(ageRule.test(user, products));
    }

    @Test
    public void testAgeExactly() {
        // Set clock to current date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-07-01T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        AgeRule ageRule = new AgeRule(24, "Exact", Category.ALCOHOL, null, "Age must be exactly 24", true);
        assertTrue(ageRule.test(user, products));
    }

    @Test
    public void testAgeAboveFail() {
        // Set clock to current date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-07-01T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        AgeRule ageRule = new AgeRule(25, "Above", Category.ALCOHOL, null, "Above age 25 alcohol must be in basket", true);
        assertTrue(ageRule.test(user, products));
    }

    @Test
    public void testAgeBelowFail() {
        // Set clock to current date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-07-01T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        user.setBirthday("01/01/2010");

        AgeRule ageRule = new AgeRule(18, "Below", Category.ALCOHOL, null, "Below age 18 alcohol must not be in the basket ", false);
        assertFalse(ageRule.test(user, products));
    }

    @Test
    public void testAgeExactlyFail() {
        // Set clock to current date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-07-01T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        AgeRule ageRule = new AgeRule(24, "Exact", Category.ALCOHOL, null, "At age 24 alcohol must not be in the basket", false);
        assertFalse(ageRule.test(user, products));
    }

    @Test
    public void testNullUser() {
        AgeRule ageRule = new AgeRule(21, "Above", Category.ALCOHOL, null, "Age must be above 21", true);
        assertThrows(IllegalArgumentException.class, () -> ageRule.test(null, products));
    }
}
