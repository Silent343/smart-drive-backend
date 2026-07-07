package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Read model for the admin's confirmed-credits report. It extends the plain loan data with the
 * resolved seller name, so the frontend can show "Ana Torres" instead of an opaque seller id.
 * The seller is resolved server-side (the loan stores the seller's internal Long id, which the
 * frontend's seller list -keyed by public UUID- cannot cross-reference on its own).
 */
public record ConfirmedLoanResource(
        @JsonProperty("id") Long id,
        @JsonProperty("car_id") String carId,
        @JsonProperty("client_id") String clientId,
        @JsonProperty("seller_id") Long sellerId,
        @JsonProperty("seller_name") String sellerName,
        @JsonProperty("status") String status,
        @JsonProperty("vehicle_price") double vehiclePrice,
        @JsonProperty("instalments_qty") int installmentsQty,
        @JsonProperty("tcea") double tcea,
        @JsonProperty("ctc") double ctc) {
}
