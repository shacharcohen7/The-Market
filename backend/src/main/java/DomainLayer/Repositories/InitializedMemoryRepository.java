package DomainLayer.Repositories;

import DomainLayer.Market.InitializedStatus;
import jakarta.inject.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
@Profile("memory")
public class InitializedMemoryRepository implements InitializedRepository{
    Map<String, InitializedStatus> map = new HashMap<>();

    @Override
    public void flush() {

    }

    @Override
    public <S extends InitializedStatus> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends InitializedStatus> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<InitializedStatus> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public InitializedStatus getOne(String s) {
        return null;
    }

    @Override
    public InitializedStatus getById(String s) {
        return null;
    }

    @Override
    public InitializedStatus getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends InitializedStatus> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends InitializedStatus> List<S> findAll(Example<S> example) {
        return List.of();
    }

    @Override
    public <S extends InitializedStatus> List<S> findAll(Example<S> example, Sort sort) {
        return List.of();
    }

    @Override
    public <S extends InitializedStatus> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends InitializedStatus> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends InitializedStatus> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends InitializedStatus, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }

    @Override
    public <S extends InitializedStatus> S save(S entity) {
        map.put(entity.getName(),entity);
        return entity;
    }

    @Override
    public <S extends InitializedStatus> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<InitializedStatus> findById(String s) {
        return Optional.ofNullable(map.get(s));
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<InitializedStatus> findAll() {
        return List.of();
    }

    @Override
    public List<InitializedStatus> findAllById(Iterable<String> strings) {
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
    public void delete(InitializedStatus entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends InitializedStatus> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<InitializedStatus> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<InitializedStatus> findAll(Pageable pageable) {
        return null;
    }
}
