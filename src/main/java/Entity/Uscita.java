package Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;





@Entity
@Table(name = "uscite")
public class Uscita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer uscitaId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private Utente utente; // L'utente DEVE essere già esistente

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal importo;

    @Column(length = 255)
    private String descrizione;

    @Column(nullable = false)
    private Date data;

    // Costruttore vuoto per Hibernate
    public Uscita() {}

    // Costruttore senza UserService
    public Uscita(Utente utente, BigDecimal importo, String descrizione, Date data) {
        this.utente = utente;
        this.importo = importo;
        this.descrizione = descrizione;
        this.data = data;
    }

    // Getter e Setter
    public Integer getUscitaId() { return uscitaId; }
    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }
    public BigDecimal getImporto() { return importo; }
    public void setImporto(BigDecimal importo) { this.importo = importo; }
    public String getDescrizione() { return descrizione; }
    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }
    public Date getData() { return data; }
    public void setData(Date data) { this.data = data; }
}