package pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.tokens;

/**
 * Outbound port for bearer token issuance and validation.
 */
public interface TokenService {
    String generateToken(String username);
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
}
