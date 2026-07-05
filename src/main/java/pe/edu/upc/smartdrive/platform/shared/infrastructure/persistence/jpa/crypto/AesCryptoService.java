package pe.edu.upc.smartdrive.platform.shared.infrastructure.persistence.jpa.crypto;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Application-wide AES-GCM cipher used to encrypt personally identifiable
 * information (PII) at rest, such as the client DNI and public identifiers.
 *
 * <p>AES-GCM is an authenticated encryption mode: besides confidentiality it
 * guarantees integrity, so a tampered ciphertext fails to decrypt instead of
 * silently returning garbage. A fresh random 12-byte IV is generated for every
 * value and prepended to the ciphertext, then the whole blob is Base64-encoded
 * so it can live in an ordinary {@code VARCHAR} column.</p>
 *
 * <p>The key is provided through the {@code app.crypto.aes-key} property
 * (Base64 of 16, 24 or 32 raw bytes → AES-128/192/256). It must be supplied via
 * an environment variable in every real environment and never committed.</p>
 */
@Component
public class AesCryptoService {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int IV_LENGTH = 12;        // 96-bit IV recommended for GCM
    private static final int TAG_LENGTH_BITS = 128; // authentication tag length

    private final String base64Key;
    private final SecureRandom random = new SecureRandom();
    private SecretKeySpec keySpec;

    public AesCryptoService(@Value("${app.crypto.aes-key:}") String base64Key) {
        this.base64Key = base64Key;
    }

    @PostConstruct
    void init() {
        if (base64Key == null || base64Key.isBlank()) {
            throw new IllegalStateException(
                "Missing property 'app.crypto.aes-key'. Provide a Base64-encoded 16/24/32-byte key "
              + "(e.g. export APP_CRYPTO_AES_KEY=$(openssl rand -base64 32)).");
        }
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        if (keyBytes.length != 16 && keyBytes.length != 24 && keyBytes.length != 32) {
            throw new IllegalStateException(
                "Invalid AES key length: " + keyBytes.length + " bytes. Expected 16, 24 or 32.");
        }
        this.keySpec = new SecretKeySpec(keyBytes, "AES");
    }

    /** Encrypts a plaintext value; returns Base64(iv || ciphertext || tag), or {@code null} if input is null. */
    public String encrypt(String plaintext) {
        if (plaintext == null) return null;
        try {
            byte[] iv = new byte[IV_LENGTH];
            random.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            byte[] cipherText = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            byte[] combined = new byte[iv.length + cipherText.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);
            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to encrypt value", e);
        }
    }

    /** Reverses {@link #encrypt(String)}; returns {@code null} if input is null. */
    public String decrypt(String stored) {
        if (stored == null) return null;
        try {
            byte[] combined = Base64.getDecoder().decode(stored);
            byte[] iv = new byte[IV_LENGTH];
            byte[] cipherText = new byte[combined.length - IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, IV_LENGTH);
            System.arraycopy(combined, IV_LENGTH, cipherText, 0, cipherText.length);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new GCMParameterSpec(TAG_LENGTH_BITS, iv));
            return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decrypt value (wrong key or tampered data)", e);
        }
    }
}
