package DomainLayer.Role;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

public class StoreManagerTest {

    private StoreManager storeManager;

    @BeforeEach
    public void setUp() {
        // Initialize StoreManager with mock data
        storeManager = new StoreManager("member-1", "store-1", true, false, "nominator-1", false);
    }

    @Test
    public void testGetNominatorMemberId() {
        assertEquals("nominator-1", storeManager.getNominatorMemberId());
    }

    @Test
    public void testGetStore_ID() {
        assertEquals("store-1", storeManager.getStore_ID());
    }

    @Test
    public void testGetMember_ID() {
        assertEquals("member-1", storeManager.getMember_ID());
    }

    @Test
    public void testSetPermissions() {
        // Arrange
        storeManager.setPermissions(true, true);

        // Assert
        assertTrue(storeManager.hasInventoryPermissions());
        assertTrue(storeManager.hasPurchasePermissions());
    }

    @Test
    public void testGetAuthorizations() throws IllegalAccessException {
        // Arrange
        List<Integer> authorizations = Arrays.asList(1, 2, 3);
        // Set authorizations using reflection as the class does not provide a method to do so
        FieldUtils.writeField(storeManager, "authorizations", authorizations, true);

        // Act
        List<Integer> retrievedAuthorizations = storeManager.getAuthorizations();

        // Assert
        assertEquals(authorizations, retrievedAuthorizations);
    }

    @Test
    public void testHasInventoryPermissions() {
        assertTrue(storeManager.hasInventoryPermissions());
    }

    @Test
    public void testHasPurchasePermissions() {
        assertFalse(storeManager.hasPurchasePermissions());
    }
}
