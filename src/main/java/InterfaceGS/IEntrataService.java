package InterfaceGS;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import Entity.Entrata;

public interface IEntrataService extends IMovimentoService<Entrata> {
	boolean inserisciEntrata(Entrata entrata); // ✅ NECESSARIO

	List<Entrata> getEntrateByUserId(int userId);

	BigDecimal getTotaleEntrateByUserId(int userId);

	List<Entrata> getEntrateByUserIdAndData(int userId, Date da, Date a);

	boolean eliminaEntrataById(int id);

	Entrata getEntrataById(int id);

	boolean modificaEntrata(int id, BigDecimal importo, Date data, String descrizione);

}