package pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.UUID;

/**
 * User aggregate root for the IAM bounded context.
 *
 * <p>Holds the administrator profile, hashed credentials and the TOTP (two-factor
 * authentication) state. TOTP follows a two-phase enrolment: a candidate secret is
 * stored in {@code totpSecretPending} until the user confirms a valid code, at which
 * point it is promoted to {@code totpSecret} and {@code totpEnabled} becomes true.</p>
 */
@Entity
@Table(name = "users")
public class User extends AuditableAbstractAggregateRoot<User> {

    /** Publicly exposed identifier (UUID v4). Never sequential, safe to show in the UI. */
    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String publicId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String fullName;
    private String dni;
    private String ruc;
    private String phone;
    private String businessName;

    @Column(nullable = false)
    private String role;

    private String totpSecret;
    private String totpSecretPending;

    @Column(nullable = false)
    private boolean totpEnabled;

    protected User() {
        this.publicId = UUID.randomUUID().toString();
        this.role = "Operador";
        this.totpEnabled = false;
    }

    /**
     * Creates a user from a sign-up command. The password must already be hashed.
     *
     * @param command        the sign-up command
     * @param hashedPassword the encoded password
     */
    public User(SignUpCommand command, String hashedPassword) {
        this();
        this.email = command.email().toLowerCase();
        this.password = hashedPassword;
        this.fullName = command.fullName();
        this.dni = command.dni();
        this.ruc = command.ruc();
        this.phone = command.phone();
        this.businessName = command.businessName();
    }

    /** Stores a candidate TOTP secret awaiting confirmation. */
    public void startTotpEnrolment(String secret) {
        this.totpSecretPending = secret;
    }

    /** Promotes the pending TOTP secret to active, enabling two-factor authentication. */
    public void confirmTotpEnrolment() {
        if (this.totpSecretPending == null)
            throw new IllegalStateException("There is no pending TOTP setup to confirm");
        this.totpSecret = this.totpSecretPending;
        this.totpSecretPending = null;
        this.totpEnabled = true;
    }

    /** Enables TOTP directly with a known secret (used by the demo seeding routine). */
    public void enableTotpWithSecret(String secret) {
        this.totpSecret = secret;
        this.totpSecretPending = null;
        this.totpEnabled = true;
    }

    public String getPublicId() { return publicId; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getDni() { return dni; }
    public String getRuc() { return ruc; }
    public String getPhone() { return phone; }
    public String getBusinessName() { return businessName; }
    public String getRole() { return role; }
    public String getTotpSecret() { return totpSecret; }
    public String getTotpSecretPending() { return totpSecretPending; }
    public boolean isTotpEnabled() { return totpEnabled; }
}
