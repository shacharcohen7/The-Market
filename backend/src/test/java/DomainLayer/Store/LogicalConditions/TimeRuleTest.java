package DomainLayer.Store.LogicalConditions;

import DomainLayer.Store.Category;
import DomainLayer.Store.PoliciesRulesLogicalConditions.TestRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.TimeRule;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TimeRuleTest {
    private List<ProductDTO> products;
    private UserDTO user;

    @BeforeEach
    public void setUp() {
        products = new ArrayList<>();
        products.add(new ProductDTO("Beer", 10, 5, "Alcohol", "ALCOHOL"));
        products.add(new ProductDTO("Wine", 20, 3, "Alcohol", "ALCOHOL"));
        products.add(new ProductDTO("Juice", 5, 10, "NonAlcohol", "NON_ALCOHOL"));

        user = null;
    }

    @Test
    public void testTimeRuleAbove() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-01T23:30:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        TimeRule rule = new TimeRule(LocalTime.of(23, 0), "Above", Category.ALCOHOL, null, "No alcohol after 23:00", false);
        assertFalse(rule.test(user, products));

        rule = new TimeRule(LocalTime.of(23, 0), "Above", null, "Beer", "No beer after 23:00", false);
        assertFalse(rule.test(user, products));
    }

    @Test
    public void testTimeRuleBelow() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-01T22:30:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        TimeRule rule = new TimeRule(LocalTime.of(23, 0), "Below", Category.ALCOHOL, null, "No alcohol before 23:00", true);
        assertTrue(rule.test(user, products));

        rule = new TimeRule(LocalTime.of(23, 0), "Below", null, "Beer", "No beer before 23:00", true);
        assertTrue(rule.test(user, products));
    }

    @Test
    public void testTimeRuleExact() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-01T23:00:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        TimeRule rule = new TimeRule(LocalTime.of(23, 0), "Exact", Category.ALCOHOL, null, "No alcohol exactly at 23:00", false);
        assertFalse(rule.test(user, products));

        rule = new TimeRule(LocalTime.of(23, 0), "Exact", null, "Beer", "No beer exactly at 23:00", false);
        assertFalse(rule.test(user, products));
    }

    @Test
    public void testTimeRuleCategoryContains() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-01T23:30:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        TimeRule rule = new TimeRule(LocalTime.of(23, 0), "Above", Category.ALCOHOL, null, "Alcohol should be available after 23:00", true);
        assertTrue(rule.test(user, products));
    }

    @Test
    public void testTimeRuleProductContains() {
        Clock fixedClock = Clock.fixed(Instant.parse("2024-06-01T23:30:00Z"), ZoneId.of("UTC"));
        TestRule.setClock(fixedClock);

        TimeRule rule = new TimeRule(LocalTime.of(23, 0), "Above", null, "Beer", "Beer should be available after 23:00", true);
        assertTrue(rule.test(user, products));
    }
}

