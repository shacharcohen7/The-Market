package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.AndRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.CondRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.OrRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;


import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Discount {

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_value_id")
    protected DiscountValue discountValue;

    @Transient
    private final Object discountValueLock;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Discount(List<DiscountValue> discountValue, List<String> operators) {
        discountValueLock = new Object();
        setDiscountValue(discountValue, operators);
    }

    public Discount() {
        discountValueLock = new Object();
    }

    public void setDiscountValue(List<DiscountValue> discountValues, List<String> operators) {
        DiscountValue discountValue = discountValues.get(0);
        if (discountValues.size() > 1) {
            for (int i = 0; i < operators.size(); i++) {
                switch (operators.get(i)) {
                    case "MAX" -> discountValue = new MaxDiscount(discountValue, discountValues.get(i + 1));
                    case "ADDITION" ->
                            discountValue = new AdditionDiscount(discountValue, discountValues.get(i + 1));
                }
            }
        }
        synchronized (discountValueLock) {
            this.discountValue = discountValue;
        }
    }

    public int calcDiscount(List<ProductDTO> basketProducts, UserDTO userDTO) {
        synchronized (discountValueLock) {
            return discountValue.calcDiscount(basketProducts);
        }
    }

    public DiscountValue getDiscountValue() {
        synchronized (discountValueLock) {
            return discountValue;
        }
    }

    public String getDescription() {
        synchronized (discountValueLock) {
            return discountValue.getDescription();
        }
    }
}
