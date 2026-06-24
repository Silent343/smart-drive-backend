package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** Request body to confirm a TOTP setup or verify a TOTP sign-in code. */
public record TotpVerifyRequestResource(String userId, String token) {
}
