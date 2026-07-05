package pe.edu.upc.smartdrive.platform.iam.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.credentials.CredentialGenerator;
import pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.tokens.TokenService;
import pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.totp.TotpService;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.Company;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.PasswordResetToken;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.*;
import pe.edu.upc.smartdrive.platform.iam.domain.model.valueobjects.Role;
import pe.edu.upc.smartdrive.platform.iam.domain.model.valueobjects.TotpEnrolment;
import pe.edu.upc.smartdrive.platform.iam.domain.repositories.CompanyRepository;
import pe.edu.upc.smartdrive.platform.iam.domain.repositories.PasswordResetTokenRepository;
import pe.edu.upc.smartdrive.platform.iam.domain.repositories.UserRepository;
import pe.edu.upc.smartdrive.platform.iam.domain.services.UserCommandService;
import pe.edu.upc.smartdrive.platform.shared.domain.validation.InputValidator;

import java.util.Optional;

/**
 * Default implementation of {@link UserCommandService}. Orchestrates the {@link UserRepository}
 * and {@link CompanyRepository} aggregates together with the hashing, token, TOTP and
 * credential-generation outbound ports.
 */
@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;
    private final TotpService totpService;
    private final CredentialGenerator credentialGenerator;

    private static final long RESET_TOKEN_TTL_MINUTES = 60;

    public UserCommandServiceImpl(UserRepository userRepository, CompanyRepository companyRepository,
                                  PasswordResetTokenRepository passwordResetTokenRepository,
                                  HashingService hashingService, TokenService tokenService,
                                  TotpService totpService, CredentialGenerator credentialGenerator) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
        this.totpService = totpService;
        this.credentialGenerator = credentialGenerator;
    }

    /**
     * Provisions a company (tenant) and its administrator in a single transaction. Fails if the
     * admin email or the company domain is already taken.
     */
    @Override
    @Transactional
    public Optional<User> handle(SignUpCommand command) {
        InputValidator.requireNotBlank(command.email(), "email");
        InputValidator.validatePassword(command.password());
        InputValidator.validateDni(command.dni());
        InputValidator.validateRucOptional(command.ruc());
        if (userRepository.existsByEmail(command.email().toLowerCase()))
            throw new IllegalArgumentException("Email " + command.email() + " is already registered");

        String domain = Company.normalizeDomain(command.companyDomain());
        if (companyRepository.existsByDomain(domain))
            throw new IllegalArgumentException("Company domain '" + domain + "' is already taken");

        var company = companyRepository.save(
                new Company(command.businessName(), command.ruc(), domain, command.maxWorkers()));

        var admin = new User(command, company.getId(), hashingService.encode(command.password()));
        return Optional.of(userRepository.save(admin));
    }

    /**
     * Registers a seller under a company. Enforces the company's worker cap, derives the username
     * from the company domain and the seller code, and generates a strong initial password.
     */
    @Override
    @Transactional
    public SellerRegistration handle(RegisterSellerCommand command) {
        InputValidator.requireNotBlank(command.firstName(), "firstName");
        InputValidator.requireNotBlank(command.lastName(), "lastName");
        InputValidator.requireNotBlank(command.code(), "code");
        InputValidator.validateDni(command.dni());
        var company = companyRepository.findById(command.companyId())
                .orElseThrow(() -> new IllegalArgumentException("Company not found"));

        long currentSellers = userRepository.countByCompanyIdAndRole(company.getId(), Role.SELLER);
        if (currentSellers >= company.getMaxWorkers())
            throw new IllegalStateException(
                "Worker cap reached: the company allows at most " + company.getMaxWorkers() + " sellers");

        String username = credentialGenerator.buildUsername(company.getDomain(), command.code());
        if (userRepository.existsByUsername(username))
            throw new IllegalArgumentException("A seller with code '" + command.code() + "' already exists");

        String initialPassword = credentialGenerator.generatePassword();
        var seller = new User(command, company.getId(), username, hashingService.encode(initialPassword));
        userRepository.save(seller);
        return new SellerRegistration(seller, initialPassword, username);
    }

    @Override
    public Optional<User> handle(SignInCommand command) {
        String handle = command.identifier().trim();
        var user = userRepository.findByEmail(handle.toLowerCase())
                .or(() -> userRepository.findByUsername(handle))
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
        if (!hashingService.matches(command.password(), user.getPassword()))
            throw new IllegalArgumentException("Invalid credentials");
        return Optional.of(user);
    }

    @Override
    public TotpEnrolment handle(SetupTotpCommand command) {
        var user = userRepository.findById(command.userId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        var enrolment = totpService.generateEnrolment(user.loginHandle());
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
        return tokenService.generateToken(user.loginHandle());
    }

    @Override
    public Optional<Long> resolveInternalId(String publicId) {
        return userRepository.findByPublicId(publicId).map(User::getId);
    }

    @Override
    @Transactional
    public Optional<String> handle(ForgotPasswordCommand command) {
        String handle = command.identifier().trim();
        var user = userRepository.findByEmail(handle.toLowerCase())
                .or(() -> userRepository.findByUsername(handle));
        // Do not reveal whether the account exists; only issue a token when it does.
        if (user.isEmpty()) return Optional.empty();
        var token = new PasswordResetToken(user.get().getId(), RESET_TOKEN_TTL_MINUTES);
        passwordResetTokenRepository.save(token);
        return Optional.of(token.getToken());
    }

    @Override
    @Transactional
    public boolean handle(ResetPasswordCommand command) {
        var token = passwordResetTokenRepository.findByToken(command.token()).orElse(null);
        if (token == null || !token.isValidNow()) return false;
        InputValidator.validatePassword(command.newPassword());
        var user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new IllegalStateException("User for reset token not found"));
        user.changePassword(hashingService.encode(command.newPassword()));
        userRepository.save(user);
        token.markUsed();
        passwordResetTokenRepository.save(token);
        return true;
    }

    @Override
    public String resolveCompanyDomain(User user) {
        return companyRepository.findById(user.getCompanyId())
                .map(Company::getDomain)
                .orElse(null);
    }
}
