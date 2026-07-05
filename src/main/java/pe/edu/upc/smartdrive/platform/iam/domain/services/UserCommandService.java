package pe.edu.upc.smartdrive.platform.iam.domain.services;

import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.*;
import pe.edu.upc.smartdrive.platform.iam.domain.model.valueobjects.TotpEnrolment;

import java.util.Optional;

/**
 * Command side of the IAM bounded context: company/admin registration, seller
 * registration, authentication and TOTP.
 */
public interface UserCommandService {

    /**
     * Registers a new company together with its administrator account.
     *
     * @param command the sign-up command (admin + company data)
     * @return the created admin user
     */
    Optional<User> handle(SignUpCommand command);

    /**
     * Registers a seller under a company (admin action). Generates the seller's username
     * and an initial password.
     *
     * @param command the seller registration command
     * @return the created seller together with the plaintext initial password (shown once)
     */
    SellerRegistration handle(RegisterSellerCommand command);

    /**
     * Validates identifier (email or username) + password credentials.
     *
     * @param command the sign-in command
     * @return the matching user when credentials are valid
     */
    Optional<User> handle(SignInCommand command);

    TotpEnrolment handle(SetupTotpCommand command);

    boolean handle(ConfirmTotpSetupCommand command);

    Optional<User> handle(VerifyTotpCommand command);

    /**
     * Starts a password reset: creates a single-use, time-limited token for the account.
     *
     * @param command the forgot-password command (email or username)
     * @return the reset token when the account exists; empty otherwise (callers should not
     *         reveal which case occurred, to avoid account enumeration)
     */
    Optional<String> handle(ForgotPasswordCommand command);

    /**
     * Completes a password reset: validates the token and sets the new (policy-checked) password.
     *
     * @param command the reset command (token + new password)
     * @return true when the password was changed; false when the token is invalid/expired/used
     */
    boolean handle(ResetPasswordCommand command);

    /** Issues a signed bearer token for an already-authenticated user. */
    String issueTokenFor(User user);

    /** Resolves the internal {@code Long} primary key from a public UUID. */
    Optional<Long> resolveInternalId(String publicId);

    /** Resolves the company domain (slug) for a user, used to build the auth response. */
    String resolveCompanyDomain(User user);

    /** Result of a seller registration: the created user plus the one-time plaintext password. */
    record SellerRegistration(User seller, String initialPassword, String username) { }
}
