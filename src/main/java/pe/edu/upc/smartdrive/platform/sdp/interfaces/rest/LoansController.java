package pe.edu.upc.smartdrive.platform.sdp.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetUserByHandleQuery;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetUserByIdQuery;
import pe.edu.upc.smartdrive.platform.iam.domain.services.UserQueryService;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateLoanCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetConfirmedLoansByCompanyQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanByIdQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanReportQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.queries.GetLoanScheduleQuery;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.LoanCommandService;
import pe.edu.upc.smartdrive.platform.sdp.domain.services.LoanQueryService;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.LoanReportResource;
import pe.edu.upc.smartdrive.platform.sdp.interfaces.rest.resources.ConfirmedLoanResource;
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
 * <p>On confirmation the loan is stamped with the authenticated user's company and, when the
 * user is a seller, their id; its indicators are recomputed server-side. The schedule and the
 * report are derived data, regenerated on demand. {@code /loans/confirmed} lets a company admin
 * review every credit confirmed by their sellers, scoped to their own company.</p>
 */
@RestController
@RequestMapping(value = "/loans", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Loans", description = "Vehicle credit simulation and persistence")
public class LoansController {

    private final LoanCommandService loanCommandService;
    private final LoanQueryService loanQueryService;
    private final UserQueryService userQueryService;

    public LoansController(LoanCommandService loanCommandService, LoanQueryService loanQueryService,
                           UserQueryService userQueryService) {
        this.loanCommandService = loanCommandService;
        this.loanQueryService = loanQueryService;
        this.userQueryService = userQueryService;
    }

    private User currentUser(Authentication authentication) {
        return userQueryService.handle(new GetUserByHandleQuery(authentication.getName()))
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    @PostMapping
    @Operation(summary = "Persist a confirmed loan",
            description = "Stamps the loan with the caller's company (and seller id when applicable) and recomputes its indicators server-side.")
    public ResponseEntity<LoanResource> create(Authentication authentication, @RequestBody LoanResource resource) {
        var user = currentUser(authentication);
        var base = CreateLoanCommandFromResourceAssembler.toCommandFromResource(resource);
        // Override ownership with the authenticated identity (never trust the client for this).
        var command = new CreateLoanCommand(
                base.carId(), base.clientId(), base.configId(),
                user.getCompanyId(),
                user.isSeller() ? user.getId() : base.sellerId(),
                "CONFIRMED",
                base.initialFee(), base.vehiclePrice(), base.loanAmount(), base.installmentsQty(), base.startDate(),
                base.fixedInstallment(), base.npvDebtor(), base.irrDebtor(), base.tcea(), base.trea(),
                base.totalInterest(), base.totalInsurance(), base.totalRiskInsurance(), base.totalGps(),
                base.totalPostage(), base.totalCommission(), base.totalTax(),
                base.initialCosts(), base.residualValue(), base.ctc());
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

    @GetMapping("/confirmed")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List confirmed loans of the admin's company",
            description = "Admin-only view of every credit confirmed by the company's sellers, with the seller name resolved.")
    public ResponseEntity<List<ConfirmedLoanResource>> confirmed(Authentication authentication) {
        var admin = currentUser(authentication);
        var loans = loanQueryService.handle(new GetConfirmedLoansByCompanyQuery(admin.getCompanyId())).stream()
                .map(loan -> {
                    // Resolve the seller's display name from the internal id stored on the loan.
                    String sellerName = loan.getSellerId() == null
                            ? "Administrador"
                            : userQueryService.handle(new GetUserByIdQuery(loan.getSellerId()))
                                .map(User::getFullName)
                                .orElse("Administrador");
                    return new ConfirmedLoanResource(
                            loan.getId(), loan.getCarId(), loan.getClientId(),
                            loan.getSellerId(), sellerName, loan.getStatus(),
                            loan.getVehiclePrice(), loan.getInstallmentsQty(), loan.getTcea(), loan.getCtc());
                })
                .toList();
        return ResponseEntity.ok(loans);
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
