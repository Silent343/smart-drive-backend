package pe.edu.upc.smartdrive.platform.shared.infrastructure.persistence.jpa.crypto;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Bridges the Spring-managed {@link AesCryptoService} into the JPA-managed
 * {@link EncryptedStringConverter}, which is instantiated outside the Spring
 * container. Runs once, right after the crypto service is ready.
 */
@Component
public class CryptoConverterInitializer {

    private final AesCryptoService cryptoService;

    public CryptoConverterInitializer(AesCryptoService cryptoService) {
        this.cryptoService = cryptoService;
    }

    @PostConstruct
    void register() {
        EncryptedStringConverter.setCryptoService(cryptoService);
    }
}
