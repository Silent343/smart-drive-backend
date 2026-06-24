package pe.edu.upc.smartdrive.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * SmartDrive Finance Platform entry point.
 *
 * <p>RESTful backend organized with Domain-Driven Design. It exposes the IAM
 * (authentication + TOTP 2FA), ARM (clients and vehicles) and SDP (vehicle
 * credit simulation) bounded contexts consumed by the SmartDrive Angular app.</p>
 */
@SpringBootApplication
@EnableJpaAuditing
public class SmartdrivePlatformApplication {
    public static void main(String[] args) {
        SpringApplication.run(SmartdrivePlatformApplication.class, args);
    }
}
