package Controller;

import Entity.Entrata;
import Entity.Utente;
import InterfaceGS.IEntrataController;
import InterfaceGS.IEntrataService;
import InterfaceGS.ISaldoService;
import InterfaceGS.IUserService;
import View.ExpenseDashboard;

import java.math.BigDecimal;
import java.util.Date;

public class EntrataController implements IEntrataController {
    private final IEntrataService entrataService;
    private final IUserService userService;
    private final ISaldoService saldoService;
    private final ExpenseDashboard dashboard;

    public EntrataController(IEntrataService entrataService, IUserService userService,
                             ISaldoService saldoService, ExpenseDashboard dashboard) {
        this.entrataService = entrataService;
        this.userService = userService;
        this.saldoService = saldoService;
        this.dashboard = dashboard;
    }

    @Override
    public boolean inserisciEntrata(BigDecimal importo, String descrizione, Date data) {
        Integer userId = userService.getCurrentUserId();
        Utente utente = userService.getUserById(userId);
        if (utente == null) return false;

        Entrata entrata = new Entrata(utente, importo, descrizione, data);
        boolean inserita = entrataService.inserisciEntrata(entrata);

        if (inserita && dashboard != null) {
            dashboard.aggiornaDashboard(); // puoi mantenere questo o sostituirlo con dashboardController.aggiornaDashboardUI(this);
        }

        return inserita;
    }

    @Override
    public boolean eliminaEntrata(int id) {
        boolean eliminata = entrataService.eliminaEntrataById(id);
        if (eliminata && dashboard != null) dashboard.aggiornaDashboard();
        return eliminata;
    }

	public ISaldoService getSaldoService() {
		return saldoService;
	}
}
