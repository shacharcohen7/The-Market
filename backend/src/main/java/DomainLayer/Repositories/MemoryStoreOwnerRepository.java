package DomainLayer.Repositories;

import DomainLayer.Role.StoreOwner;
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
public class MemoryStoreOwnerRepository implements StoreOwnerRepository {
    private Map<String,List<StoreOwner>> memberId_storeOwnersMap = new HashMap<>();
    private final Object storeOwnerLock= new Object();


    @Override
    public StoreOwner get(String storeId, String memberId) {
        synchronized (storeOwnerLock) {
            List<StoreOwner> userOwner = memberId_storeOwnersMap.get(memberId);
            if (userOwner==null){
                return null;
            }
            for (int i = 0; i < userOwner.size(); i++) {
                StoreOwner found = userOwner.get(i);
                if (found.getStore_ID().equals(storeId)) {
                    return found;
                }
            }
        }
        return null;
    }



    public StoreOwner getStoreOwnerImpl(String memberId, String storeId){
        if (memberId_storeOwnersMap.get(memberId)!=null){
            for (StoreOwner storeOwner: memberId_storeOwnersMap.get(memberId)){
                if (storeOwner.getStore_ID().equals(storeId)){
                    return storeOwner;
                }
            }
        }
        return null;
    }

    @Override
    public StoreOwner getStoreOwner(String storeId, String memberID) {
        synchronized (storeOwnerLock) {
            List<StoreOwner> userOwner = memberId_storeOwnersMap.get(memberID);
            if (userOwner==null){
                return null;
            }
            for (int i = 0; i < userOwner.size(); i++) {
                StoreOwner found = userOwner.get(i);
                if (found.getStore_ID().equals(storeId) && !found.isInProposal()) {
                    return found;
                }
            }
        }
        return null;
    }

    @Override
    public StoreOwner getStoreOwnerNominator(String storeId, String memberId) {
        synchronized (storeOwnerLock) {
            List<StoreOwner> userOwner = memberId_storeOwnersMap.get(memberId);
            if (userOwner==null){
                return null;
            }
            for (int i = 0; i < userOwner.size(); i++) {
                StoreOwner found = userOwner.get(i);
                if (found.getStore_ID().equals(storeId) && found.isInProposal()) {
                    return found;
                }
            }
        }
        return null;
    }

    /*
    @Override
    public void add(StoreOwner storeOwner) {
        synchronized (storeOwnerLock) {
            String memberId = storeOwner.getMember_ID();
            if (memberId_storeOwnersMap.get(memberId)==null){
                memberId_storeOwnersMap.put(memberId, new ArrayList<>());
            }
            memberId_storeOwnersMap.get(memberId).add(storeOwner);
        }
    }*/

    @Override
    public <S extends StoreOwner> S save(S entity) {
        synchronized (storeOwnerLock){
            String memberId = entity.getMember_ID();
            if (memberId_storeOwnersMap.get(memberId)==null){
                memberId_storeOwnersMap.put(memberId, new ArrayList<>());
            }
            memberId_storeOwnersMap.get(memberId).add(entity);
        }
        return null;
    }


    @Override
    public List<String> getAllMemberId() {
        synchronized (storeOwnerLock) {
            return memberId_storeOwnersMap.keySet().stream().toList();
        }
    }

    @Override
    public List<StoreOwner> getAllMemberIdOwners(String memberId) {
        List<StoreOwner> userOwnerActual = new ArrayList<>();
        synchronized (storeOwnerLock) {
            List<StoreOwner> userOwner = memberId_storeOwnersMap.get(memberId);
            if (userOwner!=null) {
                for (StoreOwner user : userOwner) {
                    if (!user.isInProposal()) {
                        userOwnerActual.add(user);
                    }
                }
            }
            return userOwnerActual;
        }
    }

    @Override
    public List<StoreOwner> getAllMemberIdNominatorsOwners(String memberId) {
        List<StoreOwner> userOwnerActual = new ArrayList<>();
        synchronized (storeOwnerLock) {
            List<StoreOwner> userOwner = memberId_storeOwnersMap.get(memberId);
            if (userOwner!=null) {
                for (StoreOwner user : userOwner) {
                    if (user.isInProposal()) {
                        userOwnerActual.add(user);
                    }
                }
            }
            return userOwnerActual;
        }
    }

    @Override
    public void insertStoreOwner(String memberId, String storeId, boolean founder, String nominatorId, boolean inProposal) {

        if (memberId_storeOwnersMap.get(memberId)==null){
            memberId_storeOwnersMap.put(memberId, new ArrayList<>());
        }
        memberId_storeOwnersMap.get(memberId).add(new StoreOwner(memberId,storeId,founder,nominatorId,inProposal));
    }


    @Override
    public List<StoreOwner> findAllById(Iterable<String> strings) {
        return List.of();
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(StoreOwner entity) {
        synchronized (storeOwnerLock) {
            String memberId = entity.getMember_ID();
            if (memberId_storeOwnersMap.get(memberId)!=null){
                memberId_storeOwnersMap.remove(memberId);
            }
        }
    }

    @Override
    public <S extends StoreOwner> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public StoreOwner getById(String s) {
        return null;
    }

    @Override
    public Optional<StoreOwner> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends StoreOwner> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends StoreOwner> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<StoreOwner> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {
    }

    @Override
    public StoreOwner getOne(String s) {
        return null;
    }

    @Override
    public StoreOwner getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends StoreOwner> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends StoreOwner> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends StoreOwner> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends StoreOwner> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends StoreOwner> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends StoreOwner, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends StoreOwner> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<StoreOwner> findAll() {
        return List.of();
    }


    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends StoreOwner> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<StoreOwner> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<StoreOwner> findAll(Pageable pageable) {
        return null;
}
}
