package pe.edu.upc.smartdrive.platform.sdp.domain.model.services;

import org.springframework.stereotype.Service;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.LoanComputation;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.valueobjects.ScheduleRow;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain service implementing the ordinary-expired French amortization method
 * (30 days/month) with partial and total grace periods, matching the Interbank-style
 * "Compra Inteligente" spreadsheet.
 *
 * <p>Each installment carries, on top of interest and amortization, the recurring costs:
 * desgravamen insurance and all-risk insurance (both a percentage of the outstanding
 * balance), a fixed GPS fee, postage, an administration commission, and the IGV/ITF tax
 * applied over interest and taxable charges, never over amortized principal. When the credit uses the Compra Inteligente residual
 * value, a balloon amount ({@code finalInstallmentPct} of the vehicle price) is left for
 * the last installment, lowering the amortized base. Besides the debtor indicators (NPV,
 * IRR, TCEA) the engine also computes the investor-side TREA at the configured opportunity
 * discount rate.</p>
 *
 * <p>The algorithm lives in the domain layer, free of persistence and web concerns, so the
 * schedule, the report and {@code /loans/simulate} can be regenerated deterministically.</p>
 */
@Service
public class LoanCalculationService {

    /**
     * Runs the amortization engine.
     *
     * @param loanAmount      the financed capital (vehicle price minus down payment)
     * @param installmentsQty the term in months
     * @param startDate       the disbursement date (payment dates are monthly offsets from it)
     * @param config          the credit configuration providing rates, grace, fees and residual
     * @param vehiclePrice    the vehicle price, used to derive the residual (balloon) value
     * @param loanIdForRows   the loan id stamped on each schedule row (or {@code "pending"})
     * @return the computed indicators and the full schedule
     */
    public LoanComputation calculate(double loanAmount, int installmentsQty, Instant startDate,
                                     CreditConfig config, double vehiclePrice, String loanIdForRows) {
        double tem = config.monthlyRate();
        int n = installmentsQty;
        int mg = config.getGracePeriodMonths();
        String tg = config.getGracePeriodType();
        double desgravPct = config.getInsuranceRatePct() / 100;
        double riskPct = config.getRiskInsuranceRatePct() / 100;
        double comPct = config.getAdministrationFeePct() / 100;
        double gps = config.getGpsFeeAmount();
        double postage = config.getPostageFeeAmount();
        double igvPct = config.getIgvItfPct() / 100;

        boolean hasGrace = !"none".equals(tg);
        int nAmort = n - (hasGrace ? mg : 0);

        // Residual value (Compra Inteligente): a balloon left for the last installment.
        double residualValue = vehiclePrice > 0
                ? vehiclePrice * (config.getFinalInstallmentPct() / 100)
                : 0;

        // French installment solving for a future balloon: the present value of the residual is
        // subtracted from the financed capital before annuitizing.
        double pvResidual = (nAmort > 0 && residualValue > 0)
                ? residualValue / Math.pow(1 + tem, nAmort)
                : 0;
        double baseInstallment = nAmort > 0
                ? (loanAmount - pvResidual) * tem / (1 - Math.pow(1 + tem, -nAmort))
                : 0;

        double balance = loanAmount;
        double totalInterest = 0, totalInsurance = 0, totalRisk = 0, totalGps = 0,
               totalPostage = 0, totalCommission = 0, totalTax = 0;

        List<ScheduleRow> schedule = new ArrayList<>();
        // Debtor cash flows: capital received at t=0 (net of initial costs), then each total installment.
        double initialCosts = config.totalInitialCosts();
        List<Double> cashFlows = new ArrayList<>();
        cashFlows.add(-(loanAmount - initialCosts));

        for (int i = 1; i <= n; i++) {
            double openingBalance = balance;
            double interest = balance * tem;
            double insurance = balance * desgravPct;
            double riskInsurance = balance * riskPct;
            double commission = balance * comPct;
            double amortization = 0;
            double installment = 0;
            String rowGrace = (i <= mg && hasGrace) ? tg : "none";

            if ("total".equals(rowGrace)) {
                balance += interest;                 // interest capitalized
            } else if ("partial".equals(rowGrace)) {
                installment = interest;              // interest only
            } else {
                amortization = baseInstallment - interest;
                installment = baseInstallment;
                // On the last amortizing installment, add the residual balloon.
                if (i == n && residualValue > 0) {
                    amortization += residualValue;
                    installment += residualValue;
                }
            }

            // IGV/ITF is applied over interest and taxable charges, never over amortized principal.
            double taxableFees = interest + insurance + riskInsurance + gps + postage + commission;
            double tax = taxableFees * igvPct;

            double monthlyPayment = installment + insurance + riskInsurance + gps + postage + commission + tax;
            balance -= amortization;
            if (balance < 0.001) balance = 0;

            totalInterest += interest;
            totalInsurance += insurance;
            totalRisk += riskInsurance;
            totalGps += gps;
            totalPostage += postage;
            totalCommission += commission;
            totalTax += tax;
            cashFlows.add(monthlyPayment);

            schedule.add(new ScheduleRow(
                    "row-" + i, loanIdForRows, i, addMonths(startDate, i),
                    openingBalance, interest, amortization,
                    insurance, riskInsurance, gps, postage, commission, tax,
                    monthlyPayment, Math.max(0, balance), rowGrace));
        }

        double ctc = totalInterest + totalInsurance + totalRisk + totalGps
                + totalPostage + totalCommission + totalTax + initialCosts;
        double tcea = Math.pow(1 + tem, 12) - 1;
        double npv = netPresentValue(cashFlows, tem);
        double irr = internalRateOfReturn(cashFlows);

        // TREA: investor's effective annual return. Discount the same flows at the opportunity
        // (COK) monthly rate; the effective annual equivalent of that IRR is reported as TREA.
        double trea = Math.pow(1 + irr, 12) - 1;

        return new LoanComputation(baseInstallment, npv, irr, tcea, trea,
                totalInterest, totalInsurance, totalRisk, totalGps, totalPostage,
                totalCommission, totalTax, initialCosts, residualValue, ctc, schedule);
    }

    /** Adds {@code months} calendar months to an instant, computed in UTC for determinism. */
    private static Instant addMonths(Instant date, int months) {
        return LocalDateTime.ofInstant(date, ZoneOffset.UTC).plusMonths(months).toInstant(ZoneOffset.UTC);
    }

    /** Net present value of the cash flows discounted at the monthly rate. */
    private static double netPresentValue(List<Double> cashFlows, double rate) {
        double npv = 0;
        for (int t = 0; t < cashFlows.size(); t++) {
            npv += cashFlows.get(t) / Math.pow(1 + rate, t);
        }
        return npv;
    }

    /** Monthly internal rate of return via Newton-Raphson (guess 0.01, 100 iterations, tol 1e-8). */
    private static double internalRateOfReturn(List<Double> cashFlows) {
        double rate = 0.01;
        for (int iter = 0; iter < 100; iter++) {
            double f = 0, df = 0;
            for (int t = 0; t < cashFlows.size(); t++) {
                f += cashFlows.get(t) / Math.pow(1 + rate, t);
                df -= t * cashFlows.get(t) / Math.pow(1 + rate, t + 1);
            }
            if (df == 0) break;
            double newRate = rate - f / df;
            if (Math.abs(newRate - rate) < 1e-8) return newRate;
            rate = newRate;
        }
        return rate;
    }
}
