package DomainLayer.Repositories;

import DomainLayer.User.Member;
import DomainLayer.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface MemberRepository extends JpaRepository<Member,String> {

    @Query("SELECT m FROM Member m WHERE m.username = ?1")
    Member getByUserName(String member);

    @Query("SELECT m FROM Member m WHERE m.userId = ?1")
    Member getByUserId(String userId);
}
