package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ExceptionsEnum;
import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@DiscriminatorValue("DateRule")
public class DateRule extends TestRule {
    private LocalDate date;

    @Transient
    protected final Object dateLock;

    public DateRule(LocalDate date, String range, Category category, String productName, String description, Boolean contains) {
        super(range, category, productName, description, contains);
        this.date = date;
        this.dateLock = new Object();
    }

    public DateRule() {
        super();
        this.dateLock = new Object();
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        Clock clock = TestRule.getClock();
        LocalDate currentDate = LocalDate.now(clock);
        boolean dateCheck = this.checkRange(getRange(), currentDate, getDate());
        if(getCategory() !=null || getProductName() != null) {
            if(dateCheck) {
                if (getContains())
                    return getQuantity(products) > 0;
                else
                    return getQuantity(products) == 0;
            }
            else
                return true;
        }
        return dateCheck;
    }

    public boolean checkRange(String range, LocalDate currentDate, LocalDate date) {
        switch (range) {
            case "Above":
                return currentDate.isAfter(date);
            case "Below":
                return currentDate.isBefore(date);
            case "Exact":
                return currentDate.equals(date);
            default:
                throw new IllegalArgumentException(ExceptionsEnum.InvalidRangeType.toString());
        }
    }

    private LocalDate getDate()
    {
        synchronized (dateLock) {
            return date;
        }
    }
}
