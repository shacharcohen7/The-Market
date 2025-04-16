package IntegrationTests.PaymentServices;

import AcceptanceTests.RealToTest;
import DomainLayer.PaymentServices.ExternalPaymentService;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Repositories.AcquisitionRepository;
import DomainLayer.Repositories.ExternalPaymentRepository;
import PresentationLayer.Application;
import Util.PaymentDTO;
import Util.PaymentServiceDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class PaymentServicesFacadeTest {


    @Autowired
    private ExternalPaymentRepository externalPaymentRepository;

    @Autowired
    private AcquisitionRepository acquisitionRepository;


    private PaymentServicesFacade paymentServicesFacade;

    private PaymentDTO paymentDTO;

    @BeforeEach
    public void setUp() throws Exception {
        //MockitoAnnotations.openMocks(this);
        paymentServicesFacade = new PaymentServicesFacade(externalPaymentRepository, acquisitionRepository);
        paymentDTO = new PaymentDTO("130", "david", "USD", "1234567812345678", 100, 6, 2030);

    }

    @AfterEach
    public void tearDown() {
        externalPaymentRepository.deleteAll();
        acquisitionRepository.deleteAll();
    }



    @Test
    @ExtendWith(MockitoExtension.class)
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testAddExternalServiceWithParams() throws Exception {
        //paymentServicesFacade =(mock(PaymentServicesFacade.class));
//        ExternalPaymentService externalPaymentService1 = mock(ExternalPaymentService.class);
//        when(externalPaymentService1.checkHandShake()).thenReturn(true);
        //doReturn(true).when(paymentServicesFacade).checkHandShake(any());

        boolean added = paymentServicesFacade.addExternalService("https://damp-lynna-wsep-1984852e.koyeb.app/");

        assertTrue(added);
        ExternalPaymentService service = paymentServicesFacade.getPaymentServiceByURL("https://damp-lynna-wsep-1984852e.koyeb.app/");
        assertNotNull(service);
        ExternalPaymentService service2 = paymentServicesFacade.getPaymentServiceByURL(service.getUrl());
        assertNotNull(service2);
        assertEquals("https://damp-lynna-wsep-1984852e.koyeb.app/", service.getUrl());
        assertEquals("https://damp-lynna-wsep-1984852e.koyeb.app/", service2.getUrl());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testAddExternalServiceWithDTO() throws Exception {
        //  PaymentServiceDTO paymentServiceDTO = new PaymentServiceDTO("123", "TestService", "http://test.com");

        boolean added = paymentServicesFacade.addExternalService("https://damp-lynna-wsep-1984852e.koyeb.app/");
        assertTrue(added);

        ExternalPaymentService service = paymentServicesFacade.getPaymentServiceByURL("https://damp-lynna-wsep-1984852e.koyeb.app/");
        assertNotNull(service);
        assertEquals("https://damp-lynna-wsep-1984852e.koyeb.app/", service.getUrl());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testBadRemoveExternalService() throws Exception {
        String url = "https://damp-lynna-wsep-1984852e.koyeb.app/";
        paymentServicesFacade.addExternalService(url);
        try {
            paymentServicesFacade.removeExternalService(url);
        }
        catch (Exception e){

        }

        ExternalPaymentService service = paymentServicesFacade.getPaymentServiceByURL(url);
        assertNotNull(service);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testPaySuccess() throws Exception {
        paymentServicesFacade.addExternalService("https://damp-lynna-wsep-1984852e.koyeb.app/");

        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
        Map<String, List<Integer>> storeProducts = new HashMap<>();
        storeProducts.put("product1", Arrays.asList(2, 100));
        productList.put("store1", storeProducts);

        String result = paymentServicesFacade.pay(100, paymentDTO, "userId",null, productList);

        assertNotNull(result);
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testGetAllPaymentServices() throws Exception {
        paymentServicesFacade.addExternalService("https://damp-lynna-wsep-1984852e.koyeb.app/");

        Map<String, ExternalPaymentService> allServices = paymentServicesFacade.getAllPaymentServices();
        assertEquals(1, allServices.size());
        assertTrue(allServices.containsKey("https://damp-lynna-wsep-1984852e.koyeb.app/"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testGetPaymentServiceDTOById() throws Exception {
        paymentServicesFacade.addExternalService("http://test.com");

//        PaymentServiceDTO dto = paymentServicesFacade.getPaymentServiceDTOById("123");
//        assertNotNull(dto);
//        assertEquals("123", dto.getLicensedDealerNumber());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testGetStorePurchaseInfo() throws Exception {
        paymentServicesFacade.addExternalService("https://damp-lynna-wsep-1984852e.koyeb.app/");

        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
        Map<String, List<Integer>> storeProducts = new HashMap<>();
        storeProducts.put("product1", Arrays.asList(2, 100));
        productList.put("store1", storeProducts);

        paymentServicesFacade.pay(100, paymentDTO, "userId", null, productList);


        Map<String, Integer> storePurchaseInfo = paymentServicesFacade.getStorePurchaseInfo();
        assertNotNull(storePurchaseInfo);
        assertEquals(1, storePurchaseInfo.size());
        assertTrue(storePurchaseInfo.containsKey("store1"));
        assertEquals(1, storePurchaseInfo.get("store1"));
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void testGetStoreReceiptsAndTotalAmount() throws Exception {
        paymentServicesFacade.addExternalService("https://damp-lynna-wsep-1984852e.koyeb.app/");

        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
        Map<String, List<Integer>> storeProducts = new HashMap<>();
        storeProducts.put("product1", Arrays.asList(2, 100));
        productList.put("store1", storeProducts);

        try {
            paymentServicesFacade.pay(100, paymentDTO, "userId", null,productList);
        } catch (Exception e) {
            fail("Payment failed");
        }

        Map<String, Integer> storeReceiptsAndTotalAmount = paymentServicesFacade.getStoreReceiptsAndTotalAmount("store1");
        assertNotNull(storeReceiptsAndTotalAmount);
        assertEquals(1, storeReceiptsAndTotalAmount.size());
    }
}