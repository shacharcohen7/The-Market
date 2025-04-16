package IntegrationTests.Role;

import AcceptanceTests.RealToTest;
import DomainLayer.Repositories.StoreManagerRepository;
import DomainLayer.Repositories.StoreOwnerRepository;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Role.StoreManager;
import DomainLayer.Role.StoreOwner;
import PresentationLayer.Application;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class RoleFacadeTest {

    @Autowired
    private StoreManagerRepository storeManagerRepository;

    @Autowired
    private StoreOwnerRepository storeOwnerRepository;

     @Autowired
    private StoreManagerRepository managerNominatorRepository;

    @Autowired
    private StoreOwnerRepository ownerNominatorRepository;

    private RoleFacade roleFacade;


    @BeforeEach
    public void setUp() {
        roleFacade = new RoleFacade(storeManagerRepository, storeOwnerRepository);
    }

    @AfterEach
    public void tearDown() {
        storeManagerRepository.deleteAll();
        storeOwnerRepository.deleteAll();

    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    void testVerifyStoreOwner() throws Exception {
        roleFacade.createStoreOwnerWithoutAsk("1", "1", true, "2");
        assertTrue(roleFacade.verifyStoreOwner("1", "1"));
        assertFalse(roleFacade.verifyStoreOwner("2", "1"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    void testVerifyStoreManager() throws Exception {
        roleFacade.createStoreManagerWithoutAsk("1", "1", true, true, "2");
        assertTrue(roleFacade.verifyStoreManager("1", "1"));
        assertFalse(roleFacade.verifyStoreManager("2", "1"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    void testVerifyStoreOwnerIsFounder() throws Exception {
        roleFacade.createStoreOwnerWithoutAsk("1", "1", true, "2");
        assertTrue(roleFacade.verifyStoreOwnerIsFounder("1", "1"));
        assertFalse(roleFacade.verifyStoreOwnerIsFounder("1", "2"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    void testManagerHasInventoryPermissions() throws Exception {
        roleFacade.createStoreManagerWithoutAsk("1", "1", true, false, "2");
        assertTrue(roleFacade.managerHasInventoryPermissions("1", "1"));
        assertFalse(roleFacade.managerHasInventoryPermissions("2", "1"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    void testManagerHasPurchasePermissions() throws Exception {
        roleFacade.createStoreManagerWithoutAsk("1", "1", false, true, "2");
        assertTrue(roleFacade.managerHasPurchasePermissions("1", "1"));
        assertFalse(roleFacade.managerHasPurchasePermissions("2", "1"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    void testUpdateStoreManagerPermissions() throws Exception {
        roleFacade.createStoreManagerWithoutAsk("1", "1", false, false, "2");
        roleFacade.updateStoreManagerPermissions("1", "1", true, true, "2");
        StoreManager storeManager = roleFacade.getStoreManager("1", "1");
        assertTrue(storeManager.hasInventoryPermissions());
        assertTrue(storeManager.hasPurchasePermissions());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    void testGetInformationAboutStoreRoles() throws Exception {
        roleFacade.createStoreOwnerWithoutAsk("1", "1", true, "2");
        roleFacade.createStoreManagerWithoutAsk("2", "1", true, true, "3");

        Map<String, String> roles = roleFacade.getInformationAboutStoreRoles("1");
        assertEquals(2, roles.size());
        assertEquals("owner", roles.get("1"));
        assertEquals("manager", roles.get("2"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    void testGetStoreManagersAuthorizations() throws Exception {
        roleFacade.createStoreManagerWithoutAsk("1", "1", true, false, "2");
        roleFacade.createStoreManagerWithoutAsk("2", "1", false, true, "3");

        Map<String, List<Integer>> authorizations = roleFacade.getStoreManagersAuthorizations("1");
        assertEquals(2, authorizations.size());
        assertTrue(authorizations.containsKey("1"));
        assertTrue(authorizations.containsKey("2"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    void testGetAllStoreManagers() throws Exception {
        roleFacade.createStoreManagerWithoutAsk("1", "1", true, false, "2");
        roleFacade.createStoreManagerWithoutAsk("2", "1", false, true, "3");

        List<String> managers = roleFacade.getAllStoreManagers("1");
        assertEquals(2, managers.size());
        assertTrue(managers.contains("1"));
        assertTrue(managers.contains("2"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    void testGetAllStoreOwners() throws Exception {
        roleFacade.createStoreOwnerWithoutAsk("1", "1", true, "2");
        roleFacade.createStoreOwnerWithoutAsk("2", "1", false, "3");

        List<String> owners = roleFacade.getAllStoreOwners("1");
        assertEquals(2, owners.size());
        assertTrue(owners.contains("1"));
        assertTrue(owners.contains("2"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    void testGetStoresByOwner() throws Exception {
        List<String> stores = new ArrayList<>();
        stores.add("1");
        stores.add("2");

        roleFacade.createStoreOwnerWithoutAsk("1", "1", true, "2");
        roleFacade.createStoreOwnerWithoutAsk("1", "2", false, "3");

        List<String> storesByOwner = roleFacade.getStoresByOwner(stores, "1");
        assertEquals(2, storesByOwner.size());
        assertTrue(storesByOwner.contains("1"));
        assertTrue(storesByOwner.contains("2"));
    }

}
