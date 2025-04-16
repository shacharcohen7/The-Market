package DomainLayer.SupplyServices;
import Util.ShippingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ExternalSupplyServicesTests {
    private ExternalSupplyService externalSupplyService;
    private HashSet<String> mockCountries;
    private HashSet<String> mockCities;
    private HashSet<String> shiftIdAndDetails;
    private ShippingDTO shippingDTOMock; // Add mock for ShiftingDetails


    @BeforeEach
    public void setUp() {
        // Mock the sets of countries and cities

        HashSet<String> countries =new HashSet<>();
        HashSet<String> cities =new HashSet<>();

        countries.add("Israel");
        cities.add("Bash");
        shippingDTOMock = Mockito.mock(ShippingDTO.class);
        shiftIdAndDetails = Mockito.mock(HashSet.class);


        // Initialize the service with the mock data
        externalSupplyService = new ExternalSupplyService("https://damp-lynna-wsep-1984852e.koyeb.app/");
    }

    @Test
    public void testGetSupplyURL() {
        assertEquals("https://damp-lynna-wsep-1984852e.koyeb.app/", externalSupplyService.getSupplyURL());
    }

    @Test
    public void testCheckAreaAvailability_CountryNotAvailable() {


//        assertFalse(externalSupplyService.checkAreaAvailability("France", "Bash"));
    }

    @Test
    public void testCheckAreaAvailability_CityNotAvailable() {

//        assertFalse(externalSupplyService.checkAreaAvailability("Israel", "MockCity"));
    }

//    @Test
//    public void testAddCountries() {
//        assertEquals(1,externalSupplyService.getCountries().size());
//        HashSet<String> countries1 = new HashSet<>();
//        countries1.add("Israel");
//        countries1.add("France");
//        externalSupplyService.addCountries(countries1);
//        assertEquals(2,externalSupplyService.getCountries().size());
//
//    }

//    @Test
//    public void testAddCities() {
//        assertEquals(1,externalSupplyService.getCities().size());
//        HashSet<String> cities1 = new HashSet<>();
//        cities1.add("Ashdod");
//        cities1.add("Tel aviv");
//        externalSupplyService.addCities(cities1);
//        assertEquals(3,externalSupplyService.getCities().size());
//
//    }

    @Test
    public void testCheckAreaAvailability_AreaAvailable() {


        assertTrue(externalSupplyService.checkAreaAvailability("Israel", "Bash"));
    }


}
