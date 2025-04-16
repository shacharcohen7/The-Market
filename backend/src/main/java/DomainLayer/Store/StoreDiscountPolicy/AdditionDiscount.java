package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.CompositeNumericalDiscount;
import Util.ProductDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("AdditionDiscount")
public class AdditionDiscount extends CompositeNumericalDiscount {
    public AdditionDiscount(DiscountValue discountValue1, DiscountValue discountValue2) {
        super(discountValue1, discountValue2, " (" + discountValue1.getDescription() + " addition " + discountValue2.getDescription() + ") ");
    }

    public AdditionDiscount() {
        super(null, null, "AdditionDiscount");
    }

    @Override
    public int calcDiscount(List<ProductDTO> basketProducts) {
        int discount1 = getDiscountValue1().calcDiscount(basketProducts);
        int discount2 = getDiscountValue2().calcDiscount(basketProducts);

        return discount1 + discount2;
    }

}
