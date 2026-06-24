package pe.edu.upc.smartdrive.platform.iam.infrastructure.hashing.bcrypt;

import pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Marker interface bridging the IAM {@link HashingService} port and Spring
 * Security's {@link PasswordEncoder}, so a single bean satisfies both roles.
 */
public interface BCryptHashingService extends HashingService, PasswordEncoder {
}
