package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("AmountRule")
public class AmountRule extends TestRule {
    private int quantity;

    @Transient
    protected final Object quantityLock;

    public AmountRule(int quantity, String range, Category category, String productName, String description, Boolean contains) {
        super(range, category, productName, description, contains);
        this.quantity = quantity;
        this.quantityLock = new Object();
    }

    public AmountRule() {
        super();
        this.quantityLock = new Object();
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        if (getContains())
            return checkRange(getRange(), getQuantity(products), getQuantity());
        else
            return !checkRange(getRange(), getQuantity(products), getQuantity());
    }

    private int getQuantity(){
        synchronized (quantityLock) {
            return quantity;
        }
    }
}
