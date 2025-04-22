package Service;

import java.math.BigDecimal;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Entity.Entrata;
import Entity.Utente;
import GestioneConnessioneDB.HibernateUtil;
import InterfaceGS.IEntrataService;
import InterfaceGS.IMovimentoService;
import InterfaceGS.ISaldoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

public class EntrataService implements IEntrataService, IMovimentoService<Entrata> {
	private final EntityManagerFactory emf;
	private final ISaldoService saldoService;
	private static final Logger logger = LoggerFactory.getLogger(EntrataService.class); // Logger per il logging

	public EntrataService(ISaldoService saldoService) {
		this.emf = HibernateUtil.getEntityManagerFactory();
		this.saldoService = saldoService;
	}

	@Override
	public boolean inserisciEntrata(Entrata entrata) {
		try (EntityManager em = emf.createEntityManager()) { // try-with-resources per gestire EntityManager
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();

			// Associa l'utente alla nuova entrata
			Utente utente = em.find(Utente.class, entrata.getUtente().getUserId());
			if (utente == null) {
				throw new IllegalArgumentException(
						"L'utente con ID " + entrata.getUtente().getUserId() + " non esiste.");
			}
			entrata.setUtente(utente);

			em.persist(entrata); // Salva la nuova entrata

			// Recupera il saldo attuale, aggiorna con la nuova entrata e salva nel database
			BigDecimal saldoCorrente = saldoService.getSaldoByUserId(utente.getUserId());
			BigDecimal nuovoSaldo = saldoCorrente.add(entrata.getImporto());
			saldoService.updateSaldo(utente.getUserId(), nuovoSaldo);

			transaction.commit(); // Completa la transazione
			return true;
		} catch (IllegalArgumentException e) {
			logger.error("Errore: " + e.getMessage());
			return false; // Gestisce errori di logica come utente non trovato
		} catch (PersistenceException e) {
			logger.error("Errore di persistenza: " + e.getMessage(), e); // Logga l'errore di persistenza
			return false;
		} catch (Exception e) {
			logger.error("Errore imprevisto: " + e.getMessage(), e); // Logga altre eccezioni generiche
			return false;
		}
	}

	@Override
	public List<Entrata> getEntrateByUserId(int userId) {
		try (EntityManager em = emf.createEntityManager()) { // try-with-resources per gestire EntityManager
			TypedQuery<Entrata> query = em.createQuery("SELECT e FROM Entrata e WHERE e.utente.userId = :userId",
					Entrata.class);
			query.setParameter("userId", userId);
			return query.getResultList();
		} catch (PersistenceException e) {
			logger.error("Errore di persistenza durante il recupero delle entrate per l'utente ID " + userId, e);
			return Collections.emptyList(); // Restituisce una lista vuota in caso di errore
		} catch (Exception e) {
			logger.error("Errore imprevisto durante il recupero delle entrate per l'utente ID " + userId, e);
			return Collections.emptyList(); // Restituisce una lista vuota in caso di errore
		}
	}

	@Override
	public BigDecimal getTotaleEntrateByUserId(int userId) {
		try (EntityManager em = emf.createEntityManager()) {
			TypedQuery<BigDecimal> query = em.createQuery(
					"SELECT COALESCE(SUM(e.importo), 0) FROM Entrata e WHERE e.utente.userId = :userId",
					BigDecimal.class);
			query.setParameter("userId", userId);
			return query.getSingleResult();
		} catch (Exception e) {
			logger.error("Errore durante il calcolo del totale entrate", e);
			return BigDecimal.ZERO;
		}
	}

	@Override
	public List<Entrata> getMovimentiByUserId(int userId) {
		return getEntrateByUserId(userId);
	}

	@Override
	public List<Entrata> getEntrateByUserIdAndData(int userId, Date da, Date a) {
		try (EntityManager em = emf.createEntityManager()) {
			TypedQuery<Entrata> query = em.createQuery(
					"SELECT e FROM Entrata e WHERE e.utente.userId = :userId AND e.data BETWEEN :da AND :a",
					Entrata.class);
			query.setParameter("userId", userId);
			query.setParameter("da", da);
			query.setParameter("a", a);
			return query.getResultList();
		} catch (Exception e) {
			logger.error("Errore nel filtro per data delle entrate", e);
			return Collections.emptyList();
		}
	}

	@Override
	public boolean eliminaEntrataById(int id) {
		try (EntityManager em = emf.createEntityManager()) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();

			Entrata entrata = em.find(Entrata.class, id);
			if (entrata != null) {
				em.remove(entrata);
				tx.commit();
				return true;
			} else {
				tx.rollback();
				return false;
			}
		} catch (Exception e) {
			logger.error("Errore durante l'eliminazione dell'entrata", e);
			return false;
		}
	}

	@Override
	public Entrata getEntrataById(int id) {
		try (EntityManager em = emf.createEntityManager()) {
			return em.find(Entrata.class, id);
		}
	}

	@Override
	public boolean modificaEntrata(int id, BigDecimal importo, Date data, String descrizione) {
		try (EntityManager em = emf.createEntityManager()) {
			EntityTransaction tx = em.getTransaction();
			tx.begin();
			Entrata entrata = em.find(Entrata.class, id);
			if (entrata != null) {
				entrata.setImporto(importo);
				entrata.setData(data);
				entrata.setDescrizione(descrizione);
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