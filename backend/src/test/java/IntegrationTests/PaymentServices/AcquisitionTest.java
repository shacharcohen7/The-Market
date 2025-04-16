package IntegrationTests.PaymentServices;

import DomainLayer.PaymentServices.Acquisition;
import Util.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AcquisitionTest {

    private Acquisition acquisition;

    @BeforeEach
    public void setUp() {
        // Create PaymentDTO instance with test data
        PaymentDTO paymentDTO = new PaymentDTO("130", "david", "USD","98767576576", 986, 6,2030);

        // Create product list with test data
        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
        productList.put("store1", new HashMap<>());
        productList.get("store1").put("Product1", Arrays.asList(2, 100)); // Quantity, Price
        productList.get("store1").put("Product2", Arrays.asList(1, 50));

        // Create Acquisition instance
        acquisition = new Acquisition(30000, "acq-1", "user-1",null, 150,"www.com",  paymentDTO, productList);
    }

    @Test
    public void testGetUserId() {
        assertEquals("user-1", acquisition.getUserId());
    }

    @Test
    public void testGetTotalPrice() {
        assertEquals(150, acquisition.getTotalPrice());
    }

    @Test
    public void testGetTotalPriceOfStoreInAcquisition() {
        int totalPriceStore1 = acquisition.getReceiptMap().get("store1").getTotalPriceOfStoreReceipt();
        assertEquals(150, totalPriceStore1); // Expected total: 2 * 100 + 1 * 50 = 250
    }

    @Test
    public void testGetReceiptIdByStoreId() {
        String receiptIdStore1 = acquisition.getReceiptIdByStoreId("store1");
        assertTrue(receiptIdStore1.startsWith("receipt"));
    }

    @Test
    public void testGetReceiptIdAndStoreIdMap() {
        Map<String, String> receiptIdMap = acquisition.getReceiptIdAndStoreIdMap();
        assertEquals(1, receiptIdMap.size());
    }
}

