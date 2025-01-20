package billing.billing_app.repository;

import billing.billing_app.model.BillingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreateAcctRepository extends JpaRepository<BillingEntity, Long> {

    Optional<BillingEntity> findByEmail(String email);
}
