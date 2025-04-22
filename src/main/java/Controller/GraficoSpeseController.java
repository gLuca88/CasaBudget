package Controller;

import Entity.Entrata;
import Entity.Uscita;
import InterfaceGS.IMovimentoController;

import java.math.BigDecimal;
import java.util.*;

public class GraficoSpeseController {
	private final IMovimentoController movimentoController;

	public GraficoSpeseController(IMovimentoController movimentoController) {
		this.movimentoController = movimentoController;
	}

	public Map<Integer, BigDecimal> getEntrateMensili(int userId, int year) {
		Map<Integer, BigDecimal> entrate = initMeseMap();
		for (Entrata e : movimentoController.getEntrateByAnno(userId, year)) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(e.getData());
			int mese = cal.get(Calendar.MONTH);
			entrate.put(mese, entrate.get(mese).add(e.getImporto()));
		}
		return entrate;
	}

	public Map<Integer, BigDecimal> getUsciteMensili(int userId, int year) {
		Map<Integer, BigDecimal> uscite = initMeseMap();
		for (Uscita u : movimentoController.getUsciteByAnno(userId, year)) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(u.getData());
			int mese = cal.get(Calendar.MONTH);
			uscite.put(mese, uscite.get(mese).add(u.getImporto()));
		}
		return uscite;
	}

	private Map<Integer, BigDecimal> initMeseMap() {
		Map<Integer, BigDecimal> map = new HashMap<>();
		for (int i = 0; i < 12; i++) {
			map.put(i, BigDecimal.ZERO);
		}
		return map;
	}
}
