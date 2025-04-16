package DomainLayer.Repositories;

import DomainLayer.SupplyServices.ExternalSupplyService;
import Util.ShippingDTO;
import jakarta.transaction.Transactional;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

@NoRepositoryBean
public interface ExternalSupplyRepository extends JpaRepository<ExternalSupplyService,String> {
    @Query("SELECT s FROM ShippingDTO s WHERE s.memberId = :memberId")
    List<ShippingDTO> getUserHistory(@Param("memberId") String memberId);

    @Query(value = "SELECT * FROM shipping_dto", nativeQuery = true)
    List<ShippingDTO> getSystemHistory();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO shipping_dto (shipping_id, member_id, country, city, address, zip, date, acquisition_id, transaction_id) VALUES (:shippingId, :memberId, :country, :city, :address, :zip, :date, :acquisitionId, :transactionId)", nativeQuery = true)
    void addShipping(@Param("shippingId") String shippingId,
                     @Param("memberId") String memberId,
                     @Param("country") String country,
                     @Param("city") String city,
                     @Param("address") String address,
                     @Param("zip") String zip,
                     @Param("date") Date date,
                     @Param("acquisitionId") String acquisitionId,
                     @Param("transactionId") int transactionId);
}
