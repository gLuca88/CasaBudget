package Service;

import Entity.Saldo;
import GestioneConnessioneDB.HibernateUtil;
import InterfaceGS.ISaldoService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaldoService implements ISaldoService {

	private static final Logger logger = LoggerFactory.getLogger(SaldoService.class);
	private final EntityManagerFactory emf;

	// Il costruttore prende l'EntityManagerFactory da HibernateUtil
	public SaldoService() {
		this.emf = HibernateUtil.getEntityManagerFactory();
	}

	@Override
	public boolean insertSaldo(Saldo saldo) {
		try (EntityManager em = emf.createEntityManager()) {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();

			try {
				em.persist(saldo); // Salva il saldo
				em.flush(); // FORZA L'INSERIMENTO IMMEDIATO NEL DATABASE
				transaction.commit();
				return true;
			} catch (Exception e) {
				transaction.rollback();
				logger.error("Errore durante l'inserimento del saldo: " + e.getMessage(), e);
				return false;
			}
		} catch (Exception e) {
			logger.error("Errore durante la gestione dell'EntityManager per l'inserimento saldo: " + e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean updateSaldo(int userId, BigDecimal nuovoSaldo) {
		try (EntityManager em = emf.createEntityManager()) {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();

			try {
				// Recupera il saldo basandosi sull'ID utente
				TypedQuery<Saldo> query = em.createQuery("SELECT s FROM Saldo s WHERE s.utente.userId = :userId",
						Saldo.class);
				query.setParameter("userId", userId);

				Saldo saldo = query.getSingleResult(); // Ottiene il saldo dell'utente
				if (saldo != null) {
					saldo.setSaldo(nuovoSaldo); // Aggiorna il saldo
					em.merge(saldo); // Salva le modifiche
					transaction.commit();
					return true;
				} else {
					logger.warn("Nessun saldo trovato per l'utente con ID: " + userId);
					return false;
				}
			} catch (NoResultException e) {
				transaction.rollback();
				logger.error("Nessun saldo trovato per l'utente con ID: " + userId, e);
				return false;
			} catch (Exception e) {
				transaction.rollback();
				logger.error("Errore durante l'aggiornamento del saldo per l'utente con ID: " + userId, e);
				return false;
			}
		} catch (Exception e) {
			logger.error("Errore durante la gestione dell'EntityManager per l'aggiornamento saldo: " + e.getMessage(),
					e);
			return false;
		}
	}

	@Override
	public BigDecimal getSaldoByUserId(int userId) {
		try (EntityManager em = emf.createEntityManager()) {
			TypedQuery<BigDecimal> query = em.createQuery("SELECT s.saldo FROM Saldo s WHERE s.utente.userId = :userId",
					BigDecimal.class);
			query.setParameter("userId", userId);

			try {
				return query.getSingleResult(); // Ottieni il saldo
			} catch (NoResultException e) {
				logger.warn("Nessun saldo trovato per l'utente con ID: " + userId);
				return BigDecimal.ZERO; // Se non trovato, restituisci saldo zero
			}
		} catch (Exception e) {
			logger.error("Errore durante il recupero del saldo per l'utente con ID: " + userId, e);
			return BigDecimal.ZERO; // In caso di errore, restituisci saldo zero
		}
	}
	@Override
	public void aggiornaSaldo(int userId, BigDecimal differenza) {
	    EntityManager em = HibernateUtil.getEntityManagerFactory().createEntityManager();
	    EntityTransaction tx = em.getTransaction();
	    System.out.println("🔧 aggiornaSaldo chiamato - userId: " + userId + ", differenza: " + differenza);

	    try {
	        tx.begin();
	        Saldo saldo = em.createQuery("SELECT s FROM Saldo s WHERE s.utente.userId = :userId", Saldo.class)
	                        .setParameter("userId", userId)
	                        .getSingleResult();

	        if (saldo != null && saldo.getSaldo() != null) {
	            BigDecimal nuovoSaldo = saldo.getSaldo().add(differenza);
	            saldo.setSaldo(nuovoSaldo);
	            em.merge(saldo);
	        }

	        tx.commit();
	    } catch (Exception e) {
	        if (tx.isActive()) tx.rollback();
	        e.printStackTrace();
	    } finally {
	        em.close();
	    }
	}
}
