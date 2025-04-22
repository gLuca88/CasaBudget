package Entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "entrate")
public class Entrata {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer entrataId;

	@ManyToOne
	@JoinColumn(name = "userId", nullable = false)
	private Utente utente;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal importo;

	@Column(length = 255)
	private String descrizione;

	@Temporal(TemporalType.DATE)
	@Column(nullable = false)
	private Date data;

	public Entrata(Utente utente, BigDecimal importo, String descrizione, Date data) {
		this.utente = utente;
		this.importo = importo;
		this.descrizione = descrizione;
		this.data = data;
	}

	public Entrata() {
	} // Costruttore vuoto necessario per Hibernate

	// Getter e Setter
	public Integer getEntrataId() {
		return entrataId;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public BigDecimal getImporto() {
		return importo;
	}

	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setEntrataId(Integer entrataId) {
		this.entrataId = entrataId;
	}
	
}