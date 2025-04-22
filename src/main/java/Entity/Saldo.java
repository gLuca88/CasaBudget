package Entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "saldo")
public class Saldo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // ID auto-incrementante per saldo
	private Integer saldoId;

	@ManyToOne // Relazione molti-a-uno con l'entità Utente
	@JoinColumn(name = "userId", nullable = false) // userId è la chiave esterna
	private Utente utente;

	@Column(nullable = false, precision = 10, scale = 2) // Precisione e scala per il saldo
	private BigDecimal saldo;

	// Costruttore con parametri
	public Saldo(Integer saldoId, Utente utente, BigDecimal saldo) {
		this.saldoId = saldoId;
		this.utente = utente;
		this.saldo = saldo;
	}

	// Costruttore di default
	public Saldo() {
		super();
	}

	// Getter e Setter
	public Integer getSaldoId() {
		return saldoId;
	}

	public void setSaldoId(Integer saldoId) {
		this.saldoId = saldoId;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

}
