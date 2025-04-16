package DomainLayer.PaymentServices;

import Util.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceiptTest {

    private Receipt receipt;
    private final String receiptId = "receipt-123";
    private final String storeId = "store-456";
    private final String userId = "user-789";
    private final String acquisitionID = "acquisition-123";
    private List<ProductDetailReceipt> productList;
    private final int transactionId = 67899;
    private final PaymentDTO paymentDTO = new PaymentDTO();
    private final Map<String, Map<String, List<Integer>>> products = new HashMap<>();
    private final Acquisition acquisition = new Acquisition(transactionId,acquisitionID,userId,null,250, "www.paypal",paymentDTO,products);

    @BeforeEach
    public void setUp() {
        productList = new ArrayList<>();
        productList.add(new ProductDetailReceipt(new ProductDetailReceiptId(receiptId,storeId,"Product1"),2, 100,acquisition)); // Quantity, Price
        productList.add(new ProductDetailReceipt(new ProductDetailReceiptId(receiptId,storeId,"Product2"),1, 50,acquisition));

        receipt = new Receipt(receiptId, storeId, userId, productList);
    }

    @Test
    public void testGetTotalPriceOfStoreReceipt() {
        int totalPrice = receipt.getTotalPriceOfStoreReceipt();
        // Expected total: 2 * 100 + 1 * 50 = 250
        assertEquals(150, totalPrice);
    }

    @Test
    public void testGetStoreId() {
        String retrievedStoreId = receipt.getStoreId();
        assertEquals(storeId, retrievedStoreId);
    }

    @Test
    public void testGetReceiptId() {
        String retrievedReceiptId = receipt.getReceiptId();
        assertEquals(receiptId, retrievedReceiptId);
    }
}
