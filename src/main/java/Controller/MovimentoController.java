package Controller;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import InterfaceGS.IMovimentoController;
import java.util.Date;
import java.util.List;
import InterfaceGS.IEntrataService;
import InterfaceGS.ISaldoService;
import InterfaceGS.IUscitaService;
import Service.EntrataService;
import Service.SaldoService;
import Service.UscitaService;
import Entity.Entrata;
import Entity.Uscita;

public class MovimentoController implements IMovimentoController {
    private final IEntrataService entrataService;
    private final IUscitaService uscitaService;
    private final ISaldoService saldoService;

    public MovimentoController(IEntrataService entrataService, IUscitaService uscitaService, ISaldoService saldoService) {
        this.entrataService = entrataService;
        this.uscitaService = uscitaService;
        this.saldoService = saldoService;
    }
 // ✅ Costruttore semplificato per usare sempre la configurazione di default
    public MovimentoController() {
        this(new EntrataService(new SaldoService()), new UscitaService(new SaldoService()), new SaldoService());
    }
    
    public List<Entrata> getEntrate(int userId) {
        return entrataService.getEntrateByUserId(userId);
    }

    public List<Uscita> getUscite(int userId) {
        return uscitaService.getUsciteByUserId(userId);
    }

    public List<Entrata> getEntrateByDate(int userId, Date da, Date a) {
        return entrataService.getEntrateByUserIdAndData(userId, da, a);
    }

    public List<Uscita> getUsciteByDate(int userId, Date da, Date a) {
        return uscitaService.getUsciteByUserIdAndData(userId, da, a);
    }

    public Entrata getEntrataById(int id) {
        return entrataService.getEntrataById(id);
    }

    public Uscita getUscitaById(int id) {
        return uscitaService.getUscitaById(id);
    }

    public boolean modificaEntrata(int id, BigDecimal importo, Date data, String descrizione) {
        return entrataService.modificaEntrata(id, importo, data, descrizione);
    }

    public boolean modificaUscita(int id, BigDecimal importo, Date data, String descrizione) {
        return uscitaService.modificaUscita(id, importo, data, descrizione);
    }

    public BigDecimal getTotaleEntrate(int userId) {
        return entrataService.getTotaleEntrateByUserId(userId);
    }

    public BigDecimal getTotaleUscite(int userId) {
        return uscitaService.getTotaleUsciteByUserId(userId);
    }

    public BigDecimal getSaldoAttuale(int userId) {
        return saldoService.getSaldoByUserId(userId);
    }
    public void aggiornaSaldo(int userId, BigDecimal differenza) {
        saldoService.aggiornaSaldo(userId, differenza);
    }
    public boolean eliminaEntrata(int id) {
        return entrataService.eliminaEntrataById(id);
    }

    public boolean eliminaUscita(int id) {
        return uscitaService.eliminaUscitaById(id);
    }
    public void aggiornaSaldoUtente(int userId, BigDecimal differenza) {
        saldoService.aggiornaSaldo(userId, differenza);
    }
    public List<Entrata> getEntrateByAnno(int userId, int year) {
        Calendar inizio = new GregorianCalendar(year, Calendar.JANUARY, 1);
        Calendar fine = new GregorianCalendar(year, Calendar.DECEMBER, 31);
        return entrataService.getEntrateByUserIdAndData(userId, inizio.getTime(), fine.getTime());
    }
    public List<Uscita> getUsciteByAnno(int userId, int year) {
        Calendar inizio = new GregorianCalendar(year, Calendar.JANUARY, 1);
        Calendar fine = new GregorianCalendar(year, Calendar.DECEMBER, 31);
        return uscitaService.getUsciteByUserIdAndData(userId, inizio.getTime(), fine.getTime());
    }
    
}
