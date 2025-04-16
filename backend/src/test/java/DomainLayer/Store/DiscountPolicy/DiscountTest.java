package DomainLayer.Store.DiscountPolicy;

import DomainLayer.Store.StoreDiscountPolicy.AdditionDiscount;
import DomainLayer.Store.StoreDiscountPolicy.Discount;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.MaxDiscount;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DiscountTest {

    @Mock
    private DiscountValue discountValue1;

    @Mock
    private DiscountValue discountValue2;

    private Discount discount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSetDiscountValue_Max() {
        // Arrange
        List<DiscountValue> discountValues = Arrays.asList(discountValue1, discountValue2);
        List<String> operators = Arrays.asList("MAX");

        // Act
        discount = new Discount(discountValues, operators);

        // Verify the setDiscountValue method is called correctly
        assertEquals(MaxDiscount.class, discount.getDiscountValue().getClass());
    }

    @Test
    public void testSetDiscountValue_Addition() {
        // Arrange
        List<DiscountValue> discountValues = Arrays.asList(discountValue1, discountValue2);
        List<String> operators = Arrays.asList("ADDITION");

        // Act
        discount = new Discount(discountValues, operators);

        // Verify the setDiscountValue method is called correctly
        assertEquals(AdditionDiscount.class, discount.getDiscountValue().getClass());
    }

    @Test
    public void testCalcDiscount() {
        // Arrange
        ProductDTO product1 = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        ProductDTO product2 = new ProductDTO("Product 2", 200, 1, "Description 2", "Category 2");
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);
        UserDTO userDTO = new UserDTO();

        when(discountValue1.calcDiscount(basketProducts)).thenReturn(50);
        List<DiscountValue> discountValues = Arrays.asList(discountValue1);
        List<String> operators = Arrays.asList();
        discount = new Discount(discountValues, operators);

        // Act
        int totalDiscount = discount.calcDiscount(basketProducts, userDTO);

        // Assert
        assertEquals(50, totalDiscount);
        verify(discountValue1).calcDiscount(basketProducts);
    }

    @Test
    public void testCalcDiscount_WithMultipleValues() {
        // Arrange
        ProductDTO product1 = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        ProductDTO product2 = new ProductDTO("Product 2", 200, 1, "Description 2", "Category 2");
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);
        UserDTO userDTO = new UserDTO();

        when(discountValue1.calcDiscount(basketProducts)).thenReturn(50);
        when(discountValue2.calcDiscount(basketProducts)).thenReturn(30);
        List<DiscountValue> discountValues = Arrays.asList(discountValue1, discountValue2);
        List<String> operators = Arrays.asList("MAX");
        discount = new Discount(discountValues, operators);

        // Act
        int totalDiscount = discount.calcDiscount(basketProducts, userDTO);

        // Assert
        assertEquals(50, totalDiscount);  // Assuming MaxDiscount is selected and it picks the max value
        verify(discountValue1).calcDiscount(basketProducts);
        verify(discountValue2).calcDiscount(basketProducts);
    }
}
