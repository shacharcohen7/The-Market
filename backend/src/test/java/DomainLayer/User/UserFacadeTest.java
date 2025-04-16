package DomainLayer.User;

import DomainLayer.Repositories.MemberRepository;
import DomainLayer.Repositories.UserRepository;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserFacadeTest {
    @Mock
    private User mockUser;
    @Mock
    private Member mockMember;
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private MemberRepository mockMemberRepository;
    @InjectMocks
    private UserFacade userFacade;
    private final String userId = "1";
    private final String storeId = "1";
    private final String productName = "Product1";
    private final int quantity = 2;
    private final int totalPrice = 100;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        userFacade = new UserFacade(mockUserRepository, mockMemberRepository);

        // Mocking the behavior of userRepository
        when(mockUserRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            when(mockUserRepository.getById(user.getUserID())).thenReturn(user);
            return user;
        });

        when(mockUserRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));
        when(mockUserRepository.getById(userId)).thenReturn(mockUser);

        // Configure mockMember to return specific values
        when(mockMember.getUsername()).thenReturn("testUser");
        when(mockMember.getUserId()).thenReturn(userId);
    }

    @Test
    public void testGetUserByID() {
        assertEquals(mockUser, userFacade.getUserByID("1"));
    }

    @Test
    public void testAddUser() {
        String userId = userFacade.addUser();
        when(mockUserRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));
        assertNotNull(userFacade.getUserByID(userId));
    }

    @Test
    public void testRegister() throws Exception {
        String userId = userFacade.addUser();
        when(mockUserRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));
        userFacade.register(userId, new UserDTO(userId, "testUser", "01/01/2000", "Test Country", "Test City", "123 Test St", "Test Name"), "testPass");

        when(mockMemberRepository.getByUserName("testUser")).thenReturn(mockMember);
        Member member = userFacade.getMemberByUsername("testUser");
        assertNotNull(member);
        assertEquals("testUser", member.getUsername());
    }

    @Test
    public void testAddItemsToBasket() {
        when(mockUserRepository.getById(userId)).thenReturn(mockUser);
        doNothing().when(mockUser).addToCart(anyString(), anyInt(), anyString(), anyInt());
        doNothing().when(mockUser).updateCartPrice();

        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);
        verify(mockUser).addToCart(productName, quantity, storeId, totalPrice);
        verify(mockUser).updateCartPrice();
    }

    @Test
    public void testModifyBasketProduct() {
        when(mockUserRepository.getById(userId)).thenReturn(mockUser);
        doNothing().when(mockUser).modifyProductInCart(anyString(), anyInt(), anyString(), anyInt());
        doNothing().when(mockUser).updateCartPrice();

        userFacade.modifyBasketProduct(productName, quantity, storeId, userId, totalPrice);
        verify(mockUser).modifyProductInCart(productName, quantity, storeId, totalPrice);
        verify(mockUser).updateCartPrice();
    }

    @Test
    public void testCheckIfCanRemove() {
        when(mockUserRepository.getById(userId)).thenReturn(mockUser);
        when(mockUser.checkIfProductInUserCart(productName, storeId)).thenReturn(true);

        assertDoesNotThrow(() -> userFacade.checkIfCanRemove(productName, storeId, userId));
        verify(mockUser).checkIfProductInUserCart(productName, storeId);
    }

    @Test
    public void testRemoveItemFromUserCart() {
        when(mockUserRepository.getById(userId)).thenReturn(mockUser);
        doNothing().when(mockUser).removeItemFromUserCart(anyString(), anyString());

        userFacade.removeItemFromUserCart(productName, storeId, userId);
        verify(mockUser).removeItemFromUserCart(productName, storeId);
    }
}
