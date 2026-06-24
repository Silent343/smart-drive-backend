package pe.edu.upc.smartdrive.platform.iam.domain.model.valueobjects;

/**
 * Value object returned when starting a TOTP enrolment: the QR code (as a data URI
 * image) the user scans, and the base32 secret for manual entry.
 *
 * @param qrCode the QR code rendered as a data:image/png;base64 URI
 * @param secret the base32-encoded shared secret
 */
public record TotpEnrolment(String qrCode, String secret) {
}
