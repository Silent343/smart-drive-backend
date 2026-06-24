package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** Request body to start TOTP enrolment. */
public record TotpSetupRequestResource(String userId) {
}
