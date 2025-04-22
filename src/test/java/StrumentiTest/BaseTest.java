package StrumentiTest;

import GestioneConnessioneDB.HibernateUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

public abstract class BaseTest {

	protected static EntityManagerFactory emf;

	@BeforeAll
	public static void setupAll() {
		emf = HibernateUtil.getEntityManagerFactory();
	}

	@AfterEach
	void dopoOgniTest() {
		pulisciDb();
	}

	// Metodo per pulire il DB dopo ogni test
	protected void pulisciDb() {
		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();
			em.createQuery("DELETE FROM Entrata").executeUpdate();
			em.createQuery("DELETE FROM Uscita").executeUpdate();
			em.createQuery("DELETE FROM Saldo").executeUpdate();
			em.createQuery("DELETE FROM Utente").executeUpdate();
			em.getTransaction().commit();
		} catch (Exception e) {
			em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	// Chiudi l'EntityManagerFactory alla fine di tutti i test
	@AfterAll
	public static void tearDownAll() {
		if (emf != null) {
			emf.close();
		}
	}
}
