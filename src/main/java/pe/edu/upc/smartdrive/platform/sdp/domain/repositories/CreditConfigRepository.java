package pe.edu.upc.smartdrive.platform.sdp.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;

/** Spring Data repository for the {@link CreditConfig} aggregate. */
public interface CreditConfigRepository extends JpaRepository<CreditConfig, Long> {
}
