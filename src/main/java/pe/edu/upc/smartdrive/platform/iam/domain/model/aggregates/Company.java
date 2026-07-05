package pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import pe.edu.upc.smartdrive.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

/**
 * Company aggregate: the tenant of the platform. Each company is created by exactly one
 * administrator during public sign-up, owns a globally-unique {@code domain} used to build
 * its sellers' usernames, caps how many sellers can be created ({@code maxWorkers}), and
 * holds the default bank values (rates, insurance, fees, taxes) that every simulation run by
 * its sellers inherits and cannot override.
 */
@Entity
@Table(name = "companies")
public class Company extends AuditableAbstractAggregateRoot<Company> {

    /** Legal / commercial name of the company. */
    @Column(nullable = false)
    private String name;

    /** Peruvian tax id. */
    @Column(nullable = false, length = 11)
    private String ruc;

    /**
     * Slug chosen by the admin, unique across the platform. Used to build seller usernames
     * as {@code domain-code} (e.g. "toyota-lima" + "V001" -> "toyota-lima-V001").
     */
    @Column(nullable = false, unique = true)
    private String domain;

    /** Maximum number of sellers this company may register. */
    @Column(nullable = false)
    private int maxWorkers;

    // ---- Bank default values (inherited by every seller simulation) ----
    @Column(nullable = false) private String defaultCurrency;          // PEN | USD
    @Column(nullable = false) private String defaultInterestRateType;  // efectiva | nominal
    @Column(nullable = false) private double defaultAnnualRate;        // e.g. 9.5
    @Column(nullable = false) private double defaultInsuranceRatePct;  // desgravamen % monthly
    @Column(nullable = false) private double defaultRiskInsuranceAmount; // seguro contra todo riesgo (periodic)
    @Column(nullable = false) private double defaultGpsFeeAmount;      // GPS monthly
    @Column(nullable = false) private double defaultPostageFeeAmount;  // portes
    @Column(nullable = false) private double defaultAdministrationFeePct;
    @Column(nullable = false) private double defaultIgvItfPct;         // IGV/ITF over fees & insurance
    @Column(nullable = false) private double defaultStudyCommissionAmount;
    @Column(nullable = false) private double defaultActivationCommissionAmount;

    protected Company() { }

    public Company(String name, String ruc, String domain, int maxWorkers) {
        this.name = name;
        this.ruc = ruc;
        this.domain = normalizeDomain(domain);
        this.maxWorkers = Math.max(1, maxWorkers);
        // sensible starting defaults; the admin edits them from the profile screen
        this.defaultCurrency = "PEN";
        this.defaultInterestRateType = "efectiva";
        this.defaultAnnualRate = 9.5;
        this.defaultInsuranceRatePct = 0.05;
        this.defaultRiskInsuranceAmount = 0.0;
        this.defaultGpsFeeAmount = 0.0;
        this.defaultPostageFeeAmount = 3.5;
        this.defaultAdministrationFeePct = 0.0;
        this.defaultIgvItfPct = 18.0;
        this.defaultStudyCommissionAmount = 0.0;
        this.defaultActivationCommissionAmount = 0.0;
    }

    /** Lowercases and trims the domain, replacing spaces with hyphens. */
    public static String normalizeDomain(String raw) {
        if (raw == null) throw new IllegalArgumentException("Domain is required");
        String slug = raw.trim().toLowerCase().replaceAll("\\s+", "-").replaceAll("[^a-z0-9-]", "");
        if (slug.isBlank()) throw new IllegalArgumentException("Domain must contain letters or digits");
        return slug;
    }

    /** Replaces the bank default values (admin action). */
    public void updateBankDefaults(String currency, String interestRateType, double annualRate,
                                   double insuranceRatePct, double riskInsuranceAmount, double gpsFeeAmount,
                                   double postageFeeAmount, double administrationFeePct, double igvItfPct,
                                   double studyCommissionAmount, double activationCommissionAmount) {
        this.defaultCurrency = currency;
        this.defaultInterestRateType = interestRateType;
        this.defaultAnnualRate = annualRate;
        this.defaultInsuranceRatePct = insuranceRatePct;
        this.defaultRiskInsuranceAmount = riskInsuranceAmount;
        this.defaultGpsFeeAmount = gpsFeeAmount;
        this.defaultPostageFeeAmount = postageFeeAmount;
        this.defaultAdministrationFeePct = administrationFeePct;
        this.defaultIgvItfPct = igvItfPct;
        this.defaultStudyCommissionAmount = studyCommissionAmount;
        this.defaultActivationCommissionAmount = activationCommissionAmount;
    }

    public String getName() { return name; }
    public String getRuc() { return ruc; }
    public String getDomain() { return domain; }
    public int getMaxWorkers() { return maxWorkers; }
    public String getDefaultCurrency() { return defaultCurrency; }
    public String getDefaultInterestRateType() { return defaultInterestRateType; }
    public double getDefaultAnnualRate() { return defaultAnnualRate; }
    public double getDefaultInsuranceRatePct() { return defaultInsuranceRatePct; }
    public double getDefaultRiskInsuranceAmount() { return defaultRiskInsuranceAmount; }
    public double getDefaultGpsFeeAmount() { return defaultGpsFeeAmount; }
    public double getDefaultPostageFeeAmount() { return defaultPostageFeeAmount; }
    public double getDefaultAdministrationFeePct() { return defaultAdministrationFeePct; }
    public double getDefaultIgvItfPct() { return defaultIgvItfPct; }
    public double getDefaultStudyCommissionAmount() { return defaultStudyCommissionAmount; }
    public double getDefaultActivationCommissionAmount() { return defaultActivationCommissionAmount; }
}
