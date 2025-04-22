package Controller;

import Entity.Uscita;
import Entity.Utente;
import InterfaceGS.IUscitaController;
import InterfaceGS.IUscitaService;
import InterfaceGS.IUserService;
import InterfaceGS.ISaldoService;
import View.ExpenseDashboard;

import java.math.BigDecimal;
import java.util.Date;

public class UscitaController implements IUscitaController {
    private final IUscitaService uscitaService;
    private final IUserService userService;
    private final ISaldoService saldoService;
    private final ExpenseDashboard dashboard;

    public UscitaController(IUscitaService uscitaService, IUserService userService,
                            ISaldoService saldoService, ExpenseDashboard dashboard) {
        this.uscitaService = uscitaService;
        this.userService = userService;
        this.saldoService = saldoService;
        this.dashboard = dashboard;
    }

    @Override
    public boolean inserisciUscita(BigDecimal importo, String descrizione, Date data) {
        Integer userId = userService.getCurrentUserId();
        Utente utente = userService.getUserById(userId);
        if (utente == null)
            return false;

        Uscita uscita = new Uscita(utente, importo, descrizione, data);
        boolean inserita = uscitaService.inserisciUscita(uscita);

        if (inserita && dashboard != null) {
            dashboard.aggiornaDashboard();
        }

        return inserita;
    }

    @Override
    public boolean eliminaUscita(int id) {
        boolean eliminata = uscitaService.eliminaUscitaById(id);
        if (eliminata && dashboard != null) dashboard.aggiornaDashboard();
        return eliminata;
    }

	public ISaldoService getSaldoService() {
		return saldoService;
	}
}

