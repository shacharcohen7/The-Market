package DomainLayer.Repositories;

import DomainLayer.SupplyServices.ExternalSupplyService;
import Util.ShippingDTO;
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
public class ExternalSupplyMemoryRepository implements ExternalSupplyRepository {

    private Map<String, ExternalSupplyService> externalSupplyService;
    private final Object externalSupplyServiceLock;
    private final Object usersShippingHistoryLock;
    private Map<String, List<ShippingDTO>>  usersShippingHistory; //<userName,List<ShippindDTO>>

    public ExternalSupplyMemoryRepository() {
        this.externalSupplyService = new HashMap<>();
        this.externalSupplyServiceLock = new Object();
        this.usersShippingHistory = new HashMap<>();
        this.usersShippingHistoryLock = new Object();
    }


    @Override
    public void flush() {

    }

    @Override
    public <S extends ExternalSupplyService> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends ExternalSupplyService> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<ExternalSupplyService> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public ExternalSupplyService getOne(String s) {
        return null;
    }

    @Override
    public ExternalSupplyService getById(String s) {
        return null;
    }

    @Override
    public ExternalSupplyService getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends ExternalSupplyService> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends ExternalSupplyService> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends ExternalSupplyService> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends ExternalSupplyService> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends ExternalSupplyService> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends ExternalSupplyService> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends ExternalSupplyService, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends ExternalSupplyService> S save(S entity) {
        synchronized (externalSupplyServiceLock) {
            externalSupplyService.put(entity.getSupplyURL(), entity);
        }
        return entity;
    }

    @Override
    public <S extends ExternalSupplyService> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<ExternalSupplyService> findById(String s) {
        synchronized (externalSupplyServiceLock) {
            return Optional.ofNullable(externalSupplyService.get(s));
        }
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<ExternalSupplyService> findAll() {
        List<ExternalSupplyService> services;
        synchronized (externalSupplyServiceLock) {
            services = new ArrayList<>(externalSupplyService.values());
        }
        return services;
    }


    @Override
    public List<ExternalSupplyService> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {
        synchronized (externalSupplyServiceLock) {
            externalSupplyService.remove(s);
        }
    }

    @Override
    public void delete(ExternalSupplyService entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends ExternalSupplyService> entities) {

    }

    @Override
    public void deleteAll() {
        synchronized (externalSupplyServiceLock) {
            externalSupplyService.clear();
        }
    }

    @Override
    public List<ExternalSupplyService> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<ExternalSupplyService> findAll(Pageable pageable) {
        return null;
    }


    @Override
    public List<ShippingDTO> getUserHistory(String memberId) {
        synchronized (usersShippingHistoryLock){
            return usersShippingHistory.get(memberId);
        }
    }

    @Override
    public List<ShippingDTO> getSystemHistory() {
        List<ShippingDTO> shippingDTOS= new ArrayList<>();
        synchronized (usersShippingHistoryLock){
            for (List<ShippingDTO> shippingDTO: usersShippingHistory.values()){
                shippingDTOS.addAll(shippingDTO);
            }
        }
        return shippingDTOS;
    }

    @Override
    public void addShipping(String shippingId, String memberId, String country, String city, String address, String zip, Date date, String acquisitionId, int transactionId) {
        ShippingDTO shippingDTO = new ShippingDTO(shippingId,transactionId,memberId,country,city,address,acquisitionId);
        synchronized (usersShippingHistoryLock){
            if (getUserHistory(shippingDTO.getMemberId())==null){
                usersShippingHistory.put(shippingDTO.getMemberId(), new ArrayList<>());
            }
            usersShippingHistory.get(shippingDTO.getMemberId()).add(shippingDTO);
        }
    }
}
