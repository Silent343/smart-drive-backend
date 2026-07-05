package pe.edu.upc.smartdrive.platform.shared.infrastructure.persistence.jpa.crypto;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA converter that transparently encrypts a {@code String} attribute when it is
 * written to the database and decrypts it when read back. Apply it to a field with
 * {@code @Convert(converter = EncryptedStringConverter.class)}.
 *
 * <p>JPA instantiates converters itself, outside the Spring container, so the
 * {@link AesCryptoService} cannot be injected through the constructor. Instead it is
 * published once at startup by {@link CryptoConverterInitializer} into a static holder
 * that this converter reads. This is the conventional workaround for Spring-managed
 * dependencies inside JPA converters.</p>
 *
 * <p><strong>Trade-off:</strong> an encrypted column cannot be used in SQL
 * {@code WHERE}/{@code LIKE} filters or unique constraints, because each encryption
 * produces a different ciphertext (random IV). If you need to look up a client by DNI,
 * keep a separate blind-index/hash column for equality search, or filter in memory.</p>
 */
@Converter
public class EncryptedStringConverter implements AttributeConverter<String, String> {

    private static volatile AesCryptoService crypto;

    static void setCryptoService(AesCryptoService service) {
        crypto = service;
    }

    private AesCryptoService crypto() {
        AesCryptoService c = crypto;
        if (c == null) {
            throw new IllegalStateException(
                "AesCryptoService not initialized yet. Ensure CryptoConverterInitializer ran at startup.");
        }
        return c;
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return crypto().encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return crypto().decrypt(dbData);
    }
}
