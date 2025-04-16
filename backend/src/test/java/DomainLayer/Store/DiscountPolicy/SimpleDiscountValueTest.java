package DomainLayer.Store.DiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.SimpleDiscountValue;
import Util.ProductDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SimpleDiscountValueTest {

    @Mock
    private ProductDTO product1;

    @Mock
    private ProductDTO product2;

    @Test
    public void testCalcDiscount_CategoryDiscount() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);

        // Mock behavior of ProductDTO mocks
        when(product1.getCategoryStr()).thenReturn("ELECTRONICS");
        when(product2.getCategoryStr()).thenReturn("CLOTHING");
        when(product1.getPrice()).thenReturn(200);
        when(product2.getPrice()).thenReturn(100);
        SimpleDiscountValue discountValue = new SimpleDiscountValue(10, Category.valueOf("ELECTRONICS"), false, null);

        // Act
        int discount = discountValue.calcDiscount(basketProducts);

        // Assert
        assertEquals(20, discount); // 10% of total price of products in "ELECTRONICS"
    }

    @Test
    public void testCalcDiscount_ProductsDiscount() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);

        // Mock behavior of ProductDTO mocks
        when(product1.getName()).thenReturn("Product 1");
        when(product2.getName()).thenReturn("Product 2");
        when(product1.getPrice()).thenReturn(200);
        when(product2.getPrice()).thenReturn(100);

        SimpleDiscountValue discountValue = new SimpleDiscountValue(20, null, false, Arrays.asList("Product 1", "Product 2"));

        // Act
        int discount = discountValue.calcDiscount(basketProducts);

        // Assert
        assertEquals(60, discount); // 20% of total price of "Product 1" and "Product 2"
    }

    @Test
    public void testCalcDiscount_StoreDiscount() {
        // Arrange
        MockitoAnnotations.openMocks(this);
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);

        // Mock behavior of ProductDTO mocks
        when(product1.getPrice()).thenReturn(100);
        when(product2.getPrice()).thenReturn(200);

        SimpleDiscountValue discountValue = new SimpleDiscountValue(15, null, true, null);

        // Act
        int discount = discountValue.calcDiscount(basketProducts);

        // Assert
        assertEquals(45, discount); // 15% of total price of all products (100 + 200 = 300)
    }
}