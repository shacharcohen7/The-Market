package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ExceptionsEnum;
import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Entity
@DiscriminatorValue("TimeRule")
public class TimeRule extends TestRule {
    private LocalTime time;

    @Transient
    protected final Object timeLock;

    public TimeRule(LocalTime time, String range, Category category, String productName, String description, Boolean contains) {
        super(range, category, productName, description, contains);
        this.time = time;
        this.timeLock = new Object();
    }

    public TimeRule() {
        super();
        this.timeLock = new Object();
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        Clock clock = TestRule.getClock();
        LocalTime currentTime = LocalTime.now(clock);
        boolean timeCheck = this.checkRange(getRange(), currentTime, getTime());

        if (getCategory() != null || getProductName() != null) {
            if (timeCheck) {
                if (getContains())
                    return getQuantity(products) > 0;
                else
                    return getQuantity(products) == 0;
            } else
                return true;
        }
        return timeCheck;
    }

    public boolean checkRange(String range, LocalTime currentTime, LocalTime time) {
        switch (range) {
            case "Above":
                return currentTime.isAfter(time);
            case "Below":
                return currentTime.isBefore(time);
            case "Exact":
                return currentTime.equals(time);
            default:
                throw new IllegalArgumentException(ExceptionsEnum.InvalidRangeType.toString());
        }
    }

    private LocalTime getTime()
    {
        synchronized (timeLock) {
            return time;
        }
    }
}
