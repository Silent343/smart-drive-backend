package pe.edu.upc.smartdrive.platform.shared.infrastructure.seeding;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Client;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.Vehicle;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleCommercial;
import pe.edu.upc.smartdrive.platform.arm.domain.model.aggregates.VehicleSpecification;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateClientCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleCommercialCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.model.commands.CreateVehicleSpecificationCommand;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.ClientRepository;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.VehicleCommercialRepository;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.VehicleRepository;
import pe.edu.upc.smartdrive.platform.arm.domain.repositories.VehicleSpecificationRepository;
import pe.edu.upc.smartdrive.platform.iam.application.internal.outboundservices.hashing.HashingService;
import pe.edu.upc.smartdrive.platform.iam.domain.model.aggregates.User;
import pe.edu.upc.smartdrive.platform.iam.domain.model.commands.SignUpCommand;
import pe.edu.upc.smartdrive.platform.iam.domain.repositories.UserRepository;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.CreditConfig;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.aggregates.Loan;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateCreditConfigCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.model.commands.CreateLoanCommand;
import pe.edu.upc.smartdrive.platform.sdp.domain.repositories.CreditConfigRepository;
import pe.edu.upc.smartdrive.platform.sdp.domain.repositories.LoanRepository;

import java.time.Instant;

/**
 * Seeds the demo data used by the SmartDrive frontend (the records originally served by
 * json-server's {@code db.json}). It runs once on startup and is idempotent: each section
 * is skipped when its table already contains rows, so restarts and the MySQL profile keep
 * working without duplicates.
 *
 * <p>The demo administrator is {@code fernand04@gmail.com} / {@code Sukerj$23}, with TOTP
 * pre-enabled using the known secret {@code GVLHIWCSIZWEGOT2IAUFISBJMV4GSRS3} so the 2FA
 * flow can be exercised end-to-end.</p>
 */
@Component
@Order(1)
public class DemoDataSeeder implements CommandLineRunner {

    private static final String DEMO_TOTP_SECRET = "GVLHIWCSIZWEGOT2IAUFISBJMV4GSRS3";
    private static final String VEHICLE_ID = "CCrX7ej";
    private static final String CLIENT_ID = "zzxasas";

    private final UserRepository userRepository;
    private final HashingService hashingService;
    private final CreditConfigRepository creditConfigRepository;
    private final VehicleRepository vehicleRepository;
    private final VehicleSpecificationRepository vehicleSpecificationRepository;
    private final VehicleCommercialRepository vehicleCommercialRepository;
    private final ClientRepository clientRepository;
    private final LoanRepository loanRepository;

    public DemoDataSeeder(UserRepository userRepository,
                          HashingService hashingService,
                          CreditConfigRepository creditConfigRepository,
                          VehicleRepository vehicleRepository,
                          VehicleSpecificationRepository vehicleSpecificationRepository,
                          VehicleCommercialRepository vehicleCommercialRepository,
                          ClientRepository clientRepository,
                          LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
        this.creditConfigRepository = creditConfigRepository;
        this.vehicleRepository = vehicleRepository;
        this.vehicleSpecificationRepository = vehicleSpecificationRepository;
        this.vehicleCommercialRepository = vehicleCommercialRepository;
        this.clientRepository = clientRepository;
        this.loanRepository = loanRepository;
    }

    @Override
    public void run(String... args) {
        seedUser();
        seedCreditConfigs();
        seedVehicleCatalog();
        seedClients();
        seedLoans();
    }

    private void seedUser() {
        if (userRepository.count() > 0) return;
        var command = new SignUpCommand("fernand04@gmail.com", "Sukerj$23", "Fernando",
                "51351414", "20988317377", "983772888", "Gird");
        var user = new User(command, hashingService.encode(command.password()));
        user.enableTotpWithSecret(DEMO_TOTP_SECRET);
        userRepository.save(user);
    }

    private void seedCreditConfigs() {
        if (creditConfigRepository.count() > 0) return;
        // id 1
        creditConfigRepository.save(new CreditConfig(new CreateCreditConfigCommand(
                "USD", "efectiva", 9.5, null, "partial", 5, 0.05, 3.5, 0.9)));
        // id 2
        creditConfigRepository.save(new CreditConfig(new CreateCreditConfigCommand(
                "PEN", "efectiva", 9.5, null, "partial", 4, 0.05, 3.5, 2)));
        // id 3
        creditConfigRepository.save(new CreditConfig(new CreateCreditConfigCommand(
                "PEN", "efectiva", 9.5, null, "none", 5, 0.05, 3.5, 2)));
        // id 4
        creditConfigRepository.save(new CreditConfig(new CreateCreditConfigCommand(
                "PEN", "efectiva", 9.5, null, "partial", 4, 0.05, 3.5, 2)));
        // id 5
        creditConfigRepository.save(new CreditConfig(new CreateCreditConfigCommand(
                "PEN", "efectiva", 9.5, null, "partial", 4, 0.05, 3.5, 2)));
    }

    private void seedVehicleCatalog() {
        if (vehicleRepository.count() == 0) {
            var vehicle = new Vehicle(new CreateVehicleCommand("VH-011", "Disponible",
                    "https://www.toyotaperu.com.pe/sites/default/files/2024-11/img-video-modelos-web-corolla2_2.jpg"));
            vehicle.setId(VEHICLE_ID);
            vehicleRepository.save(vehicle);
        }
        if (vehicleSpecificationRepository.count() == 0) {
            var spec = new VehicleSpecification(new CreateVehicleSpecificationCommand(
                    VEHICLE_ID, "toyota", "corlla", 2026, "Automática"));
            spec.setId("wwKugze");
            vehicleSpecificationRepository.save(spec);
        }
        if (vehicleCommercialRepository.count() == 0) {
            var commercial = new VehicleCommercial(new CreateVehicleCommercialCommand(
                    VEHICLE_ID, "1", 123123.0, "Vitel"));
            commercial.setId("gr7R_pk");
            vehicleCommercialRepository.save(commercial);
        }
    }

    private void seedClients() {
        if (clientRepository.count() > 0) return;
        var juan = new Client(new CreateClientCommand("1", "Juan Pérez", "71234567", 3500.0,
                "Ingeniero de Software", "+51 987654321", VEHICLE_ID));
        juan.setId(CLIENT_ID);
        clientRepository.save(juan);

        var second = new Client(new CreateClientCommand("1", "qweqwe", "1312312312", 312310.0,
                "eqweqweqw", "+51 4124131231", ""));
        second.setId("z1b8djX");
        clientRepository.save(second);
    }

    private void seedLoans() {
        if (loanRepository.count() > 0) return;
        var d0604 = Instant.parse("2026-06-04T00:00:00.000Z");
        var d0610 = Instant.parse("2026-06-10T00:00:00.000Z");
        var d0615 = Instant.parse("2026-06-15T00:00:00.000Z");
        // The indicators below are the values originally persisted by the frontend demo.
        save(1L, 12, 123123, 123111, 32, d0604, 4897.270691533278, 22607.436262185878, 0.01813567848767751,
                0.09499999999999909, 17750.984875123446, 1169.130257182916, 112, 23382.605143658315, 42414.720275964675);
        save(1L, 5, 32832.8, 32827.8, 12, d0604, 4244.894321805226, 2873.3682406667563, 0.018237631636929562,
                0.09499999999999909, 2128.2080519793703, 140.169824077555, 42, 2803.396481551101, 5113.774357608027);
        save(1L, 1422, 32832.8, 31410.800000000003, 12, d0610, 4624.548280455124, 2630.656770327212, 0.017236512591993932,
                0.09499999999999909, 2153.3187896590307, 141.82368854806205, 42, 2552.8263938651176, 4889.96887207221);
        save(1L, 2222, 32832.8, 30610.800000000003, 24, d0615, 1736.1755582578837, 4213.990691688132, 0.017261938664430116,
                0.09499999999999909, 3538.4502962106017, 233.05238182221683, 84, 4194.942872799904, 8050.445550832723);
        save(2L, 2222, 123123, 120901, 36, d0615, 3850.8030967585364, 43611.02152223326, 0.028138408893970342,
                0.09499999999999909, 17727.91148330733, 1167.610578094807, 126, 46704.42312379226, 65725.94518519439);
        save(3L, 1222, 123123, 121901, 14, d0615, 9211.09472723049, 18342.643944918025, 0.02814134023226707,
                0.09499999999999909, 7054.326181226864, 464.6179488366323, 49, 18584.7179534653, 26152.662083528798);
        save(5L, 1222, 123123, 121901, 36, d0615, 4305.20070637619, 48071.95835984482, 0.028134179016159108,
                0.09499999999999909, 19567.085090263292, 1288.7437730826434, 126, 51549.75092330572, 72531.57978665165);
        save(5L, 1222, 123123, 121901, 12, d0615, 15762.76395988701, 20581.55039262877, 0.02813037077954885,
                0.09499999999999909, 7902.774165321382, 520.4991417298155, 42, 20819.965669192625, 29285.23897624382);
        save(5L, 1844, 123123, 121279, 12, d0615, 15682.33443770877, 20476.737101246847, 0.028130569949075285,
                0.09499999999999909, 7862.450250580486, 517.843294229336, 42, 20713.73176917344, 29136.02531398326);
    }

    private void save(Long configId, double initialFee, double vehiclePrice, double loanAmount, int installmentsQty,
                      Instant startDate, double fixedInstallment, double npvDebtor, double irrDebtor, double tcea,
                      double totalInterest, double totalInsurance, double totalPostage, double totalCommission, double ctc) {
        loanRepository.save(new Loan(new CreateLoanCommand(VEHICLE_ID, CLIENT_ID, configId, initialFee, vehiclePrice,
                loanAmount, installmentsQty, startDate, fixedInstallment, npvDebtor, irrDebtor, tcea, totalInterest,
                totalInsurance, totalPostage, totalCommission, ctc)));
    }
}
