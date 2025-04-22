package Controller;

import java.math.BigDecimal;

import Entity.Utente;
import InterfaceGS.IDashboardController;
import InterfaceGS.IEntrataService;
import InterfaceGS.ISaldoService;
import InterfaceGS.IUscitaService;
import View.ExpenseDashboard;

public class DashboardController implements IDashboardController {
	private final ISaldoService saldoService;
	private final IEntrataService entrataService;
	private final IUscitaService uscitaService;

	public DashboardController(ISaldoService saldoService, IEntrataService entrataService,
			IUscitaService uscitaService) {
		this.saldoService = saldoService;
		this.entrataService = entrataService;
		this.uscitaService = uscitaService;
	}

	@Override
	public void aggiornaDashboardUI(ExpenseDashboard dashboard) {
		try {
			Utente utente = ControlloSessioneUtente.getInstance().getUtenteLoggato();
			int userId = utente.getUserId();

			BigDecimal totaleEntrate = entrataService.getTotaleEntrateByUserId(userId);
			BigDecimal totaleUscite = uscitaService.getTotaleUsciteByUserId(userId);
			BigDecimal saldoAttuale = saldoService.getSaldoByUserId(userId);

			dashboard.updateFields(utente.getUserName(), totaleEntrate, totaleUscite, saldoAttuale);
		} catch (Exception e) {
			dashboard.showErrore("Errore nel caricamento dati utente: " + e.getMessage());
		}
	}

}
