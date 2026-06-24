package pe.edu.upc.smartdrive.platform.arm.domain.model.commands;

/** Command to update vehicle commercial information. */
public record UpdateVehicleCommercialCommand(String id, String vehicleId, String userId, Double price, String company) {
}
