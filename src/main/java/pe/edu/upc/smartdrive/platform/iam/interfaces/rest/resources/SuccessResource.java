package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** Simple boolean acknowledgement, e.g. for TOTP setup confirmation. */
public record SuccessResource(boolean success) {
}
