package DomainLayer.SupplyServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplyServicesFacadeTest {
    private SupplyServicesFacade supplyServicesFacade;

    @BeforeEach
    public void setUp() {
        supplyServicesFacade = SupplyServicesFacade.getInstance();
        supplyServicesFacade.reset();// Reset the state before each test

    }

    @Test
    public void testAddExternalService() throws Exception {
        // Create mocks for parameters
        String url = "https://damp-lynna-wsep-1984852e.koyeb.app/";
        String supplyServiceName = "MockService";
        HashSet<String> countries =new HashSet<>();
        HashSet<String> cities =new HashSet<>();

        countries.add("Israel");
        cities.add("Bash");

        // Mock externalSupplyService
        Map<Integer, ExternalSupplyService> externalSupplyServiceMapMock = Mockito.mock(HashMap.class);
        SupplyServicesFacade supplyServicesFacadeSpy = spy(supplyServicesFacade);
      //  doReturn(externalSupplyServiceMapMock).when(supplyServicesFacadeSpy).getExternalSupplyServiceMap();

        // Call method
        boolean result = supplyServicesFacadeSpy.addExternalService( url);

        // Check result
        assertTrue(result);
    }

    @Test
    public void testCheckAvailableExternalSupplyService() throws Exception {
        // Mock externalSupplyService


        SupplyServicesFacade supplyServicesFacadeSpy = spy(supplyServicesFacade);

        String licensedDealerNumber = "12345";
        String supplyServiceName = "MockService";
        HashSet<String> countries =new HashSet<>();
        HashSet<String> cities =new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");

      supplyServicesFacadeSpy.addExternalService("https://damp-lynna-wsep-1984852e.koyeb.app/");

        //    doReturn(externalSupplyServiceMapMock).when(supplyServicesFacadeSpy).getExternalSupplyServiceMap();

        // Call method
        String result = supplyServicesFacadeSpy.checkAvailableExternalSupplyService("Israel", "Bash");
        assertEquals("https://damp-lynna-wsep-1984852e.koyeb.app/", result);

//        String result1 = supplyServicesFacadeSpy.checkAvailableExternalSupplyService("Israel", "Ashdod");
//        assertEquals("-2", result1);
//

    }

    @Test
    public void testCreateShiftingDetails() throws Exception {
        // Ensure that the initial size of shiftIdAndDetails is zero
        assertEquals(0, supplyServicesFacade.getSystemHistory().size());

        SupplyServicesFacade supplyServicesFacadeSpy = spy(supplyServicesFacade);
        supplyServicesFacadeSpy.addExternalService("https://damp-lynna-wsep-1984852e.koyeb.app/");
        boolean res = supplyServicesFacade.createShiftingDetails("User1", "MockCountry", "MockCity", "MockAddress", "10");
        assertTrue(res);
        assertEquals(1, supplyServicesFacade.getSystemHistory().size());
        assertNotNull(supplyServicesFacade.getAllSupplyServices().values());
    }


}
