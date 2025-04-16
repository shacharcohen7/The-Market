package DomainLayer.Repositories;

import DomainLayer.Role.StoreManager;
import DomainLayer.Role.SystemManager;
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
public class MemoryStoreManagerRepository implements StoreManagerRepository {

    private Map<String,List<StoreManager>> memberId_storeManagerMap = new HashMap<>();
    private final Object storeManagerLock= new Object();
    private Map<String, SystemManager> systemManagers = new HashMap<>();
    private final Object systemManagerLock = new Object();


    @Override
    public StoreManager get(String storeId, String memberId) {
        synchronized (storeManagerLock) {
            List<StoreManager> userManager = memberId_storeManagerMap.get(memberId);
            if (userManager==null){
                return null;
            }
            for (int i = 0; i < userManager.size(); i++) {
                StoreManager found = userManager.get(i);
                if (found.getStore_ID().equals(storeId)) {
                    return found;
                }
            }
        }
        return null;
    }

    @Override
    public StoreManager getStoreManager(String storeId, String memberId) {
        synchronized (storeManagerLock) {
            List<StoreManager> userManager = memberId_storeManagerMap.get(memberId);
            if (userManager==null){
                return null;
            }
            for (int i = 0; i < userManager.size(); i++) {
                StoreManager found = userManager.get(i);
                if (found.getStore_ID().equals(storeId) && !found.isInProposal()) {
                    return found;
                }
            }
        }
        return null;
    }

    @Override
    public StoreManager getStoreManagerNominator(String storeId, String memberId) {
        synchronized (storeManagerLock) {
            List<StoreManager> userManager = memberId_storeManagerMap.get(memberId);
            if (userManager==null){
                return null;
            }
            for (int i = 0; i < userManager.size(); i++) {
                StoreManager found = userManager.get(i);
                if (found.getStore_ID().equals(storeId) && found.isInProposal()) {
                    return found;
                }
            }
        }
        return null;
    }

    @Override
    public <S extends StoreManager> S save(S entity) {
        String memberId;
        synchronized (storeManagerLock) {
            memberId = entity.getMember_ID();
            if (memberId_storeManagerMap.get(memberId)==null) {
                memberId_storeManagerMap.put(memberId, new ArrayList<>());
            }
            memberId_storeManagerMap.get(memberId).add(entity);
        }
        return null;
    }

    @Override
    public void delete(StoreManager entity) {
        synchronized (storeManagerLock) {
            String memberId = entity.getMember_ID();
            if (memberId_storeManagerMap.get(memberId)!=null){
                memberId_storeManagerMap.remove(memberId);
            }
        }
    }

    @Override
    public List<String> getAllMemberId() {
        synchronized (storeManagerLock) {
            return memberId_storeManagerMap.keySet().stream().toList();
        }
    }

    @Override
    public List<StoreManager> getAllMemberIdManagers(String memberId) {
        List<StoreManager> userManagerActual = new ArrayList<>();
        synchronized (storeManagerLock) {
            List<StoreManager> userManager = memberId_storeManagerMap.get(memberId);
            if (userManager!=null) {
                for (StoreManager user : userManager) {
                    if (!user.isInProposal()) {
                        userManagerActual.add(user);
                    }

                }

            }
        }
        return userManagerActual;
    }

    @Override
    public List<StoreManager> getAllMemberIdNominatorsManagers(String memberId) {
        List<StoreManager> userManagerActual = new ArrayList<>();
        synchronized (storeManagerLock) {
            List<StoreManager> userManager = memberId_storeManagerMap.get(memberId);
            if (userManager != null) {

                for (StoreManager user : userManager) {
                    if (user.isInProposal()) {
                        userManagerActual.add(user);
                    }
                }

            }
        }
        return userManagerActual;
    }


    @Override
    public void updateStoreManagerPermissions(String memberId, String storeId, boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberID) {
        synchronized (storeManagerLock){
            StoreManager storeManager = get(storeId,memberId);
            storeManager.setPermissions(inventoryPermissions, purchasePermissions);

        }
    }

    @Override
    public void addSystemManager(String memberId) {
        synchronized (systemManagerLock){
            systemManagers.put(memberId, new SystemManager(memberId));
        }
    }

    @Override
    public String getSystemManager(String memberId) {
        synchronized (systemManagerLock){
            systemManagers.get(memberId);
        }
        return null;
    }

    @Override
    public List<String> getAllSystemManagers() {
        synchronized (systemManagerLock) {
            return systemManagers.keySet().stream().toList();
        }
    }

    @Override
    public void deleteAllSystemManager() {
        synchronized (systemManagerLock) {
            systemManagers.clear();
        }
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends StoreManager> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends StoreManager> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<StoreManager> entities) {
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public StoreManager getOne(String s) {
        return null;
    }

    @Override
    public StoreManager getById(String s) {
        return null;
    }

    @Override
    public StoreManager getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends StoreManager> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends StoreManager> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends StoreManager> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends StoreManager> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends StoreManager> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends StoreManager> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends StoreManager, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends StoreManager> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<StoreManager> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<StoreManager> findAll() {
        return List.of();
    }

    @Override
    public List<StoreManager> findAllById(Iterable<String> strings) {
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
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends StoreManager> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<StoreManager> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<StoreManager> findAll(Pageable pageable) {
        return null;
    }
}
