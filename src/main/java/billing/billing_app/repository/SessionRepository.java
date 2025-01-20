package billing.billing_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import billing.billing_app.model.Session;

public interface SessionRepository extends JpaRepository<Session, Long> {

}
