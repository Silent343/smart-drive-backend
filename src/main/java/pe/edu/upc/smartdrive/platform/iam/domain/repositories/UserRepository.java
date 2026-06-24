package pe.edu.upc.smartdrive.platform.iam.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;

import java.util.Optional;

/**
 * Persistence port for the {@link User} aggregate.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByPublicId(String publicId);
}
