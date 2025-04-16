package DomainLayer.Repositories;

import DomainLayer.PaymentServices.Acquisition;
import DomainLayer.PaymentServices.ExternalPaymentService;
import DomainLayer.User.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExternalPaymentRepository extends JpaRepository<ExternalPaymentService,String> {
}
