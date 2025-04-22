package Tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entity.Utente;
import InterfaceGS.IUserService;
import Service.UserService;
import StrumentiTest.BaseTest;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class UserServiceTest extends BaseTest {

	private IUserService userService;

	@BeforeEach
	void setUp() {
		userService = new UserService();
		pulisciDb();// Inizializza qui per ogni test
	}

	@Test
	void testRegisterUser() {
		Utente nuovoUtente = new Utente(99, "testUser", "testPassword");
		boolean registrato = userService.registerUser(nuovoUtente);
		assertTrue(registrato, "L'utente dovrebbe essere registrato con successo");
	}

	@Test
	void testUtenteGiaRegistrato() {
		Utente u1 = new Utente(15, "Andrea", "Romano");
		boolean primaRegistrazione = userService.registerUser(u1);
		assertTrue(primaRegistrazione, "La prima registrazione deve avere successo");

		boolean secondaRegistrazione = userService.registerUser(u1);
		assertFalse(secondaRegistrazione, "La seconda registrazione deve fallire perché l'utente esiste già");
	}

	@Test
	void testAutenticazione() {
		Utente u1 = new Utente(2, "Gianluca", "password1");
		Utente u2 = new Utente(5, "Felice", "password2");
		Utente u3 = new Utente(7, "Rosa", "password3");

		List<Utente> utenti = List.of(u1, u2, u3);

		// Verifica che gli utenti non siano autenticabili prima della registrazione
		for (Utente u : utenti) {
			assertFalse(userService.authenticateUser(u.getUserName(), u.getPassword()),
					"L'utente " + u.getUserName() + " non dovrebbe autenticarsi prima della registrazione");
		}

		// Registrazione degli utenti
		for (Utente u : utenti) {
			assertTrue(userService.registerUser(u),
					"L'utente " + u.getUserName() + " dovrebbe registrarsi con successo");
		}

		// Autenticazione corretta
		assertAll("Autenticazione degli utenti registrati",
				() -> assertTrue(userService.authenticateUser(u1.getUserName(), u1.getPassword())),
				() -> assertTrue(userService.authenticateUser(u2.getUserName(), u2.getPassword())),
				() -> assertTrue(userService.authenticateUser(u3.getUserName(), u3.getPassword())));

		// Test con password errata
		assertFalse(userService.authenticateUser(u1.getUserName(), "wrongPassword"),
				"L'autenticazione dovrebbe fallire con password errata");

		// Test con un utente non esistente
		assertFalse(userService.authenticateUser("NonEsistente", "password"),
				"L'autenticazione dovrebbe fallire per un utente non registrato");
	}

}
