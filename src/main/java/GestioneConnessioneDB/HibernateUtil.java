package GestioneConnessioneDB;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class HibernateUtil {

	private static EntityManagerFactory emf;

	public static EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			try {
				emf = Persistence.createEntityManagerFactory("HibernateJPA");
				System.out.println("Connessione al database riuscita!");
			} catch (Exception e) {
				System.err.println("Errore di connessione al database: " + e.getMessage());
				e.printStackTrace();
				throw new RuntimeException("Impossibile creare EntityManagerFactory", e); // Blocca il programma e
																							// mostra errore
			}
		}
		return emf;
	}

	// Metodo per chiudere la connessione quando l'applicazione termina
	public static void closeEntityManagerFactory() {
		if (emf != null) {
			emf.close();
			System.out.println("EntityManagerFactory chiuso.");
		}
	}// close class

}
