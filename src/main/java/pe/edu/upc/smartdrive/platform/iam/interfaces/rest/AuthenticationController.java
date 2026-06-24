package pe.edu.upc.smartdrive.platform.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.ConfirmTotpSetupCommand;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.SetupTotpCommand;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.VerifyTotpCommand;
import pe.edu.upc.smartdrive.platform.iam.domain.services.UserCommandService;
import pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources.*;
import pe.edu.upc.smartdrive.platform.iam.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import pe.edu.upc.smartdrive.platform.iam.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import pe.edu.upc.smartdrive.platform.iam.interfaces.rest.transform.SignUpCommandFromResourceAssembler;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Authentication and TOTP (two-factor) endpoints.
 *
 * <p>The route layout mirrors what the SmartDrive Angular client calls:
 * {@code /authentication/sign-in}, {@code /authentication/sign-up},
 * {@code /totp-setup}, {@code /verify-totp-setup} and {@code /verify-totp}.</p>
 */
@RestController
@RequestMapping(produces = APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Sign-in, sign-up and TOTP two-factor endpoints")
public class AuthenticationController {

    private final UserCommandService userCommandService;

    public AuthenticationController(UserCommandService userCommandService) {
        this.userCommandService = userCommandService;
    }

    /**
     * Authenticates with email and password. If the account has TOTP enabled, a
     * challenge is returned instead of a session token.
     */
    @PostMapping("/authentication/sign-in")
    @Operation(summary = "Sign in", description = "Validates credentials; returns a JWT or a TOTP challenge when 2FA is enabled.")
    public ResponseEntity<?> signIn(@RequestBody SignInResource resource) {
        var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command).orElseThrow();
        if (user.isTotpEnabled()) {
            // Expose the public UUID — never the sequential Long primary key.
            return ResponseEntity.ok(new TotpChallengeResource(true, user.getPublicId()));
        }
        var token = userCommandService.issueTokenFor(user);
        return ResponseEntity.ok(AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(user, token));
    }

    /** Registers a new administrator account. */
    @PostMapping("/authentication/sign-up")
    @Operation(summary = "Sign up", description = "Registers a new administrator account.")
    public ResponseEntity<SignUpResultResource> signUp(@RequestBody SignUpResource resource) {
        var command = SignUpCommandFromResourceAssembler.toCommandFromResource(resource);
        var user = userCommandService.handle(command).orElseThrow();
        return new ResponseEntity<>(new SignUpResultResource(user.getPublicId(), user.getEmail()), HttpStatus.CREATED);
    }

    /** Starts TOTP enrolment: returns a QR code and the base32 secret. */
    @PostMapping("/totp-setup")
    @Operation(summary = "Start TOTP setup", description = "Generates a TOTP secret and QR code for the given user.")
    public ResponseEntity<TotpSetupResultResource> totpSetup(@RequestBody TotpSetupRequestResource resource) {
        // resource.userId() is now a public UUID — resolve to internal Long first.
        long internalId = userCommandService.resolveInternalId(resource.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + resource.userId()));
        var enrolment = userCommandService.handle(new SetupTotpCommand(internalId));
        return ResponseEntity.ok(new TotpSetupResultResource(enrolment.qrCode(), enrolment.secret()));
    }

    /** Confirms TOTP enrolment with the first code from the authenticator app. */
    @PostMapping("/verify-totp-setup")
    @Operation(summary = "Confirm TOTP setup", description = "Validates the first code and enables 2FA for the user.")
    public ResponseEntity<?> verifyTotpSetup(@RequestBody TotpVerifyRequestResource resource) {
        long internalId = userCommandService.resolveInternalId(resource.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + resource.userId()));
        var ok = userCommandService.handle(new ConfirmTotpSetupCommand(internalId, resource.token()));
        if (!ok) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SuccessResource(false));
        return ResponseEntity.ok(new SuccessResource(true));
    }

    /** Verifies a TOTP code to complete a two-factor sign-in and returns a JWT. */
    @PostMapping("/verify-totp")
    @Operation(summary = "Verify TOTP", description = "Completes a 2FA sign-in by verifying the TOTP code and issuing a JWT.")
    public ResponseEntity<?> verifyTotp(@RequestBody TotpVerifyRequestResource resource) {
        long internalId = userCommandService.resolveInternalId(resource.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + resource.userId()));
        var user = userCommandService.handle(new VerifyTotpCommand(internalId, resource.token()));
        if (user.isEmpty())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new SuccessResource(false));
        var token = userCommandService.issueTokenFor(user.get());
        return ResponseEntity.ok(AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(user.get(), token));
    }
}
