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
import java.util.*;

@Entity
@DiscriminatorValue("AgeRule")
public class AgeRule extends TestRule {

    private int age;

    @Transient
    protected final Object ageLock;

    public AgeRule(int age, String range, Category category, String productName, String description, Boolean contains){
        super(range, category, productName, description, contains);
        this.age = age;
        this.ageLock = new Object();
    }

    public AgeRule() {
        super();
        this.ageLock = new Object();
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        if (user == null) {
            throw new IllegalArgumentException(ExceptionsEnum.UserCannotBeNull.toString());
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthdate = LocalDate.parse(user.getBirthday(), formatter);
        LocalDate today = LocalDate.now(getClock());
        int userAge = Period.between(birthdate, today).getYears();

        //check if the user is above or below the age, age can be "Above" or "Below" or "Exact"
        boolean ageCheck = checkRange(getRange(), userAge, getAge());

        if (getCategory() != null || getProductName() != null) {
            if (ageCheck) {
                if (getContains())
                    return getQuantity(products) > 0;
                else
                    return getQuantity(products) == 0;
            } else
                return true;
        }
        return ageCheck;
    }

    private int getAge()
    {
        synchronized (ageLock) {
            return age;
        }
    }
}
