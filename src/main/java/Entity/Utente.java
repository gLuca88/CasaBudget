package Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // << NECESSARIO: indica che questa classe è un'entità JPA
@Table(name = "utenti") // << Nome della tabella nel database
public class Utente {

	@Id
	private int userId; // L'ID sarà inserito manualmente dall'utente

	@Column(unique = true, nullable = false, length = 50)
	private String userName;

	@Column(nullable = false, length = 255)
	private String password;

	@Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Entrata> entrate = new ArrayList<>();

	@OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Uscita> uscite = new ArrayList<>();

	@OneToOne(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
	private Saldo saldo;

	public Utente(int userId, String username, String password) {
		this.userId = userId;
		this.userName = username;
		this.password = password;
		this.createdAt = LocalDateTime.now(); // Imposta automaticamente la data di creazione
	}

	public Utente() {
		this.entrate = new ArrayList<>();
		this.uscite = new ArrayList<>();
		this.createdAt = LocalDateTime.now();
	}

	// Getter e Setter
	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUsername(String username) {
		this.userName = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public List<Entrata> getEntrate() {
		return entrate;
	}

	public void setEntrate(List<Entrata> entrate) {
		this.entrate = entrate;
	}

	public List<Uscita> getUscite() {
		return uscite;
	}

	public void setUscite(List<Uscita> uscite) {
		this.uscite = uscite;
	}

	public Saldo getSaldo() {
		return saldo;
	}

	public void setSaldo(Saldo saldo) {
		this.saldo = saldo;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
