package Controller;

import java.math.BigDecimal;

import Entity.Saldo;
import Entity.Utente;
import InterfaceGS.IRegistrationController;
import InterfaceGS.ISaldoService;
import InterfaceGS.IUserService;

public class RegistrationController implements IRegistrationController {
    private final IUserService userService;
    private final ISaldoService saldoService;

    public RegistrationController(IUserService userService, ISaldoService saldoService) {
        this.userService = userService;
        this.saldoService = saldoService;
    }

    @Override
    public boolean registerUser(String userIdStr, String username, String password, String confirmPassword, String saldoInizialeStr) {
        try {
            if (userIdStr.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || saldoInizialeStr.isEmpty()) {
                return false;
            }

            if (!password.equals(confirmPassword)) {
                return false;
            }

            double saldoIniziale = Double.parseDouble(saldoInizialeStr);
            int userId = Integer.parseInt(userIdStr);

            boolean isUserCreated = userService.createUser(userId, username, password);
            if (!isUserCreated) return false;

            Utente utente = userService.getUserById(userId);
            if (utente == null) return false;

            Saldo saldo = new Saldo();
            saldo.setUtente(utente);
            saldo.setSaldo(new BigDecimal(saldoIniziale));

            return saldoService.insertSaldo(saldo);
        } catch (NumberFormatException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
