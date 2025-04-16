package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import PresentationLayer.Application;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AppointManager {

    @Autowired
    private BridgeToTests impl;

    static String saarUserID;
    static String tomUserID;
    static String jalalUserID;
    static String ovadUserID;
    static String storeId;

    private static final String JALAL_USERNAME = "jalal";
    private static final String JALAL_PASSWORD = "Kasoomm3";

    @BeforeEach
    public void setUp() {
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar",  "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(tomUserID,"tom",  "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "Shlaifer2");
        jalalUserID = impl.enterMarketSystem().getData();
        impl.register(jalalUserID,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "Kasoomm3");
        ovadUserID = impl.enterMarketSystem().getData();
        impl.register(ovadUserID,"ovad", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad", "Haviaaa4");
        impl.login(saarUserID, "saar", "Fadidaa1");
        storeId = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.appointStoreOwner(saarUserID, "tom", storeId);
        impl.appointStoreManager(saarUserID, "jalal", storeId, true, false);
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    @Test
    public void successfulAppointmentTest() {
        setUp();
        assertTrue(impl.appointStoreManager(saarUserID, "ovad",
                storeId, true, false).isSuccess());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void alreadyHasRoleInThisStore() {
        Response<String> response1 = impl.appointStoreManager(saarUserID, "tom",
                storeId, true, false);
        assertFalse(response1.isSuccess());
        assertEquals("this member already nominated to be store manager in this store", response1.getDescription());

        Response<String> response2 = impl.appointStoreManager(saarUserID, "jalal",
                storeId, true, false);
        assertFalse(response2.isSuccess());
        assertEquals("this member already nominated to be store manager in this store", response2.getDescription());

    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void concurrentAppointManagerTest() throws InterruptedException {
        // Create a latch to synchronize the start of both threads
        CountDownLatch latch = new CountDownLatch(1);

        // Atomic booleans to track appointment results
        AtomicBoolean appointmentSucceeded = new AtomicBoolean(false);
        AtomicBoolean appointmentFailed = new AtomicBoolean(false);

        for (int i=0; i<1000 ; i++) {
            // Define the first thread (Saar appointing Jalal)
            setUp();
            Thread thread1 = new Thread(() -> {
                try {
                    latch.await(); // Wait for the latch to be released
                    Response<String> response = impl.appointStoreManager(saarUserID, "ovad", storeId, true ,true);
                    if (response.isSuccess()) {
                        appointmentSucceeded.set(true);
                    } else {
                        appointmentFailed.set(true);
                        assertEquals(ExceptionsEnum.memberIsAlreadyStoreOwner.toString(), response.getDescription());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            // Define the second thread (SecondOwner appointing Jalal)
            Thread thread2 = new Thread(() -> {
                try {
                    latch.await(); // Wait for the latch to be released
                    Response<String> response = impl.appointStoreManager(tomUserID, "ovad", storeId, false, false);
                    if (response.isSuccess()) {
                        appointmentSucceeded.set(true);
                    } else {
                        appointmentFailed.set(true);
                        assertEquals(ExceptionsEnum.memberIsAlreadyStoreOwner.toString(), response.getDescription());
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            // Start both threads
            thread1.start();
            thread2.start();

            // Release the latch, allowing both threads to proceed
            latch.countDown();

            // Wait for both threads to finish
            thread1.join();
            thread2.join();

            // Verify that exactly one appointment succeeded and one failed
            assertTrue(appointmentSucceeded.get(), "One of the appointments should have succeeded");
            assertTrue(appointmentFailed.get(), "One of the appointments should have failed");
        }
    }
}
