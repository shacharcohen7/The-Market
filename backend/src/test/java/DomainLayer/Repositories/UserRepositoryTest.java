package DomainLayer.Repositories;

import DomainLayer.User.User;
import PresentationLayer.Application;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ContextConfiguration(classes = {Application.class})
@SpringBootTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        userRepository.save(user);
    }

    // Test methods
    @Test
    public void testFindUserByUsername() {
        User foundUser = userRepository.findById(user.getUserID()).orElse(null);
        assertEquals(user, foundUser);
    }
}
