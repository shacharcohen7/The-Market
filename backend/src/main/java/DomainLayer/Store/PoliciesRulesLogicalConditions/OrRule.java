package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("OrRule")
public class OrRule extends CompositeRule{
    public OrRule(Rule rule1, Rule rule2) {
        super(rule1, rule2);
        setDescription(" (" + rule1.getDescription() + " or " + rule2.getDescription() + ") ");
    }

    public OrRule() {
        super(null, null);
    }

    @Override
    public boolean checkRule(UserDTO user, List<ProductDTO> products) {
        return getRule1().checkRule(user, products) || getRule2().checkRule(user, products);
    }

}
