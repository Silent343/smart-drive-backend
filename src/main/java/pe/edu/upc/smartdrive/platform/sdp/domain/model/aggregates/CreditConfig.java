package pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.UpdateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/**
 * Credit configuration aggregate (SDP bounded context).
 *
 * <p>Holds the financial parameters of a credit line: currency, interest rate type and
 * value, capitalization, grace period and the recurring fees. The interest-rate
 * conversion rules (nominal to effective annual, and effective annual to effective
 * monthly) live here as domain behaviour, mirroring the frontend's CreditConfig.</p>
 */
@Entity
public class CreditConfig extends AuditableAbstractAggregateRoot<CreditConfig> {

    @Column(nullable = false)
    private String currency;            // PEN | USD

    @Column(nullable = false)
    private String interestRateType;    // efectiva | nominal

    @Column(nullable = false)
    private double annualRate;

    private Integer capitalization;     // nullable; only relevant for nominal rates

    @Column(nullable = false)
    private String gracePeriodType;     // none | partial | total

    @Column(nullable = false)
    private int gracePeriodMonths;

    @Column(nullable = false)
    private double insuranceRatePct;

    @Column(nullable = false)
    private double postageFeeAmount;

    @Column(nullable = false)
    private double administrationFeePct;

    protected CreditConfig() {
    }

    public CreditConfig(CreateCreditConfigCommand command) {
        this.currency = command.currency();
        this.interestRateType = command.interestRateType();
        this.annualRate = command.annualRate();
        this.capitalization = command.capitalization();
        this.gracePeriodType = command.gracePeriodType();
        this.gracePeriodMonths = command.gracePeriodMonths();
        this.insuranceRatePct = command.insuranceRatePct();
        this.postageFeeAmount = command.postageFeeAmount();
        this.administrationFeePct = command.administrationFeePct();
    }

    public CreditConfig updateWith(UpdateCreditConfigCommand command) {
        this.currency = command.currency();
        this.interestRateType = command.interestRateType();
        this.annualRate = command.annualRate();
        this.capitalization = command.capitalization();
        this.gracePeriodType = command.gracePeriodType();
        this.gracePeriodMonths = command.gracePeriodMonths();
        this.insuranceRatePct = command.insuranceRatePct();
        this.postageFeeAmount = command.postageFeeAmount();
        this.administrationFeePct = command.administrationFeePct();
        return this;
    }

    /** Converts a nominal annual rate to an effective annual rate; returns it directly when already effective. */
    public double effectiveAnnualRate() {
        if ("efectiva".equals(interestRateType)) return annualRate;
        int m = capitalization != null ? capitalization : 12;
        return (Math.pow(1 + annualRate / 100 / m, m) - 1) * 100;
    }

    /** Effective monthly rate (TEM) derived from the effective annual rate. */
    public double monthlyRate() {
        return Math.pow(1 + effectiveAnnualRate() / 100, 1.0 / 12) - 1;
    }

    public String getCurrency() { return currency; }
    public String getInterestRateType() { return interestRateType; }
    public double getAnnualRate() { return annualRate; }
    public Integer getCapitalization() { return capitalization; }
    public String getGracePeriodType() { return gracePeriodType; }
    public int getGracePeriodMonths() { return gracePeriodMonths; }
    public double getInsuranceRatePct() { return insuranceRatePct; }
    public double getPostageFeeAmount() { return postageFeeAmount; }
    public double getAdministrationFeePct() { return administrationFeePct; }
}
