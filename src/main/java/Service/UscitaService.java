package Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import Entity.Uscita;
import Entity.Utente;
import GestioneConnessioneDB.HibernateUtil;
import InterfaceGS.IMovimentoService;
import InterfaceGS.ISaldoService;
import InterfaceGS.IUscitaService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

public class UscitaService implements IUscitaService, IMovimentoService<Uscita> {
	private final EntityManagerFactory emf;
	private final ISaldoService saldoService; // Servizio per la gestione del saldo

	// Costruttore per inizializzare l'EntityManagerFactory e SaldoService
	public UscitaService(ISaldoService saldoService) {
		this.emf = HibernateUtil.getEntityManagerFactory();
		this.saldoService = saldoService;
	}

	@Override
	public boolean inserisciUscita(Uscita uscita) {
		// Apre una sessione di EntityManager e transazione
		try (EntityManager em = emf.createEntityManager()) {
			EntityTransaction transaction = em.getTransaction();

			try {
				transaction.begin();

				// Associa l'utente all'uscita
				Utente utente = em.find(Utente.class, uscita.getUtente().getUserId());
				if (utente == null) {
					throw new IllegalArgumentException("L'utente non esiste");
				}
				uscita.setUtente(utente);

				// Recupera il saldo attuale
				BigDecimal saldoCorrente = saldoService.getSaldoByUserId(utente.getUserId());

				// Controlla se il saldo è sufficiente per l'uscita
				if (saldoCorrente == null || saldoCorrente.compareTo(BigDecimal.ZERO) <= 0
						|| saldoCorrente.compareTo(uscita.getImporto()) < 0) {
					// Se il saldo non è sufficiente o è zero, non inserire l'uscita
					return false;
				}

				// Salva la nuova uscita
				em.persist(uscita);

				// Calcola il nuovo saldo e aggiorna nel database
				BigDecimal nuovoSaldo = saldoCorrente.subtract(uscita.getImporto());
				saldoService.updateSaldo(utente.getUserId(), nuovoSaldo);

				// Commit della transazione
				transaction.commit();
				return true;
			} catch (Exception e) {
				// Gestione delle eccezioni
				transaction.rollback();
				e.printStackTrace();
				return false;
			}
		}
	}

	@Override
	public List<Uscita> getUsciteByUserId(int userId) {
		try (EntityManager em = emf.createEntityManager()) {
			// Esegui la query per ottenere le uscite per l'utente
			TypedQuery<Uscita> query = em.createQuery("SELECT u FROM Uscita u WHERE u.utente.userId = :userId",
					Uscita.class);
			query.setParameter("userId", userId);
			return query.getResultList();
		} catch (PersistenceException e) {
			// Gestisci le eccezioni relative alla persistenza
			e.printStackTrace();
			return null; // O in alternativa una lista vuota, dipende dalle tue esigenze
		} catch (Exception e) {
			// Gestisci eventuali altre eccezioni
			e.printStackTrace();
			return null; // O in alternativa una lista vuota
		}

	}

	@Override
	public BigDecimal getTotaleUsciteByUserId(int userId) {
		try (EntityManager em = emf.createEntityManager()) {
			TypedQuery<BigDecimal> query = em.createQuery(
					"SELECT COALESCE(SUM(u.importo), 0) FROM Uscita u WHERE u.utente.userId = :userId",
					BigDecimal.class);
			query.setParameter("userId", userId);
			return query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return BigDecimal.ZERO;
		}
	}

	@Override
	public List<Uscita> getMovimentiByUserId(int userId) {
		return getUsciteByUserId(userId);
	}

	@Override
	public List<Uscita> getUsciteByUserIdAndData(int userId, Date da, Date a) {
		try (EntityManager em = emf.createEntityManager()) {
			TypedQuery<Uscita> query = em.createQuery(
					"SELECT u FROM Uscita u WHERE u.utente.userId = :userId AND u.data BETWEEN :da AND :a",
					Uscita.class);
			query.setParameter("userId", userId);
			query.setParameter("da", da);
			query.setParameter("a", a);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	@Override
	public boolean eliminaUscitaById(int id) {
		try (EntityManager em = emf.createEntityManager()) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			Uscita uscita = em.find(Uscita.class, id);
			if (uscita != null) {
				em.remove(uscita);
				tx.commit();
				return true;
			} else {
				tx.rollback();
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public Uscita getUscitaById(int id) {
		try (EntityManager em = emf.createEntityManager()) {
			return em.find(Uscita.class, id);
		}
	}

	@Override
	public boolean modificaUscita(int id, BigDecimal importo, Date data, String descrizione) {
		try (EntityManager em = emf.createEntityManager()) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			Uscita uscita = em.find(Uscita.class, id);
			if (uscita != null) {
				uscita.setImporto(importo);
				uscita.setData(data);
				uscita.setDescrizione(descrizione);
				tx.commit();
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
