package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform;

import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.LoanReportData;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.LoanReportResource;

/**
 * Builds a {@link LoanReportResource} from {@link LoanReportData}. The synthetic id mirrors
 * the frontend convention {@code report-{loanId}}.
 */
public class LoanReportResourceFromDataAssembler {
    public static LoanReportResource toResourceFromData(LoanReportData data) {
        var schedule = data.schedule().stream()
                .map(ScheduleRowResourceFromValueAssembler::toResourceFromValue)
                .toList();
        return new LoanReportResource(
                "report-" + data.loan().getId(),
                LoanResourceFromEntityAssembler.toResourceFromEntity(data.loan()),
                CreditConfigResourceFromEntityAssembler.toResourceFromEntity(data.config()),
                schedule);
    }
}
