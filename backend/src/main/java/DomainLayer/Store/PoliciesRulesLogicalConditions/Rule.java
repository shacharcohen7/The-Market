package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "rule")
public abstract class Rule {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "rule_id")
    private Long id;

    @Column(name = "description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule1_id")
    protected Rule rule1;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rule2_id")
    protected Rule rule2;

    public abstract boolean checkRule(UserDTO user, List<ProductDTO> products);

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

