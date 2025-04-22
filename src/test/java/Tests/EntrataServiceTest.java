package Tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Entity.Entrata;
import Entity.Saldo;
import Entity.Utente;
import InterfaceGS.IEntrataService;
import InterfaceGS.ISaldoService;
import InterfaceGS.IUserService;
import Service.EntrataService;
import Service.SaldoService;
import Service.UserService;
import StrumentiTest.BaseTest;

public class EntrataServiceTest extends BaseTest {
	private IEntrataService entrataService;
	private ISaldoService saldoService;
	private IUserService userService;

	@BeforeEach
	void setUp() {
		saldoService = new SaldoService();
		entrataService = new EntrataService(saldoService);
		userService = new UserService();
		pulisciDb(); // Pulisce il DB prima di ogni test
	}

	@Test
	void testInserisciEntrata() {
		// 1️⃣ Crea e registra un nuovo utente
		Utente utente = new Utente(1, "testUser", "password");
		userService.registerUser(utente);

		// 2️⃣ Verifica che l'utente sia stato registrato correttamente
		assertNotNull(userService.getUserById(utente.getUserId()), "L'utente deve esistere");

		// 3️⃣ Inserisce un saldo iniziale per l'utente
		saldoService.insertSaldo(new Saldo(null, utente, new BigDecimal("50.00")));

		// 4️⃣ Crea una nuova entrata per l'utente
		Entrata entrata = new Entrata(utente, new BigDecimal("30.00"), "Pagamento ricevuto",
				new Date(System.currentTimeMillis()));

		// 5️⃣ Inserisce l'entrata e verifica che sia stata salvata
		boolean inserito = entrataService.inserisciEntrata(entrata);
		assertTrue(inserito, "L'entrata dovrebbe essere inserita con successo");

		// 6️⃣ Recupera tutte le entrate dell'utente
		List<Entrata> entrate = entrataService.getEntrateByUserId(utente.getUserId());
		assertEquals(1, entrate.size(), "L'utente dovrebbe avere una sola entrata registrata");

		// 7️⃣ Verifica che il saldo sia stato aggiornato correttamente
		BigDecimal saldoAggiornato = saldoService.getSaldoByUserId(utente.getUserId());
		assertEquals(new BigDecimal("80.00"), saldoAggiornato, "Il saldo aggiornato dovrebbe essere 80.00");
	}

	@Test
	void testGetEntrateByUserId() {
		Utente utente = new Utente(1, "testUser", "password");
		userService.registerUser(utente);

		// Verifica che l'utente sia stato registrato correttamente
		Utente utenteSalvato = userService.getUserById(1);
		assertThat(utenteSalvato).isNotNull();

		// Creazione e inserimento di due entrate per l'utente
		Entrata entrata1 = new Entrata(utenteSalvato, new BigDecimal("50.00"), "Stipendio", new Date());
		Entrata entrata2 = new Entrata(utenteSalvato, new BigDecimal("30.00"), "Bonus", new Date());

		entrataService.inserisciEntrata(entrata1);
		entrataService.inserisciEntrata(entrata2);

		// Recupero delle entrate dal servizio
		List<Entrata> entrate = entrataService.getEntrateByUserId(1);

		// Verifiche con AssertJ
		assertThat(entrate).isNotNull();
		assertThat(entrate).hasSize(2);
		assertThat(entrate).extracting(Entrata::getImporto).containsExactlyInAnyOrder(new BigDecimal("50.00"),
				new BigDecimal("30.00"));
		assertThat(entrate).extracting(Entrata::getDescrizione).containsExactlyInAnyOrder("Stipendio", "Bonus");

	}

}
