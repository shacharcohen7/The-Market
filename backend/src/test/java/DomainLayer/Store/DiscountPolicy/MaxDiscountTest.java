package DomainLayer.Store.DiscountPolicy;

import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.MaxDiscount;
import Util.ProductDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class MaxDiscountTest {

    @Mock
    private DiscountValue discountValue1;

    @Mock
    private DiscountValue discountValue2;

    @Test
    public void testCalcDiscount_BothPositive() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        MaxDiscount maxDiscount = new MaxDiscount(discountValue1, discountValue2);
        List<ProductDTO> basketProducts = Arrays.asList(new ProductDTO("Product 1", 100, 1, "Description", "Category"));

        // Mock behavior of DiscountValue mocks
        when(discountValue1.calcDiscount(basketProducts)).thenReturn(50);
        when(discountValue2.calcDiscount(basketProducts)).thenReturn(30);

        // Act
        int result = maxDiscount.calcDiscount(basketProducts);

        // Assert
        assertEquals(50, result); // max(50, 30) should be 50
    }

    @Test
    public void testCalcDiscount_FirstNegative() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        MaxDiscount maxDiscount = new MaxDiscount(discountValue1, discountValue2);
        List<ProductDTO> basketProducts = Arrays.asList(new ProductDTO("Product 1", 100, 1, "Description", "Category"));

        // Mock behavior of DiscountValue mocks
        when(discountValue1.calcDiscount(basketProducts)).thenReturn(-50); // First discount is negative
        when(discountValue2.calcDiscount(basketProducts)).thenReturn(30);

        // Act
        int result = maxDiscount.calcDiscount(basketProducts);

        // Assert
        assertEquals(30, result); // max(-50, 30) should be 30
    }

    @Test
    public void testCalcDiscount_SecondNegative() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        MaxDiscount maxDiscount = new MaxDiscount(discountValue1, discountValue2);
        List<ProductDTO> basketProducts = Arrays.asList(new ProductDTO("Product 1", 100, 1, "Description", "Category"));

        // Mock behavior of DiscountValue mocks
        when(discountValue1.calcDiscount(basketProducts)).thenReturn(50);
        when(discountValue2.calcDiscount(basketProducts)).thenReturn(-30); // Second discount is negative

        // Act
        int result = maxDiscount.calcDiscount(basketProducts);

        // Assert
        assertEquals(50, result); // max(50, -30) should be 50
    }

    @Test
    public void testCalcDiscount_BothNegative() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        MaxDiscount maxDiscount = new MaxDiscount(discountValue1, discountValue2);
        List<ProductDTO> basketProducts = Arrays.asList(new ProductDTO("Product 1", 100, 1, "Description", "Category"));

        // Mock behavior of DiscountValue mocks
        when(discountValue1.calcDiscount(basketProducts)).thenReturn(-50); // Both discounts are negative
        when(discountValue2.calcDiscount(basketProducts)).thenReturn(-30);

        // Act
        int result = maxDiscount.calcDiscount(basketProducts);

        // Assert
        assertEquals(-30, result); // max(-50, -30) should be -30
    }
}