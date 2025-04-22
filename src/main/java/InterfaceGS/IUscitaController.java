package InterfaceGS;

import java.math.BigDecimal;
import java.util.Date;

public interface IUscitaController {
    boolean inserisciUscita(BigDecimal importo, String descrizione, Date data);
    boolean eliminaUscita(int id);
}