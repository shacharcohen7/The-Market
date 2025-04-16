package DomainLayer.Store.StoreDiscountPolicy;

import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "discount_values")
public abstract class DiscountValue {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "discount_value_id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_value1_id")
    protected DiscountValue discountValue1;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "discount_value2_id")
    protected DiscountValue discountValue2;

    @Column(name = "description")
    protected String description;

    public DiscountValue(String description) {
        this.description = description;
    }

    public DiscountValue() {

    }

    public abstract int calcDiscount(List<ProductDTO> basketProducts);

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
