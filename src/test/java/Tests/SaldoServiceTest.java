package Tests;

import StrumentiTest.BaseTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entity.Saldo;
import Entity.Utente;
import InterfaceGS.ISaldoService;
import InterfaceGS.IUserService;
import Service.SaldoService;
import Service.UserService;

import java.math.BigDecimal;

public class SaldoServiceTest extends BaseTest {

	private ISaldoService saldoService;
	private IUserService userService; // Per gestire l'utente

	@BeforeEach
	void setUp() {
		saldoService = new SaldoService(); // Inizializza il servizio Saldo
		userService = new UserService(); // Inizializza il servizio Utente
		pulisciDb(); // Pulisce il DB prima di ogni test (dal BaseTest)
	}

	@Test
	void testInsertSaldo() {
		Utente utente = new Utente(1, "testUser", "password");
		userService.registerUser(utente);

		// Verifica che l'utente sia stato registrato
		assertNotNull(userService.getUserById(utente.getUserId()), "L'utente deve esistere prima di inserire il saldo");

		// Creazione del saldo per l'utente
		Saldo saldo = new Saldo(null, utente, new BigDecimal("100.00"));

		// Inserimento del saldo
		boolean inserito = saldoService.insertSaldo(saldo);
		assertTrue(inserito, "Il saldo dovrebbe essere inserito con successo");

		// Recupero del saldo e verifica
		BigDecimal saldoRecuperato = saldoService.getSaldoByUserId(utente.getUserId());
		assertEquals(new BigDecimal("100.00"), saldoRecuperato,
				"Il saldo recuperato dovrebbe essere uguale a quello inserito");
	}
	@Test
	void testUpdateSaldo() {
	    // 1️⃣ Creiamo e registriamo un utente
	    Utente utente = new Utente(1, "testUser", "password");
	    userService.registerUser(utente);

	    // 2️⃣ Inseriamo un saldo iniziale
	    Saldo saldoIniziale = new Saldo(null, utente, new BigDecimal("50.00"));
	    saldoService.insertSaldo(saldoIniziale);

	    // 3️⃣ Verifichiamo che il saldo iniziale sia stato salvato correttamente
	    BigDecimal saldoRecuperato = saldoService.getSaldoByUserId(utente.getUserId());
	    assertEquals(new BigDecimal("50.00"), saldoRecuperato, "Il saldo iniziale dovrebbe essere 50.00");

	    // 4️⃣ Aggiorniamo il saldo
	    boolean aggiornato = saldoService.updateSaldo(utente.getUserId(), new BigDecimal("100.00"));
	    assertTrue(aggiornato, "L'aggiornamento del saldo dovrebbe avere successo");

	    // 5️⃣ Recuperiamo il saldo aggiornato e verifichiamo la modifica
	    BigDecimal saldoAggiornato = saldoService.getSaldoByUserId(utente.getUserId());
	    assertEquals(new BigDecimal("100.00"), saldoAggiornato, "Il saldo aggiornato dovrebbe essere 100.00");
	}

}
