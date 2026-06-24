package pe.edu.upc.smartdrive.platform.iam.application.internal.commandservices;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.totp.TotpService;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.*;
import pe.edu.upc.smartdrive.platform.iam.domain.model.valueobjects.TotpEnrolment;
import pe.edu.upc.smartdrive.platform.iam.domain.services.UserCommandService;
import pe.edu.upc.smartdrive.platform.iam.domain.repositories.UserRepository;

import java.util.Optional;

/**
 * Default implementation of {@link UserCommandService}. Orchestrates the
 * {@link UserRepository} aggregate and the hashing, token and TOTP outbound ports.
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final TotpService totpService;

    public UserCommandServiceImpl(UserRepository userRepository, HashingService hashingService,
                                  TokenService tokenService, TotpService totpService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.totpService = totpService;
    }

    @Override
    public Optional<User> handle(SignUpCommand command) {
        if (userRepository.existsByEmail(command.email().toLowerCase()))
            throw new IllegalArgumentException("Email " + command.email() + " is already registered");
        var user = new User(command, hashingService.encode(command.password()));
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> handle(SignInCommand command) {
        var user = userRepository.findByEmail(command.email().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!hashingService.matches(command.password(), user.getPassword()))
            throw new IllegalArgumentException("Invalid credentials");
        return Optional.of(user);
    }

    @Override
    public TotpEnrolment handle(SetupTotpCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var enrolment = totpService.generateEnrolment(user.getEmail());
        user.startTotpEnrolment(enrolment.secret());
        userRepository.save(user);
        return enrolment;
    }

    @Override
    public boolean handle(ConfirmTotpSetupCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getTotpSecretPending() == null)
            throw new IllegalStateException("There is no pending TOTP setup");
        if (!totpService.verifyCode(user.getTotpSecretPending(), command.token()))
            return false;
        user.confirmTotpEnrolment();
        userRepository.save(user);
        return true;
    }

    @Override
    public Optional<User> handle(VerifyTotpCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!user.isTotpEnabled())
            throw new IllegalStateException("Two-factor authentication is not enabled for this user");
        if (!totpService.verifyCode(user.getTotpSecret(), command.token()))
            return Optional.empty();
        return Optional.of(user);
    }

    @Override
    public String issueTokenFor(User user) {
        return tokenService.generateToken(user.getEmail());
    }

    @Override
    public Optional<Long> resolveInternalId(String publicId) {
        return userRepository.findByPublicId(publicId).map(User::getId);
    }
}
