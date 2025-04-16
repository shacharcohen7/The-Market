package DomainLayer.Role;

import DomainLayer.Role.*;
import Util.ExceptionsEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RoleFacadeTest {

    @InjectMocks
    private RoleFacade roleFacade;

    @Mock
    private StoreOwner mockStoreOwner;

    @Mock
    private StoreManager mockStoreManager;

    @Mock
    private SystemManager mockSystemManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        RoleFacade.resetInstanceForTests();
        roleFacade = RoleFacade.getInstance();
    }

    @Test
    public void testGetInstance() {
        RoleFacade instance = RoleFacade.getInstance();
        assertNotNull(instance);
        assertEquals(instance, roleFacade);
    }

    @Test
    public void testVerifyStoreOwner() throws Exception {
        when(mockStoreOwner.getStore_ID()).thenReturn("store1");
        when(mockStoreOwner.getMember_ID()).thenReturn("member1");

        roleFacade.addNewStoreOwnerToTheMarketForTests(mockStoreOwner);

        assertTrue(roleFacade.verifyStoreOwner("store1", "member1"));
        assertFalse(roleFacade.verifyStoreOwner("store2", "member1"));
    }

    @Test
    public void testVerifyStoreManager() throws Exception {
        when(mockStoreManager.getStore_ID()).thenReturn("store2");
        when(mockStoreManager.getMember_ID()).thenReturn("member2");

        roleFacade.addNewStoreManagerToTheMarketForTests(mockStoreManager);

        assertTrue(roleFacade.verifyStoreManager("store2", "member2"));
        assertFalse(roleFacade.verifyStoreManager("store1", "member2"));
    }

    @Test
    public void testVerifyStoreOwnerError() {
        Exception exception = assertThrows(Exception.class, () -> {
            roleFacade.verifyStoreOwnerError("store1", "member1");
        });
        assertEquals(ExceptionsEnum.userIsNotStoreOwner.toString(), exception.getMessage());
    }

    @Test
    public void testCreateStoreOwner() throws Exception {
        when(mockStoreOwner.getMember_ID()).thenReturn("member3");
        when(mockStoreOwner.getStore_ID()).thenReturn("store3");
        when(mockStoreOwner.verifyStoreOwnerIsFounder()).thenReturn(true);

        roleFacade.addNewStoreOwnerToTheMarketForTests(mockStoreOwner);

        assertTrue(roleFacade.verifyStoreOwner("store3", "member3"));
    }

    @Test
    public void testCreateStoreManager() throws Exception {
        when(mockStoreManager.getMember_ID()).thenReturn("member4");
        when(mockStoreManager.getStore_ID()).thenReturn("store4");

        roleFacade.addNewStoreManagerToTheMarketForTests(mockStoreManager);

        assertTrue(roleFacade.verifyStoreManager("store4", "member4"));
    }

    @Test
    public void testUpdateStoreManagerPermissions() throws Exception {
        when(mockStoreManager.getStore_ID()).thenReturn("store5");
        when(mockStoreManager.getMember_ID()).thenReturn("member5");
        when(mockStoreManager.getNominatorMemberId()).thenReturn("nominator5");
        doNothing().when(mockStoreManager).setPermissions(anyBoolean(), anyBoolean());

        roleFacade.addNewStoreManagerToTheMarketForTests(mockStoreManager);

        roleFacade.updateStoreManagerPermissions("member5", "store5", false, true, "nominator5");
        verify(mockStoreManager).setPermissions(false, true);
    }

    @Test
    public void testGetInformationAboutStoreRoles() throws Exception {
        when(mockStoreOwner.getMember_ID()).thenReturn("member6");
        when(mockStoreOwner.getStore_ID()).thenReturn("store6");
        when(mockStoreManager.getMember_ID()).thenReturn("member7");
        when(mockStoreManager.getStore_ID()).thenReturn("store6");

        roleFacade.addNewStoreOwnerToTheMarketForTests(mockStoreOwner);
        roleFacade.addNewStoreManagerToTheMarketForTests(mockStoreManager);

        Map<String, String> roles = roleFacade.getInformationAboutStoreRoles("store6");
        assertEquals("owner", roles.get("member6"));
        assertEquals("manager", roles.get("member7"));
    }

    @Test
    public void testGetStoresByOwner() throws Exception {
        when(mockStoreOwner.getMember_ID()).thenReturn("member8");
        when(mockStoreOwner.getStore_ID()).thenReturn("store8");

        roleFacade.addNewStoreOwnerToTheMarketForTests(mockStoreOwner);

        List<String> stores = Arrays.asList("store8", "store9");
        List<String> ownedStores = roleFacade.getStoresByOwner(stores, "member8");
        assertTrue(ownedStores.contains("store8"));
        assertFalse(ownedStores.contains("store9"));
    }

    @Test
    public void testVerifyMemberIsSystemManagerError() {
        Exception exception = assertThrows(Exception.class, () -> {
            roleFacade.verifyMemberIsSystemManagerError("system1");
        });
        assertEquals(ExceptionsEnum.notSystemManager.toString(), exception.getMessage());
    }
}
