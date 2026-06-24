package pe.edu.upc.smartdrive.platform.iam.infrastructure.tokens.jwt;

import jakarta.servlet.http.HttpServletRequest;
import pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.tokens.TokenService;

/**
 * Extends the {@link TokenService} port with the ability to extract a bearer token
 * from an incoming HTTP request.
 */
public interface BearerTokenService extends TokenService {
    String getBearerTokenFrom(HttpServletRequest request);
}
