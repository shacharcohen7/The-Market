package DomainLayer.Store.LogicalConditions;

import DomainLayer.Store.Category;
import DomainLayer.Store.PoliciesRulesLogicalConditions.DateRule;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateRuleTest {
    private List<ProductDTO> products;

    @BeforeEach
    public void setUp() {
        products = new ArrayList<>();

        // Set up products
        products.add(new ProductDTO("Beer", 10, 5, "Alcohol", "ALCOHOL"));
        products.add(new ProductDTO("Wine", 20, 3, "Alcohol", "ALCOHOL"));
    }

    @Test
    public void testDateAboveContainsTrue() {
        // Set clock to a specific date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-07-01T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        LocalDate date = LocalDate.of(2024, 6, 30);
        DateRule rule = new DateRule(date, "Above", Category.ALCOHOL, null, "Date must be after 2024-06-30", true);
        assertTrue(rule.test(null, products));
    }

    @Test
    public void testDateBelowContainsTrue() {
        // Set clock to a specific date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-29T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        LocalDate date = LocalDate.of(2024, 6, 30);
        DateRule rule = new DateRule(date, "Below", Category.ALCOHOL, null, "Date must be before 2024-06-30", true);
        assertTrue(rule.test(null, products));
    }

    @Test
    public void testDateExactContainsTrue() {
        // Set clock to a specific date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-30T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        LocalDate date = LocalDate.of(2024, 6, 30);
        DateRule rule = new DateRule(date, "Exact", Category.ALCOHOL, null, "Date must be exactly 2024-06-30", true);
        assertTrue(rule.test(null, products));
    }

    @Test
    public void testDateAboveContainsFalse() {
        // Set clock to a specific date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-29T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        LocalDate date = LocalDate.of(2024, 6, 28);
        DateRule rule = new DateRule(date, "Above", Category.ALCOHOL, null, "After 28.06.2024 basket must not contain alcohol", false);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testDateBelowContainsFalse() {
        // Set clock to a specific date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-07-01T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        LocalDate date = LocalDate.of(2024, 7, 30);
        DateRule rule = new DateRule(date, "Below", Category.ALCOHOL, null, "Before 30.07.2024 basket must not contain alcohol", false);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testDateExactContainsFalse() {
        // Set clock to a specific date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-29T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        LocalDate date = LocalDate.of(2024, 6, 29);
        DateRule rule = new DateRule(date, "Exact", Category.ALCOHOL, null, "At 30.06.2024 basket must not contain alcohol", false);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testDateAboveContainsFalseCondition() {
        // Set clock to a specific date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-07-01T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        LocalDate date = LocalDate.of(2024, 6, 30);
        DateRule rule = new DateRule(date, "Above", Category.ALCOHOL, null, "Date must not be after 2024-06-30", false);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testDateBelowContainsFalseCondition() {
        // Set clock to a specific date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-29T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        LocalDate date = LocalDate.of(2024, 6, 30);
        DateRule rule = new DateRule(date, "Below", Category.ALCOHOL, null, "Date must not be before 2024-06-30", false);
        assertFalse(rule.test(null, products));
    }

    @Test
    public void testDateExactContainsFalseCondition() {
        // Set clock to a specific date
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-30T00:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        LocalDate date = LocalDate.of(2024, 6, 30);
        DateRule rule = new DateRule(date, "Exact", Category.ALCOHOL, null, "Date must not be exactly 2024-06-30", false);
        assertFalse(rule.test(null, products));
    }
}


