package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import Util.DiscountValueDTO;
import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static Util.ExceptionsEnum.InvalidRuleIndex;

@Entity
@Table(name = "discount_policies")
public class DiscountPolicy {

    @Id
    @Column(name = "discount_policy_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_policy_id")
    private List<Discount> discountRules;
    
    @Transient
    private final Object discountRulesLock;

    public DiscountPolicy()
    {
        this.discountRules = new ArrayList<>();
        discountRulesLock = new Object();
    }

    public int calcDiscountPolicy(UserDTO userDTO, List<ProductDTO> products)
    {
        int totalDiscount = 0;
        for (Discount discountRule : getDiscountRules()) {
            totalDiscount += discountRule.calcDiscount(products, userDTO);
        }
        return totalDiscount;
    }

    public void addCondRule(List<Rule> rules, List<String> logicalOperators, List<DiscountValue> discDetails, List<String> numericalOperators) {
        getDiscountRules().add(new CondDiscount(discDetails, numericalOperators, rules, logicalOperators));
    }

    public void composeCurrentCondDiscountRules(int ruleNum1, int ruleNum2, List<String> logicalOperators, List<String> numericalOperators) {
        if (ruleNum1 < getDiscountRules().size() && ruleNum2 < getDiscountRules().size()) {
            List<Rule> rules = new ArrayList<>();
            List<DiscountValue> discDetails = new ArrayList<>();
            if (getDiscountRules().get(ruleNum1) instanceof CondDiscount discountRule1 && getDiscountRules().get(ruleNum2) instanceof CondDiscount discountRule2) {
                rules.add(discountRule1.getDiscountRule());
                rules.add(discountRule2.getDiscountRule());
                discDetails.add(discountRule1.getDiscountValue());
                discDetails.add(discountRule2.getDiscountValue());

                // Create a list of indices and sort in reverse order
                List<Integer> indices = Arrays.asList(ruleNum1, ruleNum2);
                indices.sort(Collections.reverseOrder());

                // Remove rules in reverse order of indices
                for (int index : indices) {
                    removeRule(index);
                }

                addCondRule(rules, logicalOperators, discDetails, numericalOperators);
            } else throw new IllegalArgumentException(InvalidRuleIndex.toString());
        } else throw new IllegalArgumentException(InvalidRuleIndex.toString());
    }

    public void composeCurrentSimpleDiscountRules(int ruleNum1, int ruleNum2, List<String> discountValueOperators) {
        if (ruleNum1 < getDiscountRules().size() && ruleNum2 < getDiscountRules().size()) {
            List<DiscountValue> discDetails = new ArrayList<>();
            if (!(getDiscountRules().get(ruleNum1) instanceof CondDiscount) && !(getDiscountRules().get(ruleNum2) instanceof CondDiscount)) {
                discDetails.add(getDiscountRules().get(ruleNum1).getDiscountValue());
                discDetails.add(getDiscountRules().get(ruleNum2).getDiscountValue());

                // Create a list of indices and sort in reverse order
                List<Integer> indices = Arrays.asList(ruleNum1, ruleNum2);
                indices.sort(Collections.reverseOrder());

                // Remove rules in reverse order of indices
                for (int index : indices) {
                    removeRule(index);
                }

                addSimple(discDetails, discountValueOperators);
            } else throw new IllegalArgumentException(InvalidRuleIndex.toString());
        } else throw new IllegalArgumentException(InvalidRuleIndex.toString());
    }

    public void addSimple(List<DiscountValue> discDetails, List<String> discountValueOperators) {
        getDiscountRules().add(new Discount(discDetails, discountValueOperators));
    }

    public void addRule(Discount discount) {
        getDiscountRules().add(discount);
    }

    public void removeRule(int ruleNum) {
        if (ruleNum < getDiscountRules().size())
            getDiscountRules().remove(ruleNum);
        else throw new IllegalArgumentException(InvalidRuleIndex.toString());
    }

    public List<Discount> getDiscountRules() {
        synchronized (discountRulesLock) {
            return discountRules;
        }
    }

    public List<String> getRulesDescriptions() {
        List<String> rulesDescriptions = new ArrayList<>();
        for (Discount discount : getDiscountRules()) {
            rulesDescriptions.add(discount.getDescription());
        }
        return rulesDescriptions;
    }

    public List<String> getCondDiscountRulesDescriptions() {
        List<String> rulesDescriptions = new ArrayList<>();
        for (Discount discount : getDiscountRules()) {
            if (discount instanceof CondDiscount condDiscount) {
                rulesDescriptions.add(condDiscount.getDescription());
            }
        }
        return rulesDescriptions;
    }

    public List<String> getSimpleDiscountRulesDescriptions() {
        List<String> rulesDescriptions = new ArrayList<>();
        for (Discount discount : getDiscountRules()) {
            if (!(discount instanceof CondDiscount)) {
                rulesDescriptions.add(discount.getDescription());
            }
        }
        return rulesDescriptions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
