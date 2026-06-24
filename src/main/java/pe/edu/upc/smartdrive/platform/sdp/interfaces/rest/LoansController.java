package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanByIdQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanReportQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanScheduleQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.LoanCommandService;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.LoanQueryService;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.LoanReportResource;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.LoanResource;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.ScheduleRowResource;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform.CreateLoanCommandFromResourceAssembler;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform.LoanReportResourceFromDataAssembler;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform.LoanResourceFromEntityAssembler;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform.ScheduleRowResourceFromValueAssembler;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.transform.SimulateLoanCommandFromResourceAssembler;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Loan endpoints (SDP bounded context), exposed at {@code /loans}.
 *
 * <p>Confirmed loans are persisted exactly as posted by the client; the amortization
 * schedule and the report are derived data, regenerated on demand by the calculation
 * engine. The simulate endpoint computes indicators without persisting. The report
 * endpoint returns a single-element array, matching the frontend's {@code reports[0]}
 * read.</p>
 */
@RestController
@RequestMapping(value = "/loans", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Loans", description = "Vehicle credit simulation and persistence")
public class LoansController {

    private final LoanCommandService loanCommandService;
    private final LoanQueryService loanQueryService;

    public LoansController(LoanCommandService loanCommandService, LoanQueryService loanQueryService) {
        this.loanCommandService = loanCommandService;
        this.loanQueryService = loanQueryService;
    }

    @PostMapping
    @Operation(summary = "Persist a confirmed loan")
    public ResponseEntity<LoanResource> create(@RequestBody LoanResource resource) {
        var command = CreateLoanCommandFromResourceAssembler.toCommandFromResource(resource);
        return loanCommandService.handle(command)
                .map(LoanResourceFromEntityAssembler::toResourceFromEntity)
                .map(r -> new ResponseEntity<>(r, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/simulate")
    @Operation(summary = "Simulate a loan without persisting it")
    public ResponseEntity<LoanResource> simulate(@RequestBody LoanResource resource) {
        var command = SimulateLoanCommandFromResourceAssembler.toCommandFromResource(resource);
        return loanCommandService.handle(command)
                .map(LoanResourceFromEntityAssembler::toResourceFromSimulated)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a loan by id")
    public ResponseEntity<LoanResource> getById(@PathVariable Long id) {
        return loanQueryService.handle(new GetLoanByIdQuery(id))
                .map(LoanResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{loanId}/schedule")
    @Operation(summary = "Get the amortization schedule of a loan")
    public ResponseEntity<List<ScheduleRowResource>> getSchedule(@PathVariable Long loanId) {
        var resources = loanQueryService.handle(new GetLoanScheduleQuery(loanId)).stream()
                .map(ScheduleRowResourceFromValueAssembler::toResourceFromValue).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/{loanId}/report")
    @Operation(summary = "Get the full report (loan, config and schedule) of a loan")
    public ResponseEntity<List<LoanReportResource>> getReport(@PathVariable Long loanId) {
        return loanQueryService.handle(new GetLoanReportQuery(loanId))
                .map(LoanReportResourceFromDataAssembler::toResourceFromData)
                .map(report -> ResponseEntity.ok(List.of(report)))
                .orElseGet(() -> ResponseEntity.ok(List.of()));
    }
}
