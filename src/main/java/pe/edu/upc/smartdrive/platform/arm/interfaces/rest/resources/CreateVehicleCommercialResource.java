package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/** Input payload to create vehicle commercial information. */
public record CreateVehicleCommercialResource(String vehicleId, String userId, Double price, String company) {
}
