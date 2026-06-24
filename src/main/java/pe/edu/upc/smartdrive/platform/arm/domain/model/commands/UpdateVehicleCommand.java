package pe.edu.upc.smartdrive.platform.arm.domain.model.commands;

/** Command to update an existing vehicle. */
public record UpdateVehicleCommand(String id, String code, String status, String imageUrl) {
}
