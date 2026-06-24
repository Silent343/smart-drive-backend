package pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources;

/** Minimal response returned after a successful registration. */
public record SignUpResultResource(String id, String email) {
}
