package DomainLayer.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import DomainLayer.Market.Market;
import static org.junit.jupiter.api.Assertions.*;

import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import DomainLayer.User.UserFacade;
import PresentationLayer.Application;
import Util.ExceptionsEnum;
import Util.PaymentServiceDTO;
import Util.SupplyServiceDTO;
import Util.UserDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.HashSet;

import Util.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Transactional
@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class StateInit {
    private static BridgeToTests impl;

    @Autowired
    private Market market;
    private UserFacade userFacade;
    private StoreFacade storeFacade;
    private RoleFacade roleFacade;
    private PaymentServicesFacade paymentServicesFacade;
    private SupplyServicesFacade supplyServicesFacade;



    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        this.userFacade = market.getUserFacade();
        this.storeFacade = market.getStoreFacade();
        this.paymentServicesFacade =market.getPaymentServiceFacade();
        this.supplyServicesFacade = market.getSupplyServicesFacade();
        this.roleFacade = market.getRoleFacade();
        //market = new Market(userFacade, storeFacade, paymentServicesFacade, supplyServicesFacade,roleFacade);

    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void successfulInitTest() throws Exception {
        assertFalse(market.isInitialized());
        assertEquals(0, market.getSystemManagerIds().size());
        assertEquals(0, userFacade.getMembers().findAll().size());
        assertEquals(0, storeFacade.getStores().size());
        assertEquals(0, paymentServicesFacade.getAllPaymentServices().size());
        assertEquals(0, supplyServicesFacade.getAllSupplyServices().size());

        market.init();
        market.startStateInitialization();

        assertTrue(market.isInitialized());
        assertEquals(1, market.getSystemManagerIds().size());
        String memberID = market.getSystemManagerIds().iterator().next();

        // assert the system manager details match the details in the config file
        assertEquals("u1", userFacade.getMembers().getById(memberID).getUsername());
        assertEquals("19/09/1996", userFacade.getMembers().getById(memberID).getBirthday());
        assertEquals("Israel", userFacade.getMembers().getById(memberID).getCountry());
        assertEquals("Ashdod", userFacade.getMembers().getById(memberID).getCity());
        assertEquals("Elul", userFacade.getMembers().getById(memberID).getAddress());
        assertEquals("David Volodarsky", userFacade.getMembers().getById(memberID).getName());

        assertEquals(6, userFacade.getMembers().findAll().size());
        assertEquals(1, storeFacade.getStores().size());
        String storeID = storeFacade.getStores().iterator().next();
        assertTrue( storeFacade.getStoreByID(storeID).getStoreProducts().containsKey("Bamba"));
        String memberId = userFacade.getMemberByUsername("u1").getMemberID();
        assertTrue(roleFacade.verifyMemberIsSystemManager(memberId));
        assertEquals(1, paymentServicesFacade.getAllPaymentServices().size());
        assertEquals(1, supplyServicesFacade.getAllSupplyServices().size());

    }
}