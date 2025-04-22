package InterfaceGS;

import java.math.BigDecimal;
import java.util.Date;

public interface IEntrataController {
    boolean inserisciEntrata(BigDecimal importo, String descrizione, Date data);
    boolean eliminaEntrata(int id);
}