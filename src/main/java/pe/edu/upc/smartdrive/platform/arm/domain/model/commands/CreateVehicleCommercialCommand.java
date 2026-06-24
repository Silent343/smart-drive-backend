package pe.edu.upc.smartdrive.platform.arm.domain.model.commands;

/** Command to register vehicle commercial information. */
public record CreateVehicleCommercialCommand(String vehicleId, String userId, Double price, String company) {
}
