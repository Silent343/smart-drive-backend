package pe.edu.upc.smartdrive.platform.arm.interfaces.rest.resources;

/** Input payload to update vehicle commercial information. The id comes from the path. */
public record UpdateVehicleCommercialResource(String vehicleId, String userId, Double price, String company) {
}
