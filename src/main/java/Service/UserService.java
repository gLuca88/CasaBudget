package Service;

import Entity.Utente;


import GestioneConnessioneDB.HibernateUtil;
import InterfaceGS.IUserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserService implements IUserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);
	private Integer currentUserId;
	private String currentUsername;
	private final EntityManagerFactory emf;

	// Il costruttore prende l'EntityManagerFactory da HibernateUtil
	public UserService() {
		this.emf = HibernateUtil.getEntityManagerFactory();
	}

	@Override
	public boolean registerUser(Utente utente) {
		try (EntityManager em = emf.createEntityManager()) {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();

			try {
				// Controllo se l'ID esiste già
				Utente existingUser = em.find(Utente.class, utente.getUserId());
				if (existingUser != null) {
					logger.warn("Errore: l'ID " + utente.getUserId() + " è già in uso.");
					return false;
				}

				em.persist(utente); // Salva il nuovo utente
				transaction.commit();
				return true;
			} catch (Exception e) {
				transaction.rollback();
				logger.error("Errore durante la registrazione dell'utente con ID: " + utente.getUserId(), e);
				return false;
			}
		} catch (Exception e) {
			logger.error("Errore durante la gestione dell'EntityManager per la registrazione utente", e);
			return false;
		}
	}

	@Override
	public boolean authenticateUser(String username, String password) {
	    try (EntityManager em = emf.createEntityManager()) {
	        TypedQuery<Utente> query = em.createQuery(
	            "SELECT u FROM Utente u WHERE u.userName = :username AND u.password = :password", Utente.class);
	        query.setParameter("username", username);
	        query.setParameter("password", password);

	        Utente user = query.getSingleResult();
	        currentUserId = user.getUserId();
	        currentUsername = user.getUserName();
	        return true;
	    } catch (NoResultException e) {
	        currentUserId = null;
	        currentUsername = null;
	        return false;
	    }
	}

	@Override
	public double getSaldoByUserId(int userId) {
		try (EntityManager em = emf.createEntityManager()) {
			TypedQuery<Double> query = em.createQuery("SELECT s.saldo FROM Saldo s WHERE s.utente.userId = :userId",
					Double.class);
			query.setParameter("userId", userId);
			try {
				return query.getSingleResult();
			} catch (NoResultException e) {
				logger.warn("Nessun saldo trovato per l'utente con ID: " + userId);
				return 0.0; // Se non trovato, restituisci 0.0
			}
		} catch (Exception e) {
			logger.error("Errore durante il recupero del saldo per l'utente con ID: " + userId, e);
			return 0.0; // Restituisci 0.0 in caso di errore
		}
	}

	@Override
	public double getTotaleEntrateByUserId(Integer userId) {
		try (EntityManager em = emf.createEntityManager()) {
			TypedQuery<Double> query = em.createQuery(
					"SELECT COALESCE(SUM(e.importo), 0) FROM Entrate e WHERE e.utente.userId = :userId", Double.class);
			query.setParameter("userId", userId);
			return query.getSingleResult();
		} catch (Exception e) {
			logger.error("Errore durante il recupero delle entrate per l'utente con ID: " + userId, e);
			return 0.0; // Restituisci 0.0 in caso di errore
		}
	}

	@Override
	public double getTotaleUsciteByUserId(Integer userId) {
		try (EntityManager em = emf.createEntityManager()) {
			TypedQuery<Double> query = em.createQuery(
					"SELECT COALESCE(SUM(u.importo), 0) FROM Uscite u WHERE u.utente.userId = :userId", Double.class);
			query.setParameter("userId", userId);
			return query.getSingleResult();
		} catch (Exception e) {
			logger.error("Errore durante il recupero delle uscite per l'utente con ID: " + userId, e);
			return 0.0; // Restituisci 0.0 in caso di errore
		}
	}

	@Override
	public Utente getUserById(int userId) {
		try (EntityManager em = emf.createEntityManager()) {
			return em.find(Utente.class, userId); // Trova l'utente per ID
		} catch (Exception e) {
			logger.error("Errore durante il recupero dell'utente con ID: " + userId, e);
			return null; // Restituisci null in caso di errore
		}
	}

	@Override
	public boolean createUser(int userId, String username, String password) {
		try (EntityManager em = emf.createEntityManager()) {
			EntityTransaction transaction = em.getTransaction();
			transaction.begin();

			// Verifica che l'utente non esista già (opzionale)
			TypedQuery<Utente> query = em.createQuery("SELECT u FROM Utente u WHERE u.userName = :username",
					Utente.class);
			query.setParameter("username", username);
			if (!query.getResultList().isEmpty()) {
				// L'utente esiste già
				return false;
			}

			// Crea un nuovo oggetto Utente
			Utente utente = new Utente();
			utente.setUserId(userId); // Imposta l'ID dell'utente
			utente.setUsername(username); // Imposta il nome utente
			utente.setPassword(password); // Imposta la password (deve essere gestita con cifratura in produzione)

			em.persist(utente); // Salva l'utente nel database
			transaction.commit(); // Completa la transazione
			return true;
		} catch (Exception e) {
			// Gestione degli errori
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public Utente login(String username, String password) {
	    try (EntityManager em = emf.createEntityManager()) {
	        TypedQuery<Utente> query = em.createQuery(
	            "SELECT u FROM Utente u WHERE u.userName = :username AND u.password = :password",
	            Utente.class);
	        query.setParameter("username", username);
	        query.setParameter("password", password);

	        return query.getSingleResult();
	    } catch (NoResultException e) {
	        logger.warn("Nessun utente trovato con username: " + username);
	        return null;
	    } catch (Exception e) {
	        logger.error("Errore durante il login per username: " + username, e);
	        return null;
	    }
	}

	@Override
	public Integer getCurrentUserId() {
		return currentUserId;
	}

	@Override
	public String getCurrentUsername() {
		return currentUsername;
	}
}