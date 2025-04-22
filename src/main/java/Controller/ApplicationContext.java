package Controller;

import InterfaceGS.*;

import Service.*;
import View.ExpenseDashboard;

public class ApplicationContext {
    private static final ApplicationContext instance = new ApplicationContext();

    private final IUserService userService;
    private final ISaldoService saldoService;
    private final MovimentoController movimentoController;
    private final IRegistrationController registrationController;
    private final ILoginController loginController;
    private final IDashboardController dashboardController;

    private EntrataController entrataController;
    private UscitaController uscitaController;
    private ExpenseDashboard dashboard = null; // Lazy init manuale

    // 🔧 Costruttore privato
    private ApplicationContext() {
        this.userService = new UserService();
        this.saldoService = new SaldoService();
        this.loginController = new LoginController();
        this.registrationController = new RegistrationController(userService, saldoService);

        this.dashboardController = new DashboardController(
            saldoService,
            new EntrataService(saldoService),
            new UscitaService(saldoService)
        );

        this.movimentoController = new MovimentoController(); // indipendente dalla dashboard
    }

    // ✅ Da chiamare dopo login (una sola volta)
    public void inizializzaDashboardEDependenze() {
        if (dashboard == null) {
            this.dashboard = new ExpenseDashboard();

            this.entrataController = new EntrataController(
                new EntrataService(saldoService),
                userService,
                saldoService,
                dashboard
            );

            this.uscitaController = new UscitaController(
                new UscitaService(saldoService),
                userService,
                saldoService,
                dashboard
            );
        }
    }

    // 🔁 Singleton getter
    public static ApplicationContext getInstance() {
        return instance;
    }

    // Getter per i servizi
    public IUserService getUserService() {
        return userService;
    }

    public ISaldoService getSaldoService() {
        return saldoService;
    }

    public IMovimentoController getMovimentoController() {
        return movimentoController;
    }

    public IRegistrationController getRegistrationController() {
        return registrationController;
    }

    public ILoginController getLoginController() {
        return loginController;
    }

    public IDashboardController getDashboardController() {
        return dashboardController;
    }

    public IEntrataController getEntrataController() {
        return entrataController;
    }

    public IUscitaController getUscitaController() {
        return uscitaController;
    }

    public ExpenseDashboard getDashboard() {
        return dashboard;
    }
}
