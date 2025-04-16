package DomainLayer.Repositories;

import DomainLayer.User.Member;
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
public class MemberMemoryRepository implements MemberRepository {

    Map<String, Member> allMembers;
    Object allMembersLock;

    public MemberMemoryRepository(){
        allMembers = new HashMap<>();
        allMembersLock = new Object();
    }

    @Override
    public void flush() {

    }


    @Override
    public <S extends Member> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends Member> List<S> saveAllAndFlush(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public void deleteAllInBatch(Iterable<Member> entities) {
        synchronized (allMembersLock) {
            allMembers.clear();
        }
    }

    @Override
    public void deleteAllByIdInBatch(Iterable<String> strings) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public Member getOne(String s) {
        return null;
    }

    @Override
    public Member getById(String id) {
        synchronized (allMembersLock) {
            return allMembers.get(id);
        }
    }

    @Override
    public Member getReferenceById(String s) {
        return null;
    }

    @Override
    public <S extends Member> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends Member> List<S> findAll(Example<S> example) {
        synchronized (allMembersLock) {
            return new ArrayList<S>((Collection<? extends S>) allMembers.values());
        }
    }

    @Override
    public <S extends Member> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends Member> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends Member> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends Member> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends Member, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }


    public void add(String userId , Member to_add) {
        synchronized (allMembersLock) {
            allMembers.put(userId, to_add);
        }
    }


    public void remove(String userId) {
        synchronized (allMembersLock) {

            allMembers.remove(userId);
        }
    }


    public List<Member> getAll() {
        synchronized (allMembersLock) {
            return new ArrayList<Member>(allMembers.values());
        }
    }


    public boolean contain(String memberId) {
        synchronized (allMembersLock) {
            return allMembers.containsKey(memberId);
        }
    }

    public void clear(){
        synchronized (allMembersLock) {
            allMembers.clear();
        }
    }

    @Override
    public Member getByUserName(String member) {
        synchronized (allMembersLock) {
            for (Member curr_member : allMembers.values()) {
                if (curr_member.getUsername().equals(member)) {
                    return curr_member;
                }
            }
        }
        return null;
    }

    @Override
    public Member getByUserId(String userId) {
        synchronized (allMembersLock) {
            for (Member curr_member : allMembers.values()) {
                if (curr_member.getUserId().equals(userId)) {
                    return curr_member;
                }
            }
        }
        return null;
    }

    @Override
    public <S extends Member> S save(S entity) {
        synchronized (allMembersLock) {
            allMembers.put(entity.getMember_ID(), entity);
        }
        return null;
    }

    @Override
    public <S extends Member> List<S> saveAll(Iterable<S> entities) {
        return List.of();
    }

    @Override
    public Optional<Member> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public List<Member> findAll() {
        synchronized (allMembersLock) {
            return new ArrayList<Member>(allMembers.values());
        }
    }

    @Override
    public List<Member> findAllById(Iterable<String> strings) {
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
    public void delete(Member entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends String> strings) {

    }

    @Override
    public void deleteAll(Iterable<? extends Member> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public List<Member> findAll(Sort sort) {
        return List.of();
    }

    @Override
    public Page<Member> findAll(Pageable pageable) {
        return null;
    }
}

