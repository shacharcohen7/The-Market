package DomainLayer.Store.StorePurchasePolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.*;
import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static Util.ExceptionsEnum.*;

@Entity
@Table(name = "purchase_policies")
public class PurchasePolicy {

    @Id
    @Column(name = "purchase_policy_id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Transient
    private final Object purchaseRulesLock;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "purchase_policy_id")
    private List<Rule> purchaseRules;

    public PurchasePolicy()
    {
        purchaseRulesLock = new Object();
        this.purchaseRules = new ArrayList<>();
    }

    public boolean checkPurchasePolicy(UserDTO userDTO, List<ProductDTO> products)
    {
        for (Rule rule : getPurchaseRules()) {
            if (!rule.checkRule(userDTO, products))
                return false;
        }
        return true;
    }

    public void addRule(List<Rule> rules, List<String> operators)
    {
        Rule rule = rules.get(0);
        if (rules.size() > 1) {
            for (int i = 0; i < operators.size(); i++) {
                switch (operators.get(i)) {
                    case "AND" -> rule = new AndRule(rule, rules.get(i + 1));
                    case "OR" -> rule = new OrRule(rule, rules.get(i + 1));
                    case "ONLY IF" -> rule = new CondRule(rule, rules.get(i + 1));
                }
            }
        }
        getPurchaseRules().add(rule);
    }

    public List<String> getRulesDescriptions() {
        List<String> rulesDescriptions = new ArrayList<>();
        for (Rule rule : getPurchaseRules()) {
            rulesDescriptions.add(rule.getDescription());
        }
        return rulesDescriptions;
    }

    public void composeCurrentStoreRules(int ruleNum1, int ruleNum2, String Operator)
    {
        if (ruleNum1 < getPurchaseRules().size() && ruleNum2 < getPurchaseRules().size()) {
            Rule rule1 = getPurchaseRules().get(ruleNum1);
            Rule rule2 = getPurchaseRules().get(ruleNum2);

            // Create a list of indices and sort in reverse order
            List<Integer> indices = Arrays.asList(ruleNum1, ruleNum2);
            indices.sort(Collections.reverseOrder());

            // Remove rules in reverse order of indices
            for (int index : indices) {
                removeRule(index);
            }

            addRule(List.of(rule1, rule2), List.of(Operator));
        } else throw new IllegalArgumentException(InvalidRuleIndex.toString());
    }

    public void removeRule(int ruleNum) {
        if (ruleNum < getPurchaseRules().size())
            getPurchaseRules().remove(ruleNum);
        else throw new IllegalArgumentException(InvalidRuleIndex.toString());
    }

    private List<Rule> getPurchaseRules(){
        synchronized (purchaseRulesLock) {
            return purchaseRules;
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
