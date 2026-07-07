package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateLoanCommand;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.LoanResource;

import java.util.List;

/**
 * Builds a {@link CreateLoanCommand} from a {@link LoanResource}. Only the inputs are taken
 * from the client; the indicators and cost totals are recomputed server-side by the
 * calculation engine before persistence, so a tampered or stale client cannot store
 * inconsistent numbers. Totals are seeded to 0 here and overwritten by the command service.
 *
 * <p>Multi-vehicle: when the resource carries a {@code vehicles} list it is mapped through;
 * otherwise a single-vehicle list is synthesized from {@code car_id}/{@code vehicle_price}.</p>
 */
public class CreateLoanCommandFromResourceAssembler {
    public static CreateLoanCommand toCommandFromResource(LoanResource r) {
        List<CreateLoanCommand.Vehicle> vehicles;
        if (r.vehicles() != null && !r.vehicles().isEmpty()) {
            vehicles = r.vehicles().stream()
                    .map(v -> new CreateLoanCommand.Vehicle(v.carId(), v.price()))
                    .toList();
        } else if (r.carId() != null) {
            vehicles = List.of(new CreateLoanCommand.Vehicle(r.carId(), r.vehiclePrice()));
        } else {
            vehicles = List.of();
        }
        return CreateLoanCommand.single(
                r.carId(), r.clientId(), r.configId(),
                null,                 // companyId — resolved server-side from the seller
                r.sellerId(),
                r.status() != null ? r.status() : "CONFIRMED",
                r.initialFee(), r.vehiclePrice(), r.loanAmount(), r.installmentsQty(), r.startDate(),
                0, 0, 0, 0, 0,        // fixedInstallment, npv, irr, tcea, trea
                0, 0, 0, 0, 0, 0, 0,  // totalInterest, insurance, risk, gps, postage, commission, tax
                0, 0, 0)              // initialCosts, residualValue, ctc
                .withVehicles(vehicles);
    }
}
