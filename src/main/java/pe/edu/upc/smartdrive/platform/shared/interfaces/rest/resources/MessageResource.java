package pe.edu.upc.smartdrive.platform.shared.interfaces.rest.resources;

/**
 * Simple message envelope used for informational REST responses.
 *
 * @param message the human-readable message
 */
public record MessageResource(String message) {
}
