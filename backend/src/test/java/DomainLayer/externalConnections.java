package DomainLayer;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import PresentationLayer.WAF.*;


import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import static org.junit.jupiter.api.Assertions.*;

import DomainLayer.PaymentServices.ExternalPaymentService;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.SupplyServices.ExternalSupplyService;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import DomainLayer.User.UserFacade;
import Util.ExceptionsEnum;
import Util.PaymentServiceDTO;
import Util.SupplyServiceDTO;
import Util.CartDTO;

import Util.UserDTO;
import jakarta.inject.Inject;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;

import java.util.*;

import Util.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;


@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class externalConnections {
//    private static BridgeToTests impl;
    private static PaymentServicesFacade paymentServicesFacade;
    private static StoreFacade storeFacade;
//    private static UserFacade userFacade;
//    private static Service_layer serviceLayer;

    @Inject
    private Market market;

    @Inject
    private Service_layer serviceLayer;

    @BeforeAll()
    public void setUp() throws Exception {
        market.init();
        paymentServicesFacade= market.getPaymentServiceFacade();
        storeFacade= market.getStoreFacade();
       // userf
        //market = new Market(paymentServicesFacade, storeFacade);
        //market.init();
        //serviceLayer = new Service_layer(market);
    }




    @Test
    public void checkPurchase() throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO("20444444", "David David", "USD", "2222333344445555", 982, 6, 2030);
        UserDTO userDTO = new UserDTO(market.enterMarketSystem());
        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
        List<Integer> priceQuantity = new ArrayList<>();
        priceQuantity.add(20);
        priceQuantity.add(20);
        Map<String, List<Integer>> productNames = new HashMap<>();
        productNames.put("Bamba", priceQuantity);
        String storeId = storeFacade.getStoreId("s1");
        productList.put(storeId,productNames);
        CartDTO cartDTO = new CartDTO(userDTO.getUserId(),1000, productList );
        serviceLayer.purchase(userDTO,paymentDTO,cartDTO);
    }

    @Test
    @Order(3)
    public void checkPaymentSuccess() throws Exception {
        Map<String, Map<String, List<Integer>>> products = new HashMap<>();
        Map<String, List<Integer>> products1 = new HashMap<>();
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        products1.put("bam", list1);
        products.put("store1", products1);
        ExternalPaymentService externalPaymentService = market.getPaymentServicesFacade().getPaymentServiceByURL("https://damp-lynna-wsep-1984852e.koyeb.app/");
        CartDTO cartDTO = new CartDTO("130", 5500, products);
        PaymentDTO paymentDTO = new PaymentDTO("20444444", "David David", "USD", "2222333344445555", 982, 6, 2030);
        int res = externalPaymentService.payWithCard(1000, paymentDTO);
        assertTrue(res>=10000);
        assertTrue(res<=100000);

    }


    @Test
    public void checkPaymentAndCancelSuccess() throws Exception {

        Map<String, Map<String, List<Integer>>> products = new HashMap<>();
        Map<String, List<Integer>> products1 = new HashMap<>();
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        products1.put("bam", list1);
        products.put("store1", products1);
        ExternalPaymentService externalPaymentService = market.getPaymentServicesFacade().getPaymentServiceByURL("https://damp-lynna-wsep-1984852e.koyeb.app/");
        CartDTO cartDTO = new CartDTO("130", 5500, products);
        PaymentDTO paymentDTO = new PaymentDTO("20444444", "David David", "USD", "2222333344445555", 982, 6, 2030);
        int res = externalPaymentService.payWithCard(1000, paymentDTO);
        System.out.println("transss is:" +  res);
        int res1= externalPaymentService.cancelPayment(res);
        assertEquals(1,res1);


    }

    @Test
    public void checkSupplySuccess() throws Exception {

        ExternalSupplyService supplyServices = market.getSupplyServicesFacade().getAllSupplyServices().get("https://damp-lynna-wsep-1984852e.koyeb.app/");
        int res = supplyServices.createSupply("david", "Israel", "Ashdod", "Elul", "3");
        assertTrue(res>=10000);
        assertTrue(res<=100000);

    }

//    @Test
//    public void checkSupplyAndCancelSuccess() throws Exception {
//        ExternalSupplyService supplyServices = market.getSupplyServicesFacade().getAllSupplyServices().get("https://damp-lynna-wsep-1984852e.koyeb.app/");
//        supplyServices.createSupply("david", "Israel", "Ashdod", "Elul", "4");
//        String shippingId = supplyServices.getShippingId("4");
//        int res1= supplyServices.cancelSupply(shippingId);
//        assertEquals(1,res1);
//    }

    @Test
    public void isValidAcquisitionIdID() throws Exception {
        assertTrue(paymentServicesFacade.isValidTransactionIdID(54656));
        assertTrue(paymentServicesFacade.isValidTransactionIdID(84656));
        assertTrue(paymentServicesFacade.isValidTransactionIdID(99656));
        assertFalse(paymentServicesFacade.isValidTransactionIdID(-1));
        assertFalse(paymentServicesFacade.isValidTransactionIdID(155));
        assertFalse(paymentServicesFacade.isValidTransactionIdID(564646644));



    }
}