package InterfaceGS;

import java.math.BigDecimal;

import Entity.Saldo;

public interface ISaldoService {

	   // Metodo per inserire un saldo nel database
    boolean insertSaldo(Saldo saldo);

    // Metodo per aggiornare il saldo di un determinato utente
    boolean updateSaldo(int userId, BigDecimal nuovoSaldo);
    
    // Metodo per ottenere il saldo di un determinato utente
    BigDecimal getSaldoByUserId(int userId);
	
    void aggiornaSaldo(int userId, BigDecimal differenza);
	
}
