package pe.edu.upc.smartdrive.platform.iam.domain.model.queries;

/** Query to load a user by their login handle (admin email or seller username). */
public record GetUserByHandleQuery(String handle) {
}
