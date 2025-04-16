package DomainLayer.Repositories;

import java.util.*;
import java.util.function.Function;

import DomainLayer.User.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;


@Repository
@Profile("memory")
public class UserMemoryRepository implements UserRepository{

    Map<String, User> allUsers;
    Object allUsersLock;

    public UserMemoryRepository(){
        allUsers = new HashMap<>();
        allUsersLock = new Object();
    }

    @Override
    public void deleteById(String s) {
        synchronized (allUsersLock) {
            allUsers.remove(s);
        }
    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example) {
        List<S> getAllUsers;
        synchronized (allUsersLock) {
            getAllUsers = new ArrayList<S>((Collection<? extends S>) allUsers.values());
        }
        return getAllUsers;
    }

    @Override
    public User getById(String s) {
        synchronized (allUsersLock) {
            return allUsers.get(s);
        }
    }

    @Override
    public Optional<User> findById(String s) {
        synchronized (allUsersLock) {
            return Optional.ofNullable(allUsers.get(s));
        }
    }

    @Override
    public boolean existsById(String s) {
        synchronized (allUsersLock) {
            return allUsers.containsKey(allUsers);
        }
    }

    @Override
    public <S extends User> S save(S entity) {
        synchronized (allUsersLock){
            allUsers.put(entity.getUserID(), entity);
        }
        return null;
    }


    @Override
    public void flush() {

    }

    @Override
    public <S extends User> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends User> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<User> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {
        synchronized (allUsersLock) {
            allUsers.clear();
        }
    }

    @Override
    public User getOne(String s) {
        return null;
    }

    @Override
    public User getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends User> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends User> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends User> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends User> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends User> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends User, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public List<User> findAllById(Iterable<String> strings) {
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
    public void deleteAll(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<User> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public boolean getReadyToPay(String userId) {
        return allUsers.get(userId).isReadyToPay();
    }

    @Override
    public void updateCartPriceToZero(Long cartId) {

    }

    @Override
    public void deleteBasketsByCartId(Long cartId) {
        //(userId).emptyCart();
    }
}
