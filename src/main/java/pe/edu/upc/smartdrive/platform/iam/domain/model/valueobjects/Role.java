package pe.edu.upc.smartdrive.platform.iam.domain.model.valueobjects;

/**
 * Roles within a company (tenant).
 *
 * <ul>
 *   <li>{@code ADMIN}  — the person who registers the company. One per company.
 *       Signs in with email + password. Manages sellers, sees the dashboard and the
 *       confirmed-credit reports produced by the company's sellers.</li>
 *   <li>{@code SELLER} — a worker created by the admin (up to the company's worker cap).
 *       Signs in with a generated username (code) + password. Operates clients, vehicles
 *       and simulations, inheriting the company's bank default values without editing them.</li>
 * </ul>
 */
public enum Role {
    ADMIN,
    SELLER;

    /** Spring Security expects authorities prefixed with {@code ROLE_}. */
    public String authority() {
        return "ROLE_" + name();
    }
}
