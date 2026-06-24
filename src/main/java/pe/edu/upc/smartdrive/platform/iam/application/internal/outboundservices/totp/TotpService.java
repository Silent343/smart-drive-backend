package pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.totp;

import pe.edu.upc.smartdrive.platform.iam.domain.model.valueobjects.TotpEnrolment;

/**
 * Outbound port for TOTP (RFC 6238) secret generation, QR rendering and verification.
 */
public interface TotpService {

    /**
     * Generates a new secret and a QR code for the given account label.
     *
     * @param accountLabel the label shown in the authenticator app (e.g. the email)
     * @return the enrolment data (QR data URI + base32 secret)
     */
    TotpEnrolment generateEnrolment(String accountLabel);

    /**
     * Validates a code against a secret with a small time-window tolerance.
     *
     * @param secret the base32 secret
     * @param code   the 6-digit code supplied by the user
     * @return {@code true} when the code is valid
     */
    boolean verifyCode(String secret, String code);
}
