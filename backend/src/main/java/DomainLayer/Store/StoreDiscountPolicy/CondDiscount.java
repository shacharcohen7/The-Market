package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.*;
import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "cond_discounts")
public class CondDiscount extends Discount{

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_rule_id")
    private Rule discountRule;

    @Transient
    private final Object discountRuleLock;

    public CondDiscount(List<DiscountValue> discountValue, List<String> discountValueOperators, List<Rule> discountRule, List<String> discountRuleOperators) {
        super(discountValue, discountValueOperators);
        discountRuleLock = new Object();
        this.setDiscountRule(discountRule, discountRuleOperators);
    }

    public CondDiscount() {
        discountRuleLock = new Object();
    }

    public void setDiscountRule(List<Rule> rules, List<String> operators)
    {
        Rule rule = rules.get(0);
        if (rules.size() > 1) {
            for (int i = 0; i < operators.size(); i++) {
                switch (operators.get(i)) {
                    case "AND" -> rule = new AndRule(rule, rules.get(i + 1));
                    case "OR" -> rule = new OrRule(rule, rules.get(i + 1));
                    case "XOR" -> rule = new XorRule(rule, rules.get(i + 1));
                }
            }
        }
        synchronized (discountRuleLock) {
            this.discountRule = rule;
        }
    }

    public Rule getDiscountRule() {
        synchronized (discountRuleLock) {
            return this.discountRule;
        }
    }

    public String getDiscountRulesDescriptions() {
        synchronized (discountRuleLock) {
            return this.discountRule.getDescription();
        }
    }

    @Override
    public int calcDiscount(List<ProductDTO> basketProducts , UserDTO userDTO) {
        synchronized (discountRuleLock) {
            if (discountRule.checkRule(userDTO, basketProducts))
                return super.calcDiscount(basketProducts, userDTO);
            return 0;
        }
    }

    @Override
    public String getDescription() {
        synchronized (discountRuleLock) {
            return " (" + super.getDescription() + " only if " + discountRule.getDescription() + ") ";
        }
    }
}
