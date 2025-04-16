package DomainLayer.Repositories;

import DomainLayer.PaymentServices.Acquisition;
import DomainLayer.PaymentServices.ExternalPaymentService;
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
public class ExternalPaymentMemoryRepository implements ExternalPaymentRepository {

    private Map<String, ExternalPaymentService> allPaymentServices = new HashMap<String, ExternalPaymentService>();


    private final Object paymentServiceLock;

    public ExternalPaymentMemoryRepository() {
        paymentServiceLock = new Object();
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends ExternalPaymentService> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends ExternalPaymentService> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<ExternalPaymentService> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ExternalPaymentService getOne(String s) {
        return null;
    }

    @Override
    public ExternalPaymentService getById(String s) {
        return null;
    }

    @Override
    public ExternalPaymentService getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends ExternalPaymentService> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ExternalPaymentService> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends ExternalPaymentService> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends ExternalPaymentService> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ExternalPaymentService> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ExternalPaymentService> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ExternalPaymentService, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends ExternalPaymentService> S save(S entity) {
        synchronized (paymentServiceLock) {
            allPaymentServices.put(entity.getUrl(), entity);
            return entity;
        }
    }

    @Override
    public <S extends ExternalPaymentService> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<ExternalPaymentService> findById(String id) {
        synchronized (paymentServiceLock) {
            return Optional.ofNullable(allPaymentServices.get(id));
        }
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<ExternalPaymentService> findAll() {
        synchronized (paymentServiceLock) {
            return new ArrayList<>(allPaymentServices.values());
        }
    }

    @Override
    public List<ExternalPaymentService> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {
        synchronized (paymentServiceLock) {
            allPaymentServices.remove(s);
        }
    }

    @Override
    public void delete(ExternalPaymentService entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends ExternalPaymentService> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<ExternalPaymentService> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<ExternalPaymentService> findAll(Pageable pageable) {
        return null;
    }
}
