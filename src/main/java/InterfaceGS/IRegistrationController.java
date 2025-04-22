package InterfaceGS;

public interface IRegistrationController {
    boolean registerUser(String userIdStr, String username, String password, String confirmPassword, String saldoInizialeStr);
}