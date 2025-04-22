package Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entity.Saldo;
import Entity.Uscita;
import Entity.Utente;
import InterfaceGS.ISaldoService;
import InterfaceGS.IUscitaService;
import InterfaceGS.IUserService;
import Service.SaldoService;
import Service.UscitaService;
import Service.UserService;
import StrumentiTest.BaseTest;

public class UscitaServiceTest extends BaseTest {

	private IUscitaService uscitaService;
	private ISaldoService saldoService;
	private IUserService userService;

	@BeforeEach
	void setUp() {
		saldoService = new SaldoService();
		uscitaService = new UscitaService(saldoService); // Passa l'EntityManagerFactory a UscitaService
		userService = new UserService();
		pulisciDb();
	}

	@Test
	void testInserisciUscita() {

		// Crea un utente di test
		Utente utente = new Utente(1, "testUser", "password");
		userService.registerUser(utente);
		assertNotNull(userService.getUserById(1), "L'utente deve esistere nel database");

		// Imposta il saldo iniziale
		saldoService.insertSaldo(new Saldo(null, utente, new BigDecimal("100.00")));

		// Crea una nuova uscita per l'utente
		Uscita uscita = new Uscita(utente, new BigDecimal("30.00"), "Pagamento fattura",
				new Date(System.currentTimeMillis()));

		// Inserisci l'uscita e verifica che sia stata salvata
		boolean inserito = uscitaService.inserisciUscita(uscita);
		assertTrue(inserito, "L'uscita dovrebbe essere inserita con successo");

		// Recupera tutte le uscite dell'utente
		List<Uscita> uscite = uscitaService.getUsciteByUserId(utente.getUserId());
		assertEquals(1, uscite.size(), "L'utente dovrebbe avere una sola uscita registrata");

		// Verifica che il saldo sia stato aggiornato correttamente
		BigDecimal saldoAggiornato = saldoService.getSaldoByUserId(utente.getUserId());
		assertEquals(new BigDecimal("70.00"), saldoAggiornato, "Il saldo aggiornato dovrebbe essere 70.00");
	}

	@Test
	void testGetUsciteByUserId() {
		// 1. Crea un utente di test
		Utente utente = new Utente(1, "testUser", "password");
		userService.registerUser(utente);
		assertNotNull(userService.getUserById(1), "L'utente deve esistere nel database");
		// 2. Imposta un saldo iniziale per l'utente
		saldoService.insertSaldo(new Saldo(null, utente, new BigDecimal("100.00"))); // Saldo di 100.00
		assertNotNull(saldoService.getSaldoByUserId(utente.getUserId()),
				"Il saldo deve essere stato impostato correttamente");

		// 2. Aggiungi delle uscite per l'utente
		Uscita uscita1 = new Uscita(utente, new BigDecimal("50.00"), "Pagamento affitto",
				new Date(System.currentTimeMillis()));
		Uscita uscita2 = new Uscita(utente, new BigDecimal("20.00"), "Pagamento bollette",
				new Date(System.currentTimeMillis()));

		// Inserisci le uscite
		uscitaService.inserisciUscita(uscita1);
		uscitaService.inserisciUscita(uscita2);

		// 3. Recupera le uscite per l'utente
		List<Uscita> uscite = uscitaService.getUsciteByUserId(utente.getUserId());

		// 4. Verifica che le uscite siano state recuperate correttamente
		assertNotNull(uscite, "La lista delle uscite non deve essere nulla");
		assertEquals(2, uscite.size(), "L'utente dovrebbe avere 2 uscite registrate");

		// 5. Verifica che le uscite siano corrette
		Uscita uscitaRecuperata1 = uscite.get(0);
		Uscita uscitaRecuperata2 = uscite.get(1);

		assertEquals(new BigDecimal("50.00"), uscitaRecuperata1.getImporto(),
				"L'importo dell'uscita 1 dovrebbe essere 50.00");
		assertEquals("Pagamento affitto", uscitaRecuperata1.getDescrizione(),
				"La descrizione dell'uscita 1 dovrebbe essere corretta");

		assertEquals(new BigDecimal("20.00"), uscitaRecuperata2.getImporto(),
				"L'importo dell'uscita 2 dovrebbe essere 20.00");
		assertEquals("Pagamento bollette", uscitaRecuperata2.getDescrizione(),
				"La descrizione dell'uscita 2 dovrebbe essere corretta");

	}

	@Test
	void testInserisciUscitaConSaldoNonDisponibile() {
		// 1. Crea un utente di test
		Utente utente = new Utente(1, "testUser", "password");
		userService.registerUser(utente);
		assertNotNull(userService.getUserById(1), "L'utente deve esistere nel database");

		// 2. Non inserire alcun saldo (saldo = null) o imposta saldo a 0
		// Se non viene impostato saldo, la logica nel servizio dovrebbe impedire
		// l'inserimento dell'uscita

		// 3. Crea un'uscita per l'utente
		Uscita uscita = new Uscita(utente, new BigDecimal("30.00"), "Pagamento bollette",
				new Date(System.currentTimeMillis()));

		// 4. Prova a inserire l'uscita
		boolean inserita = uscitaService.inserisciUscita(uscita);

		// 5. Verifica che l'uscita non sia stata inserita
		assertFalse(inserita, "L'uscita non dovrebbe essere inserita se il saldo non è disponibile o è zero");

		// 6. Verifica che non ci siano uscite per l'utente (lista vuota)
		List<Uscita> uscite = uscitaService.getUsciteByUserId(utente.getUserId());
		assertEquals(0, uscite.size(), "L'utente non dovrebbe avere uscite se il saldo è zero o non disponibile");

		// 7. Verifica che il saldo non sia stato modificato
		BigDecimal saldoRecuperato = saldoService.getSaldoByUserId(utente.getUserId());
		assertEquals(BigDecimal.ZERO, saldoRecuperato,
				"Il saldo dovrebbe essere zero perché non è stato possibile inserire l'uscita");
	}

	@Test
	void testInserisciUscitaConSaldoInsufficiente() {
		// 1. Crea un utente di test
		Utente utente = new Utente(1, "testUser", "password");
		userService.registerUser(utente);
		assertNotNull(userService.getUserById(1), "L'utente deve esistere nel database");

		// 2. Imposta un saldo iniziale (ad esempio 50.00)
		saldoService.insertSaldo(new Saldo(null, utente, new BigDecimal("50.00")));
		assertNotNull(saldoService.getSaldoByUserId(utente.getUserId()),
				"Il saldo dovrebbe essere impostato correttamente");

		// 3. Crea due uscite per l'utente con importi superiori al saldo disponibile
		// (50.00)
		Uscita uscita1 = new Uscita(utente, new BigDecimal("70.00"), "Pagamento affitto",
				new Date(System.currentTimeMillis()));
		Uscita uscita2 = new Uscita(utente, new BigDecimal("100.00"), "Pagamento bollette",
				new Date(System.currentTimeMillis()));

		// 4. Prova a inserire le uscite
		boolean inserita1 = uscitaService.inserisciUscita(uscita1);
		boolean inserita2 = uscitaService.inserisciUscita(uscita2);

		// 5. Verifica che nessuna delle uscite sia stata inserita (saldo insufficiente)
		assertFalse(inserita1, "L'uscita 1 non dovrebbe essere inserita, saldo insufficiente");
		assertFalse(inserita2, "L'uscita 2 non dovrebbe essere inserita, saldo insufficiente");

		// 6. Verifica che non ci siano uscite per l'utente
		List<Uscita> uscite = uscitaService.getUsciteByUserId(utente.getUserId());
		assertEquals(0, uscite.size(), "L'utente non dovrebbe avere uscite se il saldo è insufficiente");

		// 7. Verifica che il saldo non sia stato modificato (dovrebbe rimanere a 50.00)
		BigDecimal saldoRecuperato = saldoService.getSaldoByUserId(utente.getUserId());
		assertEquals(new BigDecimal("50.00"), saldoRecuperato,
				"Il saldo dovrebbe rimanere invariato se l'uscita non viene inserita");
	}

}
