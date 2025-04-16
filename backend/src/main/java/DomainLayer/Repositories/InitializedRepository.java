package DomainLayer.Repositories;

import DomainLayer.Market.InitializedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface InitializedRepository extends JpaRepository<InitializedStatus, String> {
}
