package pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.credentials;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

/**
 * Generates seller login credentials: the username (derived from the company domain and the
 * seller code) and a strong random initial password shown once to the admin.
 */
@Component
public class CredentialGenerator {

    // Avoids ambiguous characters (0/O, 1/l/I) to make the password easy to dictate.
    private static final String UPPER = "ABCDEFGHJKLMNPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijkmnpqrstuvwxyz";
    private static final String DIGITS = "23456789";
    private static final String SYMBOLS = "!@#$%*";
    private static final String ALL = UPPER + LOWER + DIGITS + SYMBOLS;

    private final SecureRandom random = new SecureRandom();

    /** Builds the seller username as {@code domain-code}, lowercased. */
    public String buildUsername(String companyDomain, String code) {
        return (companyDomain + "-" + code).toLowerCase();
    }

    /**
     * Generates a 12-character password guaranteed to contain an uppercase letter, a lowercase
     * letter, a digit and a symbol (matching the frontend's password policy).
     */
    public String generatePassword() {
        StringBuilder sb = new StringBuilder(12);
        sb.append(pick(UPPER));
        sb.append(pick(LOWER));
        sb.append(pick(DIGITS));
        sb.append(pick(SYMBOLS));
        for (int i = 4; i < 12; i++) sb.append(pick(ALL));
        return shuffle(sb.toString());
    }

    private char pick(String pool) {
        return pool.charAt(random.nextInt(pool.length()));
    }

    private String shuffle(String s) {
        char[] a = s.toCharArray();
        for (int i = a.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char t = a[i]; a[i] = a[j]; a[j] = t;
        }
        return new String(a);
    }
}
