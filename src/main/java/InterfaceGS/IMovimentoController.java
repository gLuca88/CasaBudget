package InterfaceGS;

import Entity.Entrata;
import Entity.Uscita;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface IMovimentoController {
    List<Entrata> getEntrate(int userId);
    List<Uscita> getUscite(int userId);
    List<Entrata> getEntrateByDate(int userId, Date da, Date a);
    List<Uscita> getUsciteByDate(int userId, Date da, Date a);
    Entrata getEntrataById(int id);
    Uscita getUscitaById(int id);
    boolean modificaEntrata(int id, BigDecimal importo, Date data, String descrizione);
    boolean modificaUscita(int id, BigDecimal importo, Date data, String descrizione);
    boolean eliminaEntrata(int id);
    boolean eliminaUscita(int id);
    BigDecimal getTotaleEntrate(int userId);
    BigDecimal getTotaleUscite(int userId);
    BigDecimal getSaldoAttuale(int userId);
    void aggiornaSaldoUtente(int userId, BigDecimal differenza);
    List<Entrata> getEntrateByAnno(int userId, int year);
    List<Uscita> getUsciteByAnno(int userId, int year);
}