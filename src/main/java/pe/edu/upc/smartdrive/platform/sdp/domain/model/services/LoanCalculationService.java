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
 * (30 days/month) with partial and total grace periods.
 *
 * <p>This is a faithful port of the frontend's {@code calculateFrench} engine, kept on
 * the server so that the schedule, the report and the {@code /loans/simulate} endpoint
 * can be regenerated deterministically from a loan's inputs and its credit
 * configuration. Keeping the algorithm in the domain layer (free of persistence and web
 * concerns) follows the single-responsibility and dependency-inversion principles.</p>
 */
@Service
public class LoanCalculationService {

    /**
     * Runs the amortization engine.
     *
     * @param loanAmount      the financed capital (vehicle price minus down payment)
     * @param installmentsQty the term in months
     * @param startDate       the disbursement date (payment dates are monthly offsets from it)
     * @param config          the credit configuration providing rates, grace and fees
     * @param loanIdForRows   the loan id stamped on each schedule row (or {@code "pending"})
     * @return the computed indicators and the full schedule
     */
    public LoanComputation calculate(double loanAmount, int installmentsQty, Instant startDate,
                                     CreditConfig config, String loanIdForRows) {
        double tem = config.monthlyRate();
        int n = installmentsQty;
        int mg = config.getGracePeriodMonths();
        String tg = config.getGracePeriodType();
        double insPct = config.getInsuranceRatePct() / 100;
        double postage = config.getPostageFeeAmount();
        double comPct = config.getAdministrationFeePct() / 100;

        boolean hasGrace = !"none".equals(tg);
        int nAmort = n - (hasGrace ? mg : 0);
        double baseInstallment = nAmort > 0
                ? loanAmount * tem / (1 - Math.pow(1 + tem, -nAmort))
                : 0;

        double balance = loanAmount;
        double totalInterest = 0, totalInsurance = 0, totalPostage = 0, totalCommission = 0;

        List<ScheduleRow> schedule = new ArrayList<>();
        // Debtor cash flows: capital received at t=0 (negative), then each total installment.
        List<Double> cashFlows = new ArrayList<>();
        cashFlows.add(-loanAmount);

        for (int i = 1; i <= n; i++) {
            double openingBalance = balance;
            double interest = balance * tem;
            double insurance = balance * insPct;
            double commission = balance * comPct;
            double amortization = 0;
            double installment = 0;
            String rowGrace = (i <= mg && hasGrace) ? tg : "none";

            if ("total".equals(rowGrace)) {
                // Pays nothing; interest is capitalized into the balance.
                balance += interest;
            } else if ("partial".equals(rowGrace)) {
                // Pays interest only; no capital amortization.
                installment = interest;
            } else {
                amortization = baseInstallment - interest;
                installment = baseInstallment;
            }

            double monthlyPayment = installment + insurance + postage + commission;
            balance -= amortization;
            if (balance < 0.001) balance = 0;

            totalInterest += interest;
            totalInsurance += insurance;
            totalPostage += postage;
            totalCommission += commission;
            cashFlows.add(monthlyPayment);

            schedule.add(new ScheduleRow(
                    "row-" + i,
                    loanIdForRows,
                    i,
                    addMonths(startDate, i),
                    openingBalance,
                    interest,
                    amortization,
                    insurance,
                    postage,
                    commission,
                    monthlyPayment,
                    Math.max(0, balance),
                    rowGrace
            ));
        }

        double ctc = totalInterest + totalInsurance + totalPostage + totalCommission;
        double tcea = Math.pow(1 + tem, 12) - 1;
        double npv = netPresentValue(cashFlows, tem);
        double irr = internalRateOfReturn(cashFlows);

        return new LoanComputation(baseInstallment, npv, irr, tcea,
                totalInterest, totalInsurance, totalPostage, totalCommission, ctc, schedule);
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
            double newRate = rate - f / df;
            if (Math.abs(newRate - rate) < 1e-8) return newRate;
            rate = newRate;
        }
        return rate;
    }
}
