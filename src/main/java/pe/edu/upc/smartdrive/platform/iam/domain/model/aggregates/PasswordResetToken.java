package pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.Instant;
import java.util.UUID;

/**
 * Single-use, time-limited token that authorizes a password reset for a given user.
 *
 * <p>Created when a user requests a reset; consumed (marked used) when the new password is
 * set. Expiry defaults to one hour. The raw token value is a UUID and is the only thing the
 * caller needs to present to complete the reset.</p>
 */
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken extends AuditableAbstractAggregateRoot<PasswordResetToken> {

    @Column(nullable = false, unique = true, updatable = false)
    private String token;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Instant expiresAt;

    @Column(nullable = false)
    private boolean used;

    protected PasswordResetToken() { }

    public PasswordResetToken(Long userId, long ttlMinutes) {
        this.token = UUID.randomUUID().toString();
        this.userId = userId;
        this.expiresAt = Instant.now().plusSeconds(ttlMinutes * 60);
        this.used = false;
    }

    /** True when the token is neither used nor past its expiry. */
    public boolean isValidNow() {
        return !used && Instant.now().isBefore(expiresAt);
    }

    public void markUsed() {
        this.used = true;
    }

    public String getToken() { return token; }
    public Long getUserId() { return userId; }
    public Instant getExpiresAt() { return expiresAt; }
    public boolean isUsed() { return used; }
}
