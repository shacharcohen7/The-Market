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
public class AppointOwner {

    @Autowired
    private BridgeToTests impl;

    static String saarUserID;
    static String tomUserID;
    static String jalalUserID;
    static String samiUserID;
    static String storeID;
    static String secondOwnerID;
    private static final String JALAL_USERNAME = "jalal";
    private static final String JALAL_PASSWORD = "Kasoomm3";
    private static final String SECOND_OWNER_USERNAME = "secondOwner";
    private static final String SECOND_OWNER_PASSWORD = "SecondOwner1";


    @BeforeEach
    public void setUp() {
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(tomUserID,"tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "Shlaifer2");
        jalalUserID = impl.enterMarketSystem().getData();
        impl.register(jalalUserID,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "Kasoomm3");
        impl.login(saarUserID, "saar", "Fadidaa1");
        impl.login(tomUserID, "tom", "Shlaifer2");


        storeID = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.appointStoreOwner(saarUserID, "tom", storeID);
        impl.answerJobProposal(tomUserID, storeID,false, true);
        samiUserID = impl.enterMarketSystem().getData();
        impl.register(samiUserID,"sami", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "sami", "Ka939kkmm3");
        secondOwnerID = impl.enterMarketSystem().getData();
        impl.register(secondOwnerID, SECOND_OWNER_USERNAME, "15/05/83", "Israel", "Jerusalem", "Yehuda halevi 12", SECOND_OWNER_USERNAME, SECOND_OWNER_PASSWORD);
        jalalUserID = impl.enterMarketSystem().getData();
        impl.register(jalalUserID, JALAL_USERNAME, "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", JALAL_USERNAME, JALAL_PASSWORD);
    }

    @AfterEach
    public void tearDown() {
        impl.resetAllTables();
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void successfulAppointedTest() {
        assertTrue(impl.appointStoreOwner(saarUserID, "sami",storeID).isSuccess());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void alreadyStoreOwnerTest() {
        Response<String> response = impl.appointStoreOwner(saarUserID, "tom",storeID);
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.memberIsAlreadyStoreOwner.toString(), response.getDescription());
    }

    @Test
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
    public void concurrentAppointOwnerTest() throws InterruptedException {
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
                    Response<String> response = impl.appointStoreOwner(saarUserID, JALAL_USERNAME, storeID);
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
                    Response<String> response = impl.appointStoreOwner(secondOwnerID, JALAL_USERNAME, storeID);
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