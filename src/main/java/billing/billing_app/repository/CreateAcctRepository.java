package billing.billing_app.repository;

import billing.billing_app.model.BillingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CreateAcctRepository extends JpaRepository<BillingEntity, Long> {

    @Query("SELECT b FROM BillingEntity b WHERE b.user.email = :email")
    Optional<BillingEntity> findByEmail(@Param("email") String email);

}
