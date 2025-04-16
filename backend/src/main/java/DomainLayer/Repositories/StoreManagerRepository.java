package DomainLayer.Repositories;

import DomainLayer.Role.StoreManager;
import DomainLayer.Role.SystemManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

@NoRepositoryBean
public interface StoreManagerRepository extends JpaRepository<StoreManager, String> {
    @Query("SELECT so FROM StoreManager so WHERE so.id.store_ID = :storeId AND so.id.member_ID = :memberId")
    StoreManager get(String storeId, String memberId);

    @Query("SELECT so FROM StoreManager so WHERE so.id.store_ID = :storeId AND so.id.member_ID = :memberId AND so.inProposal = false")
    StoreManager getStoreManager(String storeId, String memberId);

    //מועמד
    @Query("SELECT so FROM StoreManager so WHERE so.id.store_ID = :storeId AND so.id.member_ID = :memberId AND so.inProposal = true")
    StoreManager getStoreManagerNominator(String storeId, String memberId);

    @Query("SELECT so.id.member_ID FROM StoreManager so")
    List<String> getAllMemberId();

    @Query("SELECT so FROM StoreManager so WHERE so.id.member_ID = :memberId AND so.inProposal = false")
    List<StoreManager> getAllMemberIdManagers(String memberId);

    @Query("SELECT so FROM StoreManager so WHERE so.id.member_ID = :memberId AND so.inProposal = true")
    List<StoreManager> getAllMemberIdNominatorsManagers(String memberId);

    @Modifying
    @Query("UPDATE StoreManager sm SET sm.inventoryPermissions = :inventoryPermissions, sm.purchasePermissions = :purchasePermissions WHERE sm.id.store_ID = :storeId AND sm.id.member_ID = :memberId AND sm.nominatorMemberId = :nominatorMemberID")
    void updateStoreManagerPermissions(@Param("memberId") String memberId,
                                       @Param("storeId") String storeId,
                                       @Param("inventoryPermissions") boolean inventoryPermissions,
                                       @Param("purchasePermissions") boolean purchasePermissions,
                                       @Param("nominatorMemberID") String nominatorMemberID);

    @Modifying
    @Query(value = "INSERT INTO system_manager (member_id) VALUES (:member_id)", nativeQuery = true)
    void addSystemManager(@Param("member_id") String memberId);


    @Query(value = "SELECT sm.member_id FROM system_manager sm WHERE sm.member_id = :memberId", nativeQuery = true)
    String getSystemManager(@Param("memberId") String memberId);

    @Query(value = "SELECT sm.member_id FROM system_manager sm", nativeQuery = true)
    List<String> getAllSystemManagers();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM system_manager", nativeQuery = true)
    void deleteAllSystemManager();

}

