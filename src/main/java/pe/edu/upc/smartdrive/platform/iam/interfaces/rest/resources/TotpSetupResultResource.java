package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** QR code (data URI) and base32 secret returned when starting TOTP enrolment. */
public record TotpSetupResultResource(String qrCode, String secret) {
}
