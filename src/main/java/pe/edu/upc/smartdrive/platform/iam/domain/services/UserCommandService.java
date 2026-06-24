package pe.edu.upc.smartdrive.platform.iam.domain.services;

import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.*;
import pe.edu.upc.smartdrive.platform.iam.domain.model.valueobjects.TotpEnrolment;

import java.util.Optional;

/**
 * Command side of the IAM bounded context: registration, authentication and TOTP.
 */
public interface UserCommandService {

    /**
     * Registers a new user.
     *
     * @param command the sign-up command
     * @return the created user, or empty if the email is already taken
     */
    Optional<User> handle(SignUpCommand command);

    /**
     * Validates email/password credentials.
     *
     * @param command the sign-in command
     * @return the matching user when credentials are valid
     */
    Optional<User> handle(SignInCommand command);

    /**
     * Starts TOTP enrolment for a user, producing a secret and a QR code.
     *
     * @param command the setup command
     * @return the enrolment data to display to the user
     */
    TotpEnrolment handle(SetupTotpCommand command);

    /**
     * Confirms TOTP enrolment by validating the first code, enabling 2FA.
     *
     * @param command the confirmation command
     * @return {@code true} when the code is valid and 2FA was enabled
     */
    boolean handle(ConfirmTotpSetupCommand command);

    /**
     * Verifies a TOTP code during a two-factor sign-in.
     *
     * @param command the verification command
     * @return the user when the code is valid
     */
    Optional<User> handle(VerifyTotpCommand command);

    /**
     * Issues a signed bearer token for an already-authenticated user.
     *
     * @param user the authenticated user
     * @return the signed JWT
     */
    String issueTokenFor(User user);

    /**
     * Resolves the internal {@code Long} primary key from a public UUID.
     * Used by TOTP endpoints that still route by internal ID internally.
     *
     * @param publicId the UUID exposed to the client
     * @return the internal id, or empty if not found
     */
    Optional<Long> resolveInternalId(String publicId);
}
