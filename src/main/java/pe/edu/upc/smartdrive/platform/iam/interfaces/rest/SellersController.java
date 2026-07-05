package pe.edu.upc.smartdrive.platform.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.RegisterSellerCommand;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetSellersByCompanyQuery;
import pe.edu.upc.smartdrive.platform.iam.domain.model.queries.GetUserByHandleQuery;
import pe.edu.upc.smartdrive.platform.iam.domain.services.UserCommandService;
import pe.edu.upc.smartdrive.platform.iam.domain.services.UserQueryService;
import pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources.RegisterSellerResource;
import pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources.SellerRegistrationResultResource;
import pe.edu.upc.smartdrive.platform.iam.interfaces.rest.resources.SellerResource;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Admin-only management of the company's sellers. Every endpoint is scoped to the
 * authenticated admin's own company (tenant), so an admin can never see or modify
 * sellers of another company.
 */
@RestController
@RequestMapping(value = "/sellers", produces = APPLICATION_JSON_VALUE)
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Sellers", description = "Admin management of company sellers")
public class SellersController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public SellersController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    /** Resolves the authenticated admin from the security context. */
    private User currentAdmin(Authentication authentication) {
        String handle = authentication.getName();
        return userQueryService.handle(new GetUserByHandleQuery(handle))
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    /** Registers a seller under the admin's company; returns the generated credentials once. */
    @PostMapping
    @Operation(summary = "Register seller",
            description = "Creates a seller under the admin's company and returns the generated username and initial password (shown once).")
    public ResponseEntity<SellerRegistrationResultResource> register(
            Authentication authentication, @RequestBody RegisterSellerResource resource) {
        var admin = currentAdmin(authentication);
        var command = new RegisterSellerCommand(admin.getCompanyId(), resource.firstName(),
                resource.lastName(), resource.code(), resource.dni(), resource.phone());
        var result = userCommandService.handle(command);
        var body = new SellerRegistrationResultResource(
                result.seller().getPublicId(), result.username(), result.initialPassword(),
                result.seller().getFullName(), result.seller().getCode());
        return new ResponseEntity<>(body, HttpStatus.CREATED);
    }

    /** Lists the sellers of the admin's company. */
    @GetMapping
    @Operation(summary = "List sellers", description = "Lists the sellers registered under the admin's company.")
    public ResponseEntity<List<SellerResource>> list(Authentication authentication) {
        var admin = currentAdmin(authentication);
        var sellers = userQueryService.handle(new GetSellersByCompanyQuery(admin.getCompanyId()))
                .stream()
                .map(s -> new SellerResource(s.getPublicId(), s.getUsername(), s.getFullName(),
                        s.getFirstName(), s.getLastName(), s.getCode(), s.getDni(), s.getPhone()))
                .toList();
        return ResponseEntity.ok(sellers);
    }
}
