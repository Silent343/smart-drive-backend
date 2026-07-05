package pe.edu.upc.smartdrive.platform.shared.domain.validation;

/**
 * Small collection of reusable input validators shared across bounded contexts. Each method
 * throws {@link IllegalArgumentException} with a user-facing (Spanish) message when the value
 * is invalid, so callers can surface it directly. Centralizing them keeps validation rules
 * consistent between the IAM (passwords), ARM (DNI/RUC) and SDP (financial inputs) contexts.
 */
public final class InputValidator {

    private InputValidator() { }

    /** Password policy mirroring the frontend: 8+ chars, upper, lower, digit and symbol. */
    public static void validatePassword(String password) {
        if (password == null || password.length() < 8)
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres");
        if (!password.matches(".*[A-Z].*"))
            throw new IllegalArgumentException("La contraseña debe incluir al menos una mayúscula");
        if (!password.matches(".*[a-z].*"))
            throw new IllegalArgumentException("La contraseña debe incluir al menos una minúscula");
        if (!password.matches(".*[0-9].*"))
            throw new IllegalArgumentException("La contraseña debe incluir al menos un número");
        if (!password.matches(".*[^A-Za-z0-9].*"))
            throw new IllegalArgumentException("La contraseña debe incluir al menos un símbolo");
    }

    /** Peruvian DNI: exactly 8 digits. */
    public static void validateDni(String dni) {
        if (dni == null || !dni.matches("\\d{8}"))
            throw new IllegalArgumentException("El DNI debe tener exactamente 8 dígitos");
    }

    /** Peruvian RUC: exactly 11 digits (allowed to be blank when optional). */
    public static void validateRucOptional(String ruc) {
        if (ruc != null && !ruc.isBlank() && !ruc.matches("\\d{11}"))
            throw new IllegalArgumentException("El RUC debe tener exactamente 11 dígitos");
    }

    public static void requireNotBlank(String value, String fieldName) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("El campo '" + fieldName + "' es obligatorio");
    }

    public static void requirePositive(double value, String fieldName) {
        if (value <= 0)
            throw new IllegalArgumentException("El campo '" + fieldName + "' debe ser mayor a 0");
    }

    public static void requireNonNegative(double value, String fieldName) {
        if (value < 0)
            throw new IllegalArgumentException("El campo '" + fieldName + "' no puede ser negativo");
    }

    public static void requireRange(double value, double min, double max, String fieldName) {
        if (value < min || value > max)
            throw new IllegalArgumentException(
                "El campo '" + fieldName + "' debe estar entre " + min + " y " + max);
    }
}
