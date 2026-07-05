package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.ScheduleRow;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.ScheduleRowResource;

/** Maps a {@link ScheduleRow} value object to its REST resource. */
public class ScheduleRowResourceFromValueAssembler {
    public static ScheduleRowResource toResourceFromValue(ScheduleRow r) {
        return new ScheduleRowResource(
                r.id(), r.loanId(), r.installmentNo(), r.paymentDate(),
                r.openingBalance(), r.interest(), r.amortization(),
                r.insurance(), r.riskInsurance(), r.gps(), r.postage(), r.commission(), r.tax(),
                r.monthlyPayment(), r.endingBalance(), r.gracePeriodType());
    }
}
