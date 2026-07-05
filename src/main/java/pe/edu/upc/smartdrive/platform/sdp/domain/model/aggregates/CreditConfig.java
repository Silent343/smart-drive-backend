package pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.UpdateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/**
 * Credit configuration aggregate (SDP bounded context).
 *
 * <p>Holds the financial parameters of a credit line following the Interbank-style French
 * method spreadsheet: currency, interest rate type/value, capitalization, grace period,
 * the residual value of the "Compra Inteligente" modality and the full set of initial and
 * recurring costs (desgravamen and all-risk insurance, GPS, postage, administration and
 * study/activation commissions, notary/registry costs) plus the IGV/ITF tax applied over
 * fees and insurance. The rate-conversion rules live here as domain behaviour.</p>
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

    // ---- Recurring costs (charged every installment) ----
    /** Kept for backward compatibility: desgravamen insurance rate (% over balance). */
    @Column(nullable = false)
    private double insuranceRatePct;

    /** All-risk insurance rate (% over balance), monthly. */
    @Column(nullable = false)
    private double riskInsuranceRatePct;

    /** Fixed GPS fee charged every installment. */
    @Column(nullable = false)
    private double gpsFeeAmount;

    /** Fixed postage/shipping fee charged every installment. */
    @Column(nullable = false)
    private double postageFeeAmount;

    /** Administration fee (% over balance) charged every installment. */
    @Column(nullable = false)
    private double administrationFeePct;

    // ---- Residual value (Compra Inteligente) ----
    /** Final balloon installment as a percentage of the vehicle price (0..50). */
    @Column(nullable = false)
    private double finalInstallmentPct;

    // ---- Taxes ----
    /** IGV/ITF tax (%) applied over fees and insurance amounts. */
    @Column(nullable = false)
    private double igvItfPct;

    // ---- Initial (one-off) costs ----
    @Column(nullable = false) private double notaryCostAmount;
    @Column(nullable = false) private double registryCostAmount;
    @Column(nullable = false) private double appraisalCostAmount;      // tasación
    @Column(nullable = false) private double studyCommissionAmount;    // comisión de estudio
    @Column(nullable = false) private double activationCommissionAmount; // comisión de activación

    // ---- Opportunity cost (for TREA / investor view) ----
    /** Annual discount rate (COK) used for the investor NPV/TREA, as a percentage. */
    @Column(nullable = false)
    private double discountAnnualRatePct;

    protected CreditConfig() {
    }

    public CreditConfig(CreateCreditConfigCommand c) {
        this.currency = c.currency();
        this.interestRateType = c.interestRateType();
        this.annualRate = c.annualRate();
        this.capitalization = c.capitalization();
        this.gracePeriodType = c.gracePeriodType();
        this.gracePeriodMonths = c.gracePeriodMonths();
        this.insuranceRatePct = c.insuranceRatePct();
        this.postageFeeAmount = c.postageFeeAmount();
        this.administrationFeePct = c.administrationFeePct();
        // new fields (nullable-safe defaults so older callers keep compiling)
        this.riskInsuranceRatePct = c.riskInsuranceRatePct();
        this.gpsFeeAmount = c.gpsFeeAmount();
        this.finalInstallmentPct = c.finalInstallmentPct();
        this.igvItfPct = c.igvItfPct();
        this.notaryCostAmount = c.notaryCostAmount();
        this.registryCostAmount = c.registryCostAmount();
        this.appraisalCostAmount = c.appraisalCostAmount();
        this.studyCommissionAmount = c.studyCommissionAmount();
        this.activationCommissionAmount = c.activationCommissionAmount();
        this.discountAnnualRatePct = c.discountAnnualRatePct();
    }

    public CreditConfig updateWith(UpdateCreditConfigCommand c) {
        this.currency = c.currency();
        this.interestRateType = c.interestRateType();
        this.annualRate = c.annualRate();
        this.capitalization = c.capitalization();
        this.gracePeriodType = c.gracePeriodType();
        this.gracePeriodMonths = c.gracePeriodMonths();
        this.insuranceRatePct = c.insuranceRatePct();
        this.postageFeeAmount = c.postageFeeAmount();
        this.administrationFeePct = c.administrationFeePct();
        this.riskInsuranceRatePct = c.riskInsuranceRatePct();
        this.gpsFeeAmount = c.gpsFeeAmount();
        this.finalInstallmentPct = c.finalInstallmentPct();
        this.igvItfPct = c.igvItfPct();
        this.notaryCostAmount = c.notaryCostAmount();
        this.registryCostAmount = c.registryCostAmount();
        this.appraisalCostAmount = c.appraisalCostAmount();
        this.studyCommissionAmount = c.studyCommissionAmount();
        this.activationCommissionAmount = c.activationCommissionAmount();
        this.discountAnnualRatePct = c.discountAnnualRatePct();
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

    /** Sum of the one-off initial costs added to the financed operation at t=0. */
    public double totalInitialCosts() {
        return notaryCostAmount + registryCostAmount + appraisalCostAmount
                + studyCommissionAmount + activationCommissionAmount;
    }

    public String getCurrency() { return currency; }
    public String getInterestRateType() { return interestRateType; }
    public double getAnnualRate() { return annualRate; }
    public Integer getCapitalization() { return capitalization; }
    public String getGracePeriodType() { return gracePeriodType; }
    public int getGracePeriodMonths() { return gracePeriodMonths; }
    public double getInsuranceRatePct() { return insuranceRatePct; }
    public double getRiskInsuranceRatePct() { return riskInsuranceRatePct; }
    public double getGpsFeeAmount() { return gpsFeeAmount; }
    public double getPostageFeeAmount() { return postageFeeAmount; }
    public double getAdministrationFeePct() { return administrationFeePct; }
    public double getFinalInstallmentPct() { return finalInstallmentPct; }
    public double getIgvItfPct() { return igvItfPct; }
    public double getNotaryCostAmount() { return notaryCostAmount; }
    public double getRegistryCostAmount() { return registryCostAmount; }
    public double getAppraisalCostAmount() { return appraisalCostAmount; }
    public double getStudyCommissionAmount() { return studyCommissionAmount; }
    public double getActivationCommissionAmount() { return activationCommissionAmount; }
    public double getDiscountAnnualRatePct() { return discountAnnualRatePct; }
}
