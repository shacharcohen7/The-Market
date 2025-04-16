package DomainLayer.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GuestTest {

    private Guest guest;

    @BeforeEach
    public void setUp() {
        guest = new Guest();
    }

    @Test
    public void testLogoutThrowsException() {
        // Assert that calling logout throws an IllegalArgumentException
        assertThrows(IllegalArgumentException.class, guest::Logout);
    }

    @Test
    public void testExit() {
        assertDoesNotThrow(() -> guest.exitMarketSystem());
        // No state change or other behavior to verify in Exit method for Guest
    }
}
