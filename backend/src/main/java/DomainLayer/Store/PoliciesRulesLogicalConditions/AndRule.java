package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("AndRule")
public class AndRule extends CompositeRule {

    public AndRule(Rule rule1, Rule rule2) {
        super(rule1, rule2);
        setDescription(" (" + rule1.getDescription() + " and " + rule2.getDescription() + ") ");
    }

    public AndRule() {
        super(null, null);
    }

    @Override
    public boolean checkRule(UserDTO user, List<ProductDTO> products) {
        return getRule1().checkRule(user, products) && getRule2().checkRule(user, products);
    }

}
