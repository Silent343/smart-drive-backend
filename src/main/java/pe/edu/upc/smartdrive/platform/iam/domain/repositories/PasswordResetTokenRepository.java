package pe.edu.upc.smartdrive.platform.iam.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.PasswordResetToken;

import java.util.Optional;

/** Persistence port for {@link PasswordResetToken}. */
@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
