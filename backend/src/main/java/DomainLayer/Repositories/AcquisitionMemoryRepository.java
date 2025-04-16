package DomainLayer.Repositories;

import DomainLayer.PaymentServices.Acquisition;
import DomainLayer.PaymentServices.ProductDetailReceipt;
import DomainLayer.PaymentServices.Receipt;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;

@Repository
@Profile("memory")
public class AcquisitionMemoryRepository implements AcquisitionRepository{

    private Map<String, Acquisition> IdAndAcquisition = new HashMap<>();
    private final Object acquisitionLock;

    public AcquisitionMemoryRepository(){
        acquisitionLock = new Object();
    }


    @Override
    public void flush() {

    }

    @Override
    public <S extends Acquisition> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Acquisition> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Acquisition> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Acquisition getOne(String s) {
        return null;
    }

    @Override
    public Acquisition getById(String s) {
        return null;
    }

    @Override
    public Acquisition getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends Acquisition> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Acquisition> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Acquisition> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Acquisition> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Acquisition> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Acquisition> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Acquisition, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Acquisition> S save(S entity) {
        synchronized (acquisitionLock) {
            IdAndAcquisition.put(entity.getAcquisitionId(), entity);
            return entity;
        }
    }


    @Override
    public <S extends Acquisition> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Acquisition> findById(String s) {
        synchronized (acquisitionLock) {
            return Optional.ofNullable(IdAndAcquisition.get(s));
        }
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Acquisition> findAll() {
        synchronized (acquisitionLock) {
            return new ArrayList<>(IdAndAcquisition.values());
        }
    }

    @Override
    public List<Acquisition> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(Acquisition entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Acquisition> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Acquisition> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Acquisition> findAll(Pageable pageable) {
        return null;
    }

//    @Override
//    public int getTotalPriceOfStoreReceipt(String storeId, String receiptId, String acquisitionId) {
//        Acquisition acquisition = IdAndAcquisition.get(acquisitionId);
//        int price = acquisition.getReceiptMap().get(storeId).getTotalPriceOfStoreReceipt();
//        return price;
//    }
//
//    @Override
//    public List<Object[]> getProductsAndPricesPerReceipt(String storeID, String receiptId, String acquisitionId) {
//        List<Object[]> productsAndPricesPerReceipt = new ArrayList<>();
//        Acquisition acquisition = IdAndAcquisition.get(acquisitionId);
//        List<ProductDetailReceipt> products = acquisition.getReceiptMap().get(storeID).getProductList();
//        for (ProductDetailReceipt productDetailReceipt : products) {
//            Object[] productAndPrice = new Object[2];
//            productAndPrice[0] = productDetailReceipt.getId().getProductName();
//            productAndPrice[1] = productDetailReceipt.getPrice();
//            productsAndPricesPerReceipt.add(productAndPrice);
//        }
//        return productsAndPricesPerReceipt;
//    }
//
//
////    @Override
////    public Map<String, Integer> getProductsAndAmountPerReceipt(String storeID, String receiptId, String acquisitionId) {
////        Map<String, Integer> productsAndAmountPerReceipt = new HashMap<>();
////        Acquisition acquisition = IdAndAcquisition.get(acquisitionId);
////        List<ProductDetailReceipt> products = acquisition.getReceiptMap().get(storeID).getProductList();
////        for (ProductDetailReceipt productDetailReceipt : products) {
////            productsAndAmountPerReceipt.put(productDetailReceipt.getId().getProductName(), productDetailReceipt.getAmount());
////        }
////        return productsAndAmountPerReceipt;
////    }
//
////    @Override
////    public List<ProductDetailReceipt> getProductDetailReceipt(String storeID, String receiptId, String acquisitionId) {
////        Acquisition acquisition = IdAndAcquisition.get(acquisitionId);
////        return acquisition.getReceiptMap().get(receiptId).getProductList();
////    }
//
//    @Override
//    public List<ProductDetailReceipt> getProductDetailReceipt(String receiptId, String acquisitionId) {
//        return List.of();
//    }
//
//    @Override
//    public List<Object[]> getProductsAndAmountPerReceipt(String storeId, String receiptId, String acquisitionId) {
//        List<ProductDetailReceipt> productDetailReceipts = getProductDetailReceipt(receiptId, acquisitionId);
//        List<Object[]> productsAndAmounts = new ArrayList<>();
//
//        for (ProductDetailReceipt productDetailReceipt : productDetailReceipts) {
//            Object[] productAndAmount = new Object[2];
//            productAndAmount[0] = productDetailReceipt.getId().getProductName();
//            productAndAmount[1] = productDetailReceipt.getAmount();
//            productsAndAmounts.add(productAndAmount);
//        }
//
//        return productsAndAmounts;
//    }
//
//    @Override
//    public List<Object[]> findProductsAndAmountsByStoreAndReceiptAndAcquisition(String storeId, String receiptId, String acquisitionId) {
//        return List.of();
//    }
//
//    @Override
//    public List<ProductDetailReceipt> findProductDetailReceiptsByReceiptAndAcquisition(String receiptId, String acquisitionId) {
//        return List.of();
//    }
//
//    @Override
//    public int findTotalPriceByStoreAndReceiptAndAcquisition(String storeId, String receiptId, String acquisitionId) {
//        return 0;
//    }

    @Override
    public List<Object[]> findProductsAndAmountsByStoreAndReceiptAndAcquisition(String storeId, String receiptId, String acquisitionId) {
        return List.of();
    }

    @Override
    public List<ProductDetailReceipt> findProductDetailReceiptsByReceiptAndAcquisition(String receiptId, String acquisitionId) {
        Acquisition acquisition = IdAndAcquisition.get(acquisitionId);
        List<ProductDetailReceipt> productDetailReceipts = acquisition.getProductDetailReceipts();
        List<ProductDetailReceipt> ans = new ArrayList<>();
        for (ProductDetailReceipt productDetailReceipt : productDetailReceipts) {
            if (productDetailReceipt.getId().getReceiptId().equals(receiptId)){
                ans.add(productDetailReceipt);
            }
        }
        return ans;
    }

    @Override
    public int findTotalPriceByReceipt(String receiptId) {
        int sum = 0;
        for (Acquisition acquisition : IdAndAcquisition.values()) {
            List<ProductDetailReceipt> productDetailReceipts = acquisition.getProductDetailReceipts();
            for (ProductDetailReceipt productDetailReceipt : productDetailReceipts) {
                if (productDetailReceipt.getId().getReceiptId().equals(receiptId)){
                    sum += productDetailReceipt.getPrice();
                }
            }
        }
        return sum;
    }

    @Override
    public List<Acquisition> findByStoreId(String storeId) {
        List<Acquisition> acquisitions = new ArrayList<>();
        for (Acquisition acquisition : IdAndAcquisition.values()) {
            Collection<Receipt> receipts = acquisition.getReceiptMap().values();
            for(Receipt receipt : receipts){
                if (Objects.equals(receipt.getStoreId(), storeId)){
                    acquisitions.add(acquisition);
                }
            }
        }
        return acquisitions;
    }

    @Override
    public List<String> getAllReceiptsByStoreId(String storeId) {
        List<Acquisition> acquisitions = IdAndAcquisition.values().stream().toList();
        List<String> receipts = new ArrayList<>();
        for (Acquisition acquisition : acquisitions) {
            List<ProductDetailReceipt> productDetailReceipts = acquisition.getProductDetailReceipts();
            for (ProductDetailReceipt productDetailReceipt : productDetailReceipts) {
                if (!receipts.contains(productDetailReceipt.getId().getReceiptId())){
                    receipts.add(productDetailReceipt.getId().getReceiptId());
                }
            }
        }
        return receipts;
    }

//    @Override
//    public List<Acquisition> findByUserId(String userID) {
//        List<Acquisition> acquisitions = new ArrayList<>();
//        for (Acquisition acquisition : IdAndAcquisition.values()) {
//            if(Objects.equals(acquisition.getUserId(), userID)){
//                acquisitions.add(acquisition);
//            }
//        }
//        return acquisitions;
//    }

    @Override
    public List<Acquisition> findByMemberId(String memberId) {
        List<Acquisition> acquisitions = new ArrayList<>();
        for (Acquisition acquisition : IdAndAcquisition.values()) {
            if(Objects.equals(acquisition.getMemberId(), memberId)){
                acquisitions.add(acquisition);
            }
        }
        return acquisitions;
    }

    @Override
    public List<String> getReceiptIdsByAcquisitionId(String AcquisitionId) {
        Acquisition acquisition = IdAndAcquisition.get(AcquisitionId);
        List<String> receiptIds = new ArrayList<>();
        for (Receipt receipt : acquisition.getReceiptMap().values()) {
            receiptIds.add(receipt.getReceiptId());
        }
        return receiptIds;
    }
}
