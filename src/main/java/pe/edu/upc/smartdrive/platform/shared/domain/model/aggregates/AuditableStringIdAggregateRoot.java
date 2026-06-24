package pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.UUID;

/**
 * Base class for aggregate roots identified by an application-assigned
 * {@code String} id.
 *
 * <p>The SmartDrive frontend models clients and vehicles with short string
 * identifiers, so the ARM aggregates keep string keys. The id is generated on
 * construction (a compact, URL-safe token) unless explicitly assigned, which
 * also lets the seeding routine reuse the ids expected by the frontend.</p>
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableStringIdAggregateRoot {

    @Id
    @Column(length = 64)
    private String id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Date updatedAt;

    protected AuditableStringIdAggregateRoot() {
        this.id = generateId();
    }

    /**
     * Generates a compact, URL-safe identifier (7 characters) resembling the
     * tokens used by the frontend.
     *
     * @return a freshly generated identifier
     */
    protected static String generateId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 7);
    }

    public String getId() {
        return id;
    }

    /**
     * Assigns an explicit identifier. Used by the seeding routine to preserve
     * the relationships defined in the frontend demo data.
     *
     * @param id the identifier to assign
     */
    public void setId(String id) {
        this.id = id;
    }
}
