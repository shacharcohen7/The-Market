package DomainLayer.Repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("db")
public interface UserDBRepository extends UserRepository {
    // Additional methods specific to database interactions can be added here
}