package DomainLayer.Repositories;

import DomainLayer.Store.Store;
import DomainLayer.User.User;
//import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface StoreRepository extends JpaRepository<Store, String> {

    @Query("SELECT s.store_ID FROM Store s")
    List<String> getAllIds();

    /*
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO product (product_name, price, quantity, category, description, rating, num_of_ratings, store_id) VALUES (?2, ?3, ?4, ?5, ?6, 0, 0, ?1)", nativeQuery = true)
    void addProductToStore(String storeId, String productName, int price, int quantity, String category, String description);
     */
}
