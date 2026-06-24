package pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateLoanCommand;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import java.time.Instant;

/**
 * Loan aggregate (SDP bounded context).
 *
 * <p>Stores a confirmed vehicle credit: its inputs (vehicle, client, configuration,
 * amounts, term and start date) and the financial indicators produced by the French
 * amortization engine (fixed installment, NPV, IRR, TCEA and cost totals). The
 * indicators are computed by {@code LoanCalculationService} before persistence.</p>
 */
@Entity
public class Loan extends AuditableAbstractAggregateRoot<Loan> {

    private String carId;       // FK -> ARM vehicle
    private String clientId;    // FK -> ARM client
    private Long configId;      // FK -> CreditConfig

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
    private double totalInterest;
    private double totalInsurance;
    private double totalPostage;
    private double totalCommission;
    private double ctc;

    protected Loan() {
    }

    public Loan(CreateLoanCommand command) {
        this.carId = command.carId();
        this.clientId = command.clientId();
        this.configId = command.configId();
        this.initialFee = command.initialFee();
        this.vehiclePrice = command.vehiclePrice();
        this.loanAmount = command.loanAmount();
        this.installmentsQty = command.installmentsQty();
        this.startDate = command.startDate();
        this.fixedInstallment = command.fixedInstallment();
        this.npvDebtor = command.npvDebtor();
        this.irrDebtor = command.irrDebtor();
        this.tcea = command.tcea();
        this.totalInterest = command.totalInterest();
        this.totalInsurance = command.totalInsurance();
        this.totalPostage = command.totalPostage();
        this.totalCommission = command.totalCommission();
        this.ctc = command.ctc();
    }

    public String getCarId() { return carId; }
    public String getClientId() { return clientId; }
    public Long getConfigId() { return configId; }
    public double getInitialFee() { return initialFee; }
    public double getVehiclePrice() { return vehiclePrice; }
    public double getLoanAmount() { return loanAmount; }
    public int getInstallmentsQty() { return installmentsQty; }
    public Instant getStartDate() { return startDate; }
    public double getFixedInstallment() { return fixedInstallment; }
    public double getNpvDebtor() { return npvDebtor; }
    public double getIrrDebtor() { return irrDebtor; }
    public double getTcea() { return tcea; }
    public double getTotalInterest() { return totalInterest; }
    public double getTotalInsurance() { return totalInsurance; }
    public double getTotalPostage() { return totalPostage; }
    public double getTotalCommission() { return totalCommission; }
    public double getCtc() { return ctc; }
}
