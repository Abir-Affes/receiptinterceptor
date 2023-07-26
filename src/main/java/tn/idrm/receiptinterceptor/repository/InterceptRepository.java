package tn.idrm.receiptinterceptor.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tn.idrm.receiptinterceptor.domain.Intercept;

/**
 * Spring Data JPA repository for the Intercept entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterceptRepository extends JpaRepository<Intercept, Long> {}
