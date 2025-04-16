package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("XorRule")
public class XorRule extends CompositeRule  {

    public XorRule(Rule rule1, Rule rule2) {
        super(rule1, rule2);
        setDescription(" (" + rule1.getDescription() + " xor " + rule2.getDescription() + ") ");
    }

    public XorRule() {
        super(null, null);
    }

    @Override
    public boolean checkRule(UserDTO user, List<ProductDTO> products) {
        return getRule1().checkRule(user, products) ^ getRule2().checkRule(user, products);
    }

}
