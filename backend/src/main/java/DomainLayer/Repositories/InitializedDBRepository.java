package DomainLayer.Repositories;

import DomainLayer.Market.InitializedStatus;
import DomainLayer.Market.Market;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
@Profile("db")
public interface InitializedDBRepository extends InitializedRepository {
}
