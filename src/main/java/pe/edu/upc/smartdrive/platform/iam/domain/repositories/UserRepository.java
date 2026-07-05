package pe.edu.upc.smartdrive.platform.iam.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.domain.model.valueobjects.Role;

import java.util.List;
import java.util.Optional;

/**
 * Persistence port for the {@link User} aggregate. Supports both login schemes
 * (admin by email, seller by username) and tenant-scoped seller queries.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<User> findByPublicId(String publicId);

    List<User> findByCompanyIdAndRole(Long companyId, Role role);
    long countByCompanyIdAndRole(Long companyId, Role role);
}
