package pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateLoanCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.LoanVehicle;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.util.ArrayList;
import java.util.List;
import java.time.Instant;

/**
 * Loan aggregate (SDP bounded context).
 *
 * <p>Stores a confirmed vehicle credit: its inputs (vehicle, client, configuration, amounts,
 * term and start date) and the financial indicators produced by the French amortization
 * engine, now including the full Interbank-style cost totals (all-risk insurance, GPS, tax),
 * the initial costs, the residual (balloon) value and the investor TREA. The indicators are
 * computed by {@code LoanCalculationService} before persistence.</p>
 *
 * <p>A loan also records the {@code companyId} and {@code sellerId} that produced it and a
 * {@code status}, so a company admin can review the credits confirmed by their sellers.</p>
 */
@Entity
public class Loan extends AuditableAbstractAggregateRoot<Loan> {

    private String carId;       // FK -> ARM vehicle (primary/first vehicle, kept for compatibility)
    private String clientId;    // FK -> ARM client
    private Long configId;      // FK -> CreditConfig

    /**
     * All vehicles financed by this credit, stored in the child table {@code loan_vehicle}.
     * The loan's {@code vehiclePrice} is the sum of these prices, and {@code carId} mirrors the
     * first vehicle so existing single-vehicle consumers keep working.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "loan_vehicle", joinColumns = @JoinColumn(name = "loan_id"))
    private List<LoanVehicle> vehicles = new ArrayList<>();

    // Tenancy / ownership (bloque 1 + 3 support)
    private Long companyId;     // FK -> Company
    private Long sellerId;      // FK -> User (the seller who created it)

    @Column(nullable = false)
    private String status;      // SIMULATED | CONFIRMED

    private double initialFee;
    private double vehiclePrice;
    private double loanAmount;
    private int installmentsQty;

    @Column(nullable = false)
    private Instant startDate;

    private double fixedInstallment;
    private double npvDebtor;
    private double irrDebtor;
    private double tcea;
    private double trea;
    private double totalInterest;
    private double totalInsurance;
    private double totalRiskInsurance;
    private double totalGps;
    private double totalPostage;
    private double totalCommission;
    private double totalTax;
    private double initialCosts;
    private double residualValue;
    private double ctc;

    protected Loan() {
        this.status = "CONFIRMED";
    }

    public Loan(CreateLoanCommand c) {
        this.clientId = c.clientId();
        this.configId = c.configId();
        this.companyId = c.companyId();
        this.sellerId = c.sellerId();
        this.status = c.status() != null ? c.status() : "CONFIRMED";

        // Multi-vehicle: build the child collection. Falls back to the single carId when the
        // command carries no explicit vehicle list (keeps older callers working).
        this.vehicles = new ArrayList<>();
        if (c.vehicles() != null && !c.vehicles().isEmpty()) {
            c.vehicles().forEach(v -> this.vehicles.add(new LoanVehicle(v.carId(), v.price())));
        } else if (c.carId() != null) {
            this.vehicles.add(new LoanVehicle(c.carId(), c.vehiclePrice()));
        }
        // carId mirrors the first vehicle for single-vehicle compatibility.
        this.carId = this.vehicles.isEmpty() ? c.carId() : this.vehicles.get(0).getCarId();

        this.initialFee = c.initialFee();
        // vehiclePrice is the sum of all financed vehicles (or the provided value when no list).
        this.vehiclePrice = this.vehicles.isEmpty()
                ? c.vehiclePrice()
                : this.vehicles.stream().mapToDouble(LoanVehicle::getPrice).sum();
        this.loanAmount = c.loanAmount();
        this.installmentsQty = c.installmentsQty();
        this.startDate = c.startDate();
        this.fixedInstallment = c.fixedInstallment();
        this.npvDebtor = c.npvDebtor();
        this.irrDebtor = c.irrDebtor();
        this.tcea = c.tcea();
        this.trea = c.trea();
        this.totalInterest = c.totalInterest();
        this.totalInsurance = c.totalInsurance();
        this.totalRiskInsurance = c.totalRiskInsurance();
        this.totalGps = c.totalGps();
        this.totalPostage = c.totalPostage();
        this.totalCommission = c.totalCommission();
        this.totalTax = c.totalTax();
        this.initialCosts = c.initialCosts();
        this.residualValue = c.residualValue();
        this.ctc = c.ctc();
    }

    public String getCarId() { return carId; }
    public List<LoanVehicle> getVehicles() { return vehicles; }
    public String getClientId() { return clientId; }
    public Long getConfigId() { return configId; }
    public Long getCompanyId() { return companyId; }
    public Long getSellerId() { return sellerId; }
    public String getStatus() { return status; }
    public double getInitialFee() { return initialFee; }
    public double getVehiclePrice() { return vehiclePrice; }
    public double getLoanAmount() { return loanAmount; }
    public int getInstallmentsQty() { return installmentsQty; }
    public Instant getStartDate() { return startDate; }
    public double getFixedInstallment() { return fixedInstallment; }
    public double getNpvDebtor() { return npvDebtor; }
    public double getIrrDebtor() { return irrDebtor; }
    public double getTcea() { return tcea; }
    public double getTrea() { return trea; }
    public double getTotalInterest() { return totalInterest; }
    public double getTotalInsurance() { return totalInsurance; }
    public double getTotalRiskInsurance() { return totalRiskInsurance; }
    public double getTotalGps() { return totalGps; }
    public double getTotalPostage() { return totalPostage; }
    public double getTotalCommission() { return totalCommission; }
    public double getTotalTax() { return totalTax; }
    public double getInitialCosts() { return initialCosts; }
    public double getResidualValue() { return residualValue; }
    public double getCtc() { return ctc; }
}
