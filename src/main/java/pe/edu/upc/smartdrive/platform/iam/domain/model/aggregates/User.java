package pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.RegisterSellerCommand;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.smartdrive.platform.iam.domain.model.valueobjects.Role;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import pe.edu.upc.smartdrive.platform.shared.infrastructure.persistence.jpa.crypto.EncryptedStringConverter;

import java.util.UUID;

/**
 * User aggregate root for the IAM bounded context.
 *
 * <p>A user belongs to exactly one {@link Company} (tenant) and has a {@link Role}:
 * <ul>
 *   <li>{@code ADMIN} signs in with {@code email} + password (created during company sign-up);</li>
 *   <li>{@code SELLER} signs in with {@code username} + password (created by an admin, with a
 *       system-generated initial password).</li>
 * </ul>
 * The {@link #loginHandle()} returns whichever identifier applies, and is what the JWT is
 * issued for. PII such as {@code dni} is stored encrypted at rest via {@link EncryptedStringConverter}.</p>
 *
 * <p>TOTP follows a two-phase enrolment: a candidate secret is stored in
 * {@code totpSecretPending} until the user confirms a valid code, at which point it is promoted
 * to {@code totpSecret} and {@code totpEnabled} becomes true.</p>
 */
@Entity
@Table(name = "users")
public class User extends AuditableAbstractAggregateRoot<User> {

    /**
     * Publicly exposed identifier (UUID v4). Random and non-identifying, so it is stored in
     * clear text: it must remain SQL-searchable (the TOTP flow looks users up by it) and it
     * reveals nothing about the person. The PII that must be protected is {@code dni}, which
     * is encrypted below.
     */
    @Column(nullable = false, unique = true, updatable = false, length = 36)
    private String publicId;

    /** Tenant this user belongs to. */
    @Column(nullable = false)
    private Long companyId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 16)
    private Role role;

    /** Admin login identifier. Null for sellers. Unique when present. */
    @Column(unique = true)
    private String email;

    /** Seller login identifier ({domain}-{code}). Null for admins. Unique when present. */
    @Column(unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;
    private String firstName;
    private String lastName;

    /** Seller short code assigned by the admin (e.g. "V001"). Null for admins. */
    private String code;

    /** Encrypted at rest. */
    @Convert(converter = EncryptedStringConverter.class)
    @Column(length = 512)
    private String dni;

    private String ruc;
    private String phone;
    private String businessName;

    private String totpSecret;
    private String totpSecretPending;

    @Column(nullable = false)
    private boolean totpEnabled;

    protected User() {
        this.publicId = UUID.randomUUID().toString();
        this.totpEnabled = false;
    }

    /**
     * Creates the ADMIN user of a freshly registered company. The password must already be hashed.
     */
    public User(SignUpCommand command, Long companyId, String hashedPassword) {
        this();
        this.companyId = companyId;
        this.role = Role.ADMIN;
        this.email = command.email().toLowerCase();
        this.password = hashedPassword;
        this.fullName = command.fullName();
        this.dni = command.dni();
        this.ruc = command.ruc();
        this.phone = command.phone();
        this.businessName = command.businessName();
    }

    /**
     * Creates a SELLER user under a company. The {@code username} is derived from the company
     * domain and seller code; the password (system-generated) must already be hashed.
     */
    public User(RegisterSellerCommand command, Long companyId, String username, String hashedPassword) {
        this();
        this.companyId = companyId;
        this.role = Role.SELLER;
        this.username = username;
        this.password = hashedPassword;
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.fullName = (command.firstName() + " " + command.lastName()).trim();
        this.code = command.code();
        this.dni = command.dni();
        this.phone = command.phone();
    }

    /** The identifier this user signs in with and that the JWT is issued for. */
    public String loginHandle() {
        return role == Role.ADMIN ? email : username;
    }

    /** Overwrites the password with an already-hashed value (used by password reset / seller creation). */
    public void changePassword(String newHashedPassword) {
        this.password = newHashedPassword;
    }

    public void startTotpEnrolment(String secret) {
        this.totpSecretPending = secret;
    }

    public void confirmTotpEnrolment() {
        if (this.totpSecretPending == null)
            throw new IllegalStateException("There is no pending TOTP setup to confirm");
        this.totpSecret = this.totpSecretPending;
        this.totpSecretPending = null;
        this.totpEnabled = true;
    }

    public void enableTotpWithSecret(String secret) {
        this.totpSecret = secret;
        this.totpSecretPending = null;
        this.totpEnabled = true;
    }

    public boolean isAdmin() { return role == Role.ADMIN; }
    public boolean isSeller() { return role == Role.SELLER; }

    public String getPublicId() { return publicId; }
    public Long getCompanyId() { return companyId; }
    public Role getRole() { return role; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCode() { return code; }
    public String getDni() { return dni; }
    public String getRuc() { return ruc; }
    public String getPhone() { return phone; }
    public String getBusinessName() { return businessName; }
    public String getTotpSecret() { return totpSecret; }
    public String getTotpSecretPending() { return totpSecretPending; }
    public boolean isTotpEnabled() { return totpEnabled; }
}
