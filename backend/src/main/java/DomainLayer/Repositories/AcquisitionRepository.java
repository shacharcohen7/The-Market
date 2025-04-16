package DomainLayer.Repositories;

import DomainLayer.PaymentServices.Acquisition;
import DomainLayer.PaymentServices.ProductDetailReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


@NoRepositoryBean
public interface AcquisitionRepository extends JpaRepository<Acquisition,String> {

    @Query("SELECT p.productDetailReceiptId.productName, p.amount FROM ProductDetailReceipt p WHERE p.productDetailReceiptId.receiptId = :receiptId AND p.productDetailReceiptId.storeId = :storeId AND p.acquisition.acquisitionId = :acquisitionId")
    List<Object[]> findProductsAndAmountsByStoreAndReceiptAndAcquisition(@Param("storeId") String storeId, @Param("receiptId") String receiptId, @Param("acquisitionId") String acquisitionId);

    @Query("SELECT p FROM ProductDetailReceipt p WHERE p.productDetailReceiptId.receiptId = :receiptId AND p.acquisition.acquisitionId = :acquisitionId")
    List<ProductDetailReceipt> findProductDetailReceiptsByReceiptAndAcquisition(@Param("receiptId") String receiptId, @Param("acquisitionId") String acquisitionId);

    @Query("SELECT SUM(p.price) FROM ProductDetailReceipt p WHERE p.productDetailReceiptId.receiptId = :receiptId")
    int findTotalPriceByReceipt(@Param("receiptId") String receiptId);

    @Query("SELECT a FROM Acquisition a JOIN a.productDetailReceipts p WHERE p.productDetailReceiptId.storeId = :storeId")
    List<Acquisition> findByStoreId(String storeId);

    @Query("SELECT DISTINCT p.productDetailReceiptId.receiptId FROM ProductDetailReceipt p WHERE p.productDetailReceiptId.storeId = :storeId")
    List<String> getAllReceiptsByStoreId(String storeId);

//    @Query("SELECT a FROM Acquisition a WHERE a.userId = :userID")
//    List<Acquisition> findByUserId(String userID);

    @Query("SELECT a FROM Acquisition a WHERE a.memberId = :memberId")
    List<Acquisition> findByMemberId(String memberId);





//    @Query("SELECT new Util.ProductFromReceiptDTO(p.productDetailReceiptId.receiptId, p.productDetailReceiptId.storeId, a.userId, p.productDetailReceiptId.productName, p.amount, p.price) " +
//            "FROM Acquisition a JOIN a.productDetailReceipts p " +
//            "WHERE a.acquisitionId = :acquisitionId")
//    List<ProductFromReceiptDTO> getReceiptsByAcquisitionId(@Param("acquisitionId") String acquisitionId);

    @Query("SELECT p.productDetailReceiptId.receiptId FROM ProductDetailReceipt p WHERE p.acquisition.acquisitionId = :AcquisitionId")
    List<String> getReceiptIdsByAcquisitionId(String AcquisitionId);



}
