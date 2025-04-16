package DomainLayer.Repositories;

import DomainLayer.Store.Store;
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
public class StoreMemoryRepository implements StoreRepository{

    private Map<String, Store> allStores ;
    private final Object storesLock;


    public StoreMemoryRepository(){
        allStores = new HashMap<String, Store>();
        storesLock = new Object();

    }

    @Override
    public List<String> getAllIds() {
        synchronized (storesLock) {
            return new ArrayList<>(allStores.keySet());
        }
    }

    /*
    @Override
    public void addProductToStore(String storeId, String productName, int price, int quantity, String category, String description) {

    }
*/
    @Override
    public void flush() {

    }

    @Override
    public <S extends Store> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Store> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Store> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Store getOne(String s) {
        return null;
    }

    @Override
    public Store getById(String s) {
        synchronized (storesLock) {
            return allStores.get(s);
        }
    }

    @Override
    public Store getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends Store> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Store> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends Store> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends Store> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Store> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Store> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Store, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends Store> S save(S entity) {
        synchronized (storesLock){
            allStores.put(entity.getStoreID(), entity);
        }
        return entity;
    }

    @Override
    public <S extends Store> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Store> findById(String s) {
        synchronized (storesLock) {
            return Optional.ofNullable(allStores.get(s));
        }
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Store> findAll() {
        synchronized (storesLock) {
            return new ArrayList<Store>(allStores.values());
        }
    }

    @Override
    public List<Store> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {
        synchronized (storesLock) {
            allStores.remove(s);
        }
    }

    @Override
    public void delete(Store entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Store> entities) {

    }

    @Override
    public void deleteAll() {
        synchronized (storesLock) {
            allStores.clear();
        }
    }

    @Override
    public List<Store> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Store> findAll(Pageable pageable) {
        return null;
    }
}
