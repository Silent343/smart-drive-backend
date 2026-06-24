package pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.hashing;

/**
 * Outbound port for password hashing and verification.
 */
public interface HashingService {
    String encode(CharSequence rawPassword);
    boolean matches(CharSequence rawPassword, String encodedPassword);
}
