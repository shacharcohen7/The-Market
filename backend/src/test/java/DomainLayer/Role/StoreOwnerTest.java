package DomainLayer.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StoreOwnerTest {

    private StoreOwner storeOwner;

    @BeforeEach
    public void setUp() {
        // Initialize StoreOwner with mock data
        storeOwner = new StoreOwner("member-1", "store-1", true, "nominator-1", false);
    }

    @Test
    public void testGetStore_ID() {
        assertEquals("store-1", storeOwner.getStore_ID());
    }

    @Test
    public void testGetNominatorId() {
        assertEquals("nominator-1", storeOwner.getNominatorId());
    }

    @Test
    public void testGetMember_ID() {
        assertEquals("member-1", storeOwner.getMember_ID());
    }

    @Test
    public void testVerifyStoreOwnerIsFounder_True() {
        assertTrue(storeOwner.verifyStoreOwnerIsFounder());
    }

    @Test
    public void testVerifyStoreOwnerIsFounder_False() {
        StoreOwner nonFounderOwner = new StoreOwner("member-2", "store-2", false, "nominator-2", false);
        assertFalse(nonFounderOwner.verifyStoreOwnerIsFounder());
    }
}
