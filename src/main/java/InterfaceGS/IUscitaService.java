package InterfaceGS;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import Entity.Uscita;

public interface IUscitaService extends IMovimentoService<Uscita> {
	boolean inserisciUscita(Uscita uscita); // ✅ NECESSARIO

	List<Uscita> getUsciteByUserId(int userId);

	BigDecimal getTotaleUsciteByUserId(int userId);

	List<Uscita> getUsciteByUserIdAndData(int userId, Date da, Date a);

	boolean eliminaUscitaById(int id);

	Uscita getUscitaById(int id);

	boolean modificaUscita(int id, BigDecimal importo, Date data, String descrizione);

}
