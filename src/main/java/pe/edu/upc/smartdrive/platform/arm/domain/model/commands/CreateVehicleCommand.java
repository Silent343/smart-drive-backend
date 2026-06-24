package pe.edu.upc.smartdrive.platform.arm.domain.model.commands;

/** Command to register a new vehicle. */
public record CreateVehicleCommand(String code, String status, String imageUrl) {
}
