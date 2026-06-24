package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** Returned by sign-in when the account has 2FA enabled and a TOTP code is required. */
public record TotpChallengeResource(boolean requiresTotp, String userId) {
}
