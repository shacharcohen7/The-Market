package DomainLayer.Store.DiscountPolicy;

import DomainLayer.Store.StoreDiscountPolicy.AdditionDiscount;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import Util.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdditionDiscountTest {

    @Mock
    private DiscountValue discountValue1;

    @Mock
    private DiscountValue discountValue2;

    private AdditionDiscount additionDiscount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        additionDiscount = new AdditionDiscount(discountValue1, discountValue2);
    }

    @Test
    public void testCalcDiscount() {
        // Arrange
        ProductDTO product1 = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        ProductDTO product2 = new ProductDTO("Product 2", 200, 1, "Description 2", "Category 2");
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);

        when(discountValue1.calcDiscount(basketProducts)).thenReturn(50);
        when(discountValue2.calcDiscount(basketProducts)).thenReturn(30);

        // Act
        int totalDiscount = additionDiscount.calcDiscount(basketProducts);

        // Assert
        assertEquals(80, totalDiscount);

        // Verify that the calcDiscount methods were called
        verify(discountValue1).calcDiscount(basketProducts);
        verify(discountValue2).calcDiscount(basketProducts);
    }

    @Test
    public void testCalcDiscount_ZeroDiscount() {
        // Arrange
        ProductDTO product1 = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        ProductDTO product2 = new ProductDTO("Product 2", 200, 1, "Description 2", "Category 2");
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);

        when(discountValue1.calcDiscount(basketProducts)).thenReturn(0);
        when(discountValue2.calcDiscount(basketProducts)).thenReturn(0);

        // Act
        int totalDiscount = additionDiscount.calcDiscount(basketProducts);

        // Assert
        assertEquals(0, totalDiscount);

        // Verify that the calcDiscount methods were called
        verify(discountValue1).calcDiscount(basketProducts);
        verify(discountValue2).calcDiscount(basketProducts);
    }

    @Test
    public void testCalcDiscount_NegativeDiscount() {
        // Arrange
        ProductDTO product1 = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        ProductDTO product2 = new ProductDTO("Product 2", 200, 1, "Description 2", "Category 2");
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);

        when(discountValue1.calcDiscount(basketProducts)).thenReturn(-10);
        when(discountValue2.calcDiscount(basketProducts)).thenReturn(-20);

        // Act
        int totalDiscount = additionDiscount.calcDiscount(basketProducts);

        // Assert
        assertEquals(-30, totalDiscount);

        // Verify that the calcDiscount methods were called
        verify(discountValue1).calcDiscount(basketProducts);
        verify(discountValue2).calcDiscount(basketProducts);
    }
}
