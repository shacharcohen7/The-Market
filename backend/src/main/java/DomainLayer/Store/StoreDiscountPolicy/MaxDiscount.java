package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.CompositeNumericalDiscount;
import Util.ProductDTO;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.util.List;

@Entity
@DiscriminatorValue("MaxDiscount")
public class MaxDiscount extends CompositeNumericalDiscount {

    public MaxDiscount(DiscountValue discountValue1, DiscountValue discountValue2) {
        super(discountValue1, discountValue2, " (Max between " + discountValue1.getDescription() + " and " + discountValue2.getDescription() + ") ");
    }

    public MaxDiscount() {
        super(null, null, "MaxDiscount");
    }

    @Override
    public int calcDiscount(List<ProductDTO> basketProducts) {
        //check which discount is bigger and return it
        int discount1 = getDiscountValue1().calcDiscount(basketProducts);
        int discount2 = getDiscountValue2().calcDiscount(basketProducts);
        return Math.max(discount1, discount2);
    }
}
