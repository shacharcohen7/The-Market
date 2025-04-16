package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("CondRule")
public class CondRule extends CompositeRule {

    public CondRule(Rule rule1, Rule rule2) {
        super(rule1, rule2);
        setDescription(" (" + rule1.getDescription() + " only if " + rule2.getDescription() + ") ");
    }

    public CondRule() {
        super(null, null);
    }

    @Override
    public boolean checkRule(UserDTO user, List<ProductDTO> products) {
        if (getRule1().checkRule(user, products)) {
            return getRule2().checkRule(user, products);
        }
        return true;
    }

}
