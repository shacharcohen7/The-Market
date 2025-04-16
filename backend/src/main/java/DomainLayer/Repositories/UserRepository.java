package DomainLayer.Repositories;

import DomainLayer.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

@NoRepositoryBean
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u.readyToPay FROM User u WHERE u.userID = :userId")
    boolean getReadyToPay(String userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE cart c SET c.cart_price = 0 WHERE c.cart_id = :cartId", nativeQuery = true)
    void updateCartPriceToZero(@Param("cartId") Long cartId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM basket b WHERE b.cart_id = :cartId" , nativeQuery = true)
    void deleteBasketsByCartId(@Param("cartId") Long cartId);
}