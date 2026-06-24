package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources;

import java.util.List;

/**
 * REST representation of a loan report: the loan, its configuration and the schedule.
 * Returned inside a single-element array by the report endpoint, as the frontend reads
 * {@code reports[0]}.
 */
public record LoanReportResource(String id, LoanResource loan, CreditConfigResource config,
                                 List<ScheduleRowResource> schedule) {
}
