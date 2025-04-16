package DomainLayer.Repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("db")
public interface DBStoreManagerRepository extends StoreManagerRepository {
}
