package pe.edu.upc.smartdrive.platform.iam.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.Company;

import java.util.Optional;

/**
 * Persistence port for the {@link Company} (tenant) aggregate.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    boolean existsByDomain(String domain);
    Optional<Company> findByDomain(String domain);
}
