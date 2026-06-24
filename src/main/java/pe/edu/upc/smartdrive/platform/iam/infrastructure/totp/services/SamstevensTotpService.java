package pe.edu.upc.smartdrive.platform.iam.infrastructure.totp.services;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.totp.TotpService;
import pe.edu.upc.smartdrive.platform.iam.domain.model.valueobjects.TotpEnrolment;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

/**
 * TOTP implementation backed by the dev.samstevens.totp library. Secrets are base32
 * encoded and codes use SHA1 / 6 digits / 30s, matching Google Authenticator and the
 * tolerance the SmartDrive frontend expects (a +-1 period window).
 */
@Service
public class SamstevensTotpService implements TotpService {

    private final SecretGenerator secretGenerator = new DefaultSecretGenerator();
    private final QrGenerator qrGenerator = new ZxingPngQrGenerator();
    private final TimeProvider timeProvider = new SystemTimeProvider();
    private final CodeGenerator codeGenerator = new DefaultCodeGenerator();
    private final CodeVerifier codeVerifier;

    @Value("${totp.issuer:SmartDrive}")
    private String issuer;

    public SamstevensTotpService() {
        var verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
        verifier.setAllowedTimePeriodDiscrepancy(1);
        this.codeVerifier = verifier;
    }

    @Override
    public TotpEnrolment generateEnrolment(String accountLabel) {
        String secret = secretGenerator.generate();
        QrData data = new QrData.Builder()
                .label(accountLabel)
                .secret(secret)
                .issuer(issuer)
                .algorithm(HashingAlgorithm.SHA1)
                .digits(6)
                .period(30)
                .build();
        try {
            byte[] imageData = qrGenerator.generate(data);
            String qrCode = getDataUriForImage(imageData, qrGenerator.getImageMimeType());
            return new TotpEnrolment(qrCode, secret);
        } catch (QrGenerationException e) {
            throw new IllegalStateException("Could not generate the TOTP QR code", e);
        }
    }

    @Override
    public boolean verifyCode(String secret, String code) {
        if (secret == null || code == null) return false;
        return codeVerifier.isValidCode(secret, code);
    }
}
